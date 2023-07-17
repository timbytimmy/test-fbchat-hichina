<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-10">
        <div class="col-12 q-mt-xl">
          <q-input rounded outlined v-model="title" label="Blog title" />
        </div>
        <div class="col-12 q-mt-xl">
          <QuillEditor
            @paste="handlePaste"
            theme="snow"
            v-model:content="content"
            contentType="html"
            toolbar="full"
            ref="quillEditor"
            :modules="combineModule"
          />
        </div>
        <div class="col-12 q-mt-xl row q-mb-xl">
          <div class="col-6 row justify-left">
            <q-btn
              @click="updateBlog()"
              color="grey-4"
              text-color="primary"
              glossy
              unelevated
              icon="update"
              label="Update"
            />
          </div>
          <div class="col-6 row justify-left">
            <q-btn
              @click="goPage('/blog-detail/' + currentBlogId)"
              color="grey-4"
              text-color="primary"
              glossy
              unelevated
              icon="visibility"
              label="View Blog"
            />
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance } from "vue";
import { useQuasar } from "quasar";
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import ImageUploader from "quill-image-uploader";
import { api } from "boot/axios";
import { useRoute } from "vue-router";
export default {
  name: "BlogEditPage",
  components: {
    QuillEditor,
  },
  setup() {
    const instance = getCurrentInstance();
    const app = getCurrentInstance().appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const route = useRoute();

    const title = ref("");
    const content = ref("");
    const savedScrollPosition = ref(null);

    const currentBlogId = ref("");

    currentBlogId.value = route.params.blogId;

    function handlePaste() {}

    const imageUploadModule = {
      name: "imageUploader",
      module: ImageUploader,
      options: {
        upload: (file) => {
          return new Promise((resolve, reject) => {
            const formData = new FormData();
            formData.append("imageFile", file);
            formData.append("expectedType", "blogImage");

            gp.$showLoading($q);
            api
              .post("/api/v1/image/upload", formData)
              .then((res) => {
                resolve(res.data.data);
                gp.$hideLoading($q);
              })
              .catch((err) => {
                gp.$hideLoading($q);
                reject("Upload failed");
                console.error("Error:", err);
              });
          });
        },
      },
    };

    const combineModule = [imageUploadModule];

    function whoami() {
      api
        .get("/api/v1/user/whoami")
        .then(function (response) {
          console.log("current user in setup: " + response.data);
        })
        .catch(function (error) {
          console.log("currently not logged in setup: " + error);
          gp.$goPage("/auth/login");
        });
    }

    function loadBlogDetail(blogId) {
      api
        .get("/api/v1/blog/" + blogId)
        .then(function (response) {
          console.log("blog detail:");
          console.log(response.data);
          title.value = response.data.data.title;
          content.value = response.data.data.content;
        })
        .catch(function (error) {
          console.log("currently not logged in blog edit: " + error);
        });
    }
    function updateBlog() {
      const params = {};
      params.title = title.value;
      params.content = content.value;
      params.headImageUrl = "";

      gp.$showLoading($q);
      api
        .put("/api/v1/blog/edit-basic/" + route.params.blogId, params)
        .then((res) => {
          gp.$hideLoading($q);
          gp.$generalNotify($q, true, "Succeed updating blogs");
        })
        .catch((err) => {
          console.error("Error:", err);
          $gp.$hideLoading($q);
          gp.$generalNotify($q, false, "Error:", err);
          if (err.response.status == 401) {
            gp.$goPage("/auth/login");
          }
        });
    }
    onMounted(() => {
      whoami();

      loadBlogDetail(route.params.blogId);
    });

    return {
      title,
      content,
      updateBlog,
      handlePaste,
      currentBlogId,
      combineModule,
    };
  },
};
</script>
<style lang="sass" scoped>
.ql-clipboard
  position: fixed !important
  opacity: 0 !important
  left: 50% !important
  top: 50% !important
</style>

