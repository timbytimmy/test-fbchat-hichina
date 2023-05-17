<template>
  <div class="wrapper">
    <el-row class="title" justify="center">
      <el-col :span="18"
        ><el-input v-model="title" placeholder="Blog title"
      /></el-col>
    </el-row>

    <div class="edit">
      <QuillEditor
        theme="snow"
        v-model:content="content"
        contentType="html"
        toolbar="full"
        :modules="combineModule"
      />
    </div>

    <div style="margin-top: 30px">
      <el-row class="title" justify="center">
        <el-col :span="18"
          ><el-input v-model="bannerImageUrl" placeholder="Banner Image Url" />
        </el-col>
      </el-row>
    </div>

    <el-row
      justify="center"
      v-if="bannerImageUrl != null && bannerImageUrl != ''"
    >
      <v-lazy-image style="height: 440px; width: 950px" :src="bannerImageUrl" />
    </el-row>

    <div class="btns flex-between">
      <p class="post" @click="updateBlog()">Update</p>
    </div>
  </div>
</template>
  
  <script setup>
import VLazyImage from "v-lazy-image";
import { AXIOS } from "@/common/http-commons";
import ImageUploader from "quill-image-uploader";
import { useRouter } from "vue-router";
import { ElNotification } from "element-plus";
const title = ref("");

const router = useRouter();

const route = useRoute();

const content = ref("");

const bannerImageUrl = ref("");

const imageUploadModule = {
  name: "imageUploader",
  module: ImageUploader,
  options: {
    upload: (file) => {
      return new Promise((resolve, reject) => {
        const formData = new FormData();
        formData.append("imageFile", file);
        formData.append("expectedType", "blogImage");

        AXIOS.post("/api/v1/image/upload", formData)
          .then((res) => {
            resolve(res.data.data);
          })
          .catch((err) => {
            reject("Upload failed");
            console.error("Error:", err);
          });
      });
    },
  },
};

const combineModule = [imageUploadModule];

function whoami() {
  AXIOS.get("/api/v1/user/whoami")
    .then(function (response) {
      console.log("current user in setup: " + response.data);
    })
    .catch(function (error) {
      console.log("currently not logged in setup: " + error);
      router.push("/auth/login");
    });
}

function loadBlogDetail(blogId) {
  AXIOS.get("/api/v1/blog/" + blogId)
    .then(function (response) {
      console.log("blog detail:");
      console.log(response.data);
      title.value = response.data.data.title;
      content.value = response.data.data.content;
      bannerImageUrl.value = response.data.data.headImageUrl;
    })
    .catch(function (error) {
      console.log("currently not logged in blog edit: " + error);
      router.push("/auth/login");
    });
}

onMounted(() => {
  whoami();

  loadBlogDetail(route.params.blogId);
});

function updateBlog() {
  const params = {};
  params.title = title.value;
  params.content = content.value;
  params.headImageUrl = bannerImageUrl.value;

  AXIOS.put("/api/v1/blog/edit-basic/" + route.params.blogId, params)
    .then((res) => {
      ElNotification({
        title: "Succeed",
        message: "Succeed updating blogs",
        type: "Succeed",
      });
    })
    .catch((err) => {
      console.error("Error:", err);
      ElNotification({
        title: "Error",
        message: "Failed updating blogs",
        type: "Error",
      });
    });
}
</script>
  
  <style scoped lang="scss">
.wrapper {
  margin-top: 113px;
}

.title {
  margin: 0 auto;
  height: 95px;
  text-indent: 5px;
  color: rgba(51, 51, 51, 1);
  font-size: 12px;
  line-height: 95px;
  text-align: left;
}

.edit {
  margin: 20px auto 0;
  width: 1199px;
  height: 602px;
}

.btns {
  width: 882px;
  margin: 38px auto 0;
}

.post {
  cursor: pointer;
  margin-top: 20px;
  width: 321px;
  height: 58px;
  color: rgba(250, 250, 250, 1);
  background-color: rgba(42, 130, 228, 1);
  border-radius: 8px;
  font-size: 32px;
  line-height: 58px;
  text-align: center;
}

.draft {
  cursor: pointer;
  margin-top: 20px;
  width: 321px;
  height: 58px;
  color: rgba(250, 250, 250, 1);
  background-color: rgba(42, 130, 228, 1);
  border-radius: 8px;
  font-size: 32px;
  line-height: 58px;
  text-align: center;
}
</style>