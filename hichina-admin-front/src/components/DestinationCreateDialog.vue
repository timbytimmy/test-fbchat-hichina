<template>
  <div>
    <q-dialog v-model="imageUploadBox">
      <q-card>
        <q-card-section style="width: 300px" class="row items-center q-pb-none">
          <div class="text-h6">图片上传</div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-card-section>
          <upload-image
            :processImage="customProcessImage"
            :removeImage="customRemoveImage"
            @update-urls="customHandleUpdatedUrls"
          ></upload-image>
        </q-card-section>
      </q-card>
    </q-dialog>
    <q-dialog
      v-model="dialogOpen"
      :maximized="maximizedToggle"
      transition-show="slide-up"
      transition-hide="slide-down"
    >
      <q-card class="bg-cyan-1 text-black">
        <q-bar>
          <q-space />

          <q-btn
            dense
            flat
            icon="minimize"
            @click="maximizedToggle = false"
            :disable="!maximizedToggle"
          >
            <q-tooltip v-if="maximizedToggle" class="bg-white text-primary"
              >Minimize</q-tooltip
            >
          </q-btn>
          <q-btn
            dense
            flat
            icon="crop_square"
            @click="maximizedToggle = true"
            :disable="maximizedToggle"
          >
            <q-tooltip v-if="!maximizedToggle" class="bg-white text-primary"
              >Maximize</q-tooltip
            >
          </q-btn>
          <q-btn dense flat icon="close" v-close-popup>
            <q-tooltip class="bg-white text-primary">Close</q-tooltip>
          </q-btn>
        </q-bar>

        <q-card-section>
          <div class="text-h6">创建目的地</div>
        </q-card-section>

        <q-card-section>
          <div style="width: 100vw" class="row justify-center">
            <div class="col-8">
              <q-input
                outlined
                class="col-8"
                label="头图URL"
                v-model="destinationProfileImage"
              />
            </div>
            <div class="col-8">
              <img
                v-if="destinationProfileImage != ''"
                style="height: 240px; width: 290px"
                :src="destinationProfileImage"
              />
            </div>

            <div class="col-8">
              <q-select
                @update:model-value="(val) => levelChange(val)"
                outlined
                v-model="level"
                :options="levels"
                label="层级"
              />
            </div>
            <div class="col-8" v-if="parentCandidateOptions.length > 0">
              <q-select
                @update:model-value="(val) => parentLevelDestinationChange(val)"
                outlined
                v-model="parentLevelDestination"
                :options="parentCandidateOptions"
                label="父目的地"
              />
            </div>
            <div class="col-8">
              <q-input
                outlined
                label="目的地名称"
                v-model="destinationName"
              ></q-input>
            </div>
            <div v-if="insertVideoLinkBox" class="col-8">
              <q-input
                class="col-7"
                filled
                bottom-slots
                v-model="videolink"
                label="视频链接"
                counter
                maxlength="120"
                :dense="dense"
              >
                <template v-slot:append>
                  <q-btn
                    @click="doInsertVideo"
                    color="primary"
                    icon="add_link"
                  />
                </template>
              </q-input>
            </div>
            <div class="col-8">
              <QuillEditor
                theme="snow"
                v-model:content="description"
                contentType="html"
                toolbar="full"
                :modules="combineModule"
              />
              <!-- <q-editor
                v-model="description"
                ref="destinationDialogEditor"
                :definitions="definitions"
                @paste="pasteCapture"
                @drop="(evt) => dropCapture(evt)"
                :toolbar="[
                  ['insert_img', 'insert_video'],
                  [
                    {
                      label: $q.lang.editor.align,
                      icon: $q.iconSet.editor.align,
                      fixedLabel: true,
                      options: ['left', 'center', 'right', 'justify'],
                    },
                  ],
                  [
                    'bold',
                    'italic',
                    'strike',
                    'underline',
                    'subscript',
                    'superscript',
                  ],
                  ['token', 'hr', 'link', 'custom_btn'],
                  [
                    {
                      label: $q.lang.editor.formatting,
                      icon: $q.iconSet.editor.formatting,
                      list: 'no-icons',
                      options: [
                        'p',
                        'h1',
                        'h2',
                        'h3',
                        'h4',
                        'h5',
                        'h6',
                        'code',
                      ],
                    },
                    {
                      label: $q.lang.editor.fontSize,
                      icon: $q.iconSet.editor.fontSize,
                      fixedLabel: true,
                      fixedIcon: true,
                      list: 'no-icons',
                      options: [
                        'size-1',
                        'size-2',
                        'size-3',
                        'size-4',
                        'size-5',
                        'size-6',
                        'size-7',
                      ],
                    },
                    {
                      label: $q.lang.editor.defaultFont,
                      icon: $q.iconSet.editor.font,
                      fixedIcon: true,
                      list: 'no-icons',
                      options: [
                        'default_font',
                        'arial',
                        'arial_black',
                        'comic_sans',
                        'courier_new',
                        'impact',
                        'lucida_grande',
                        'times_new_roman',
                        'verdana',
                      ],
                    },
                    'removeFormat',
                  ],
                  ['quote', 'unordered', 'ordered', 'outdent', 'indent'],

                  ['undo', 'redo'],
                  ['viewsource'],
                ]"
                :fonts="{
                  arial: 'Arial',
                  arial_black: 'Arial Black',
                  comic_sans: 'Comic Sans MS',
                  courier_new: 'Courier New',
                  impact: 'Impact',
                  lucida_grande: 'Lucida Grande',
                  times_new_roman: 'Times New Roman',
                  verdana: 'Verdana',
                }"
              /> -->
            </div>
            <div class="col-8" style="margin-top: 80px">
              <q-btn
                label="提交"
                @click="submitNewDestination"
                color="primary"
              />
            </div>
          </div>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
