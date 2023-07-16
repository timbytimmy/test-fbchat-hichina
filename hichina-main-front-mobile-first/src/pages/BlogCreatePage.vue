<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-10">
        <div class="col-12 q-mt-xl">
          <q-input rounded outlined v-model="title" label="Blog title" />
        </div>
        <div class="col-12 q-mt-xl">
          <QuillEditor
            theme="snow"
            v-model:content="content"
            contentType="html"
            toolbar="full"
            :modules="combineModule"
          />
        </div>
        <div class="col-12 q-mt-xl row q-mb-xl">
          <div class="col-6 row justify-center">
            <q-btn
              @click="postBlog(false)"
              color="grey-4"
              text-color="primary"
              glossy
              unelevated
              icon="publish"
              label="Publish"
            />
          </div>
          <div class="col-6 row justify-center">
            <q-btn
              @click="postBlog(true)"
              color="grey-4"
              text-color="primary"
              glossy
              unelevated
              icon="drafts"
              label="Draft"
            />
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance } from "vue";
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import ImageUploader from "quill-image-uploader";
import { useQuasar } from "quasar";
import { api } from "boot/axios";
export default {
  name: "BlogCreatePage",
  components: {
    QuillEditor,
  },
  setup() {
    const instance = getCurrentInstance();
    const app = getCurrentInstance().appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const content = ref("");
    const title = ref("");
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

    function postBlog(shouldDraft) {
      if (
        content.value == null ||
        content.value.length < 1 ||
        title.value == null ||
        title.value.length < 1
      ) {
        gp.$generalNotify($q, false, "Title or content cannot be empty");
        return;
      }

      const params = {};
      params.draft = shouldDraft;
      params.title = title.value;
      params.content = content.value;
      params.headImageUrl = "";

      gp.$showLoading($q);

      api
        .post("/api/v1/blog", params)
        .then((res) => {
          console.log(res.data.data.blogId);
          gp.$hideLoading($q);
          gp.$goPage("/blog-detail/" + res.data.data.blogId);
        })
        .catch((err) => {
          gp.$hideLoading($q);
          console.error("Error:", err);
        });
    }

    onMounted(() => {
      whoami();
    });
    return {
      content,
      combineModule,
      postBlog,
      title,
    };
  },
};
</script>
