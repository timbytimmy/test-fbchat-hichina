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
      <p class="post" @click="postBlog(false)">Post</p>
      <p class="draft" @click="postBlog(true)">Save Draft</p>
    </div>
  </div>
</template>
  
  <script setup>
import VLazyImage from "v-lazy-image";
import { AXIOS } from "@/common/http-commons";
import ImageUploader from "quill-image-uploader";
import { useRouter } from "vue-router";
const title = ref("");

const router = useRouter();

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

onMounted(() => {
  whoami();
});

function postBlog(shouldDraft) {
  const params = {};
  params.draft = shouldDraft;
  params.title = title.value;
  params.content = content.value;
  params.headImageUrl = bannerImageUrl.value;

  AXIOS.post("/api/v1/blog", params)
    .then((res) => {
      console.log(res.data.data.blogId);
      router.push("/blog-detail/" + res.data.data.blogId);
    })
    .catch((err) => {
      console.error("Error:", err);
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