import UploadImage from "v-upload-image";
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import ImageUploader from "quill-image-uploader";
export default {
  name: "DestinationCreateDialog",
  components: {
    "upload-image": UploadImage,
    QuillEditor: QuillEditor,
  },
  setup() {
    const imageUploadModule = {
      name: "imageUploader",
      module: ImageUploader,
      options: {
        upload: (file) => {
          return new Promise((resolve, reject) => {
            const formData = new FormData();
            formData.append("imageFile", file);
            formData.append("expectedType", "blogImage");

            api
              .post("/api/v1/image/upload", formData)
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
    const $q = useQuasar();

    return {
      combineModule,
      showNotifyMessageFail(msg) {
        $q.notify({
          message: msg,
          color: "red",
          position: "top-right",
        });
      },
      showNotifyMessageSucceed(msg) {
        $q.notify({
          message: msg,
          color: "green",
          position: "top-right",
        });
      },
      showFullPageLoading() {
        this.disableAction = true;
        $q.loading.show();
      },
      hideFullPageLoading() {
        $q.loading.hide();
        this.disableAction = false;
      },
    };
  },
  data() {
    return {
      destinationProfileImage: "",
      videolink: "",
      insertVideoLinkBox: false,
      imageUploadBox: false,
      definitions: {
        insert_img: {
          tip: "插入图片",
          icon: "photo",
          handler: this.insertImg,
        },
        insert_video: {
          tip: "插入油管视频",
          icon: "play_circle_filled",
          handler: this.insertVid,
        },
      },
      mode: "new",
      description: "",
      level: "",
      parentLevelDestination: "",
      levels: [
        {
          label: "省",
          value: 5,
        },
        {
          label: "市",
          value: 6,
        },
        {
          label: "景点",
          value: 7,
        },
      ],
      destinationName: "",
      destinationId: "",
      maximizedToggle: true,
      dialogOpen: false,
      parentCandidates: [],
      parentCandidateOptions: [],
    };
  },
  methods: {
    changeToEmbeded(originLink) {
      if (originLink.indexOf("&") > 0) {
        originLink = originLink.substring(0, originLink.indexOf("&"));
      }
      return originLink.replace("watch?v=", "embed/");
    },
    doInsertVideo() {
      if (this.videolink == null || this.videolink.length < 1) {
        this.showNotifyMessageFail("空链接");
        return;
      }
      this.videolink = this.changeToEmbeded(this.videolink);
      this.$refs.destinationDialogEditor.runCmd(
        "insertHTML",
        `<iframe
        src="` +
          this.videolink +
          `"
          width="640"
          height="360"
        frameborder="0"
        allowfullscreen
      />`
      );
      this.insertVideoLinkBox = false;
    },
    async customProcessImage(image) {
      const formData = new FormData();
      formData.append("expectedType", "blogImage");
      formData.append("imageFile", image);
      api
        .post("/api/v1/image/upload", formData)
        .then((response) => {
          this.showNotifyMessageSucceed("成功插入图片");
          this.imageUploadBox = false;
          this.$refs.destinationDialogEditor.runCmd(
            "insertImage",
            response.data.data
          );
          return { imageId: "somefake", imageUrl: response.data.data };
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    insertImg() {
      this.imageUploadBox = true;
    },
    insertVid() {
      this.insertVideoLinkBox = true;
      this.videolink = "";
    },
    loadAndSetForm(destinationId) {
      var storeThis = this;
      api
        .get("/api/v1/destination/" + destinationId)
        .then((response) => {
          console.log("get destination by id: ");
          console.log(response.data);

          var level = this.levels.filter((levelObj) => {
            return levelObj.value == response.data.level;
          });
          this.level = level[0];

          if (this.level.value > 5) {
            this.loadDestinationsByLevel(
              this.level.value - 1,
              response.data.parentId
            );
          }

          this.destinationName = response.data.destinationName;
          this.destinationProfileImage = response.data.destinationProfileImage;
          this.description = response.data.description;
          this.destinationId = destinationId;
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    parentLevelDestinationChange(val) {
      console.log("设置parentId");
      //console.log(val);
      console.log(this.parentLevelDestination);
    },
    clearForm() {
      this.destinationProfileImage = "";
      this.parentCandidateOptions = [];
      this.destinationName = "";
      this.level = "";
      this.parentLevelDestination = "";
      this.description = "";
    },
    submitEditDestination() {
      if (!this.validateForm()) {
        return;
      }
      var params = {};
      params.name = this.destinationName;
      params.description = this.description;
      params.level = this.level.value;
      params.destinationProfileImage = this.destinationProfileImage;
      if (this.parentLevelDestination != "") {
        params.parentId = this.parentLevelDestination.value;
      }
      var storeThis = this;
      api
        .put("/api/v1/destination/" + this.destinationId, params)
        .then((response) => {
          console.log(response.data);
          if (response.data.ok === true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.dialogOpen = false;
            this.$emit("close-me-event");
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
    validateForm() {
      if (this.destinationName == null || this.destinationName.length < 1) {
        this.showNotifyMessageFail("名称不能为空");
        return false;
      }
      if (this.level == null || this.level.length < 1) {
        this.showNotifyMessageFail("层级不能为空");
        return false;
      }
      if (
        this.parentCandidateOptions.length > 0 &&
        this.parentLevelDestination == ""
      ) {
        this.showNotifyMessageFail("父目的地不能为空");
        return false;
      }
      return true;
    },
    submitNewDestination() {
      if (this.mode == "edit") {
        this.submitEditDestination();
        return;
      }
      if (!this.validateForm()) {
        return;
      }
      var params = {};
      params.name = this.destinationName;
      params.description = this.description;
      params.level = this.level.value;
      params.destinationProfileImage = this.destinationProfileImage;
      if (this.parentLevelDestination != "") {
        params.parentId = this.parentLevelDestination.value;
      }
      var storeThis = this;
      api
        .post("/api/v1/destination", params)
        .then((response) => {
          console.log(response.data);
          if (response.data.ok === true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.dialogOpen = false;
            this.$emit("close-me-event");
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
    loadDestinationsByLevel(level, parentId) {
      var params = {};
      params.page = -1;
      params.pageSize = 100;
      params.query = "";
      params.level = level;
      api
        .get("/api/v1/destination", {
          params: params,
        })
        .then((response) => {
          this.parentCandidates = response.data.data.data;
          console.log("parent destination candidates:");
          console.log(this.parentCandidates);
          this.parentCandidateOptions = [];
          for (var i in this.parentCandidates) {
            var obj = {};
            obj.label = this.parentCandidates[i].destinationName;
            obj.value = this.parentCandidates[i].destinationId;
            this.parentCandidateOptions.push(obj);
          }
          if (parentId != null) {
            var parentObjs = this.parentCandidateOptions.filter((levelObj) => {
              return levelObj.value == parentId;
            });
            this.parentLevelDestination = parentObjs[0];
          }
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    levelChange(val) {
      if (val.value > 5) {
        //省以下的才需要加载候选父目的地，不得跨级选择父目的地
        this.loadDestinationsByLevel(val.value - 1);
      } else {
        this.parentLevelDestination = "";
        this.parentCandidateOptions = [];
      }
    },
    toggleDialog() {
      this.mode = "new";
      this.clearForm();
      this.dialogOpen = !this.dialogOpen;
    },
    toggleEditMode(destinationId) {
      this.mode = "edit";
      this.clearForm();
      this.dialogOpen = !this.dialogOpen;
      this.loadAndSetForm(destinationId);
    },
  },
};
</script>
