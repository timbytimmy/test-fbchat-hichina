<template>
  <q-page padding>
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

    <div v-if="mode === 'view'">
      <div style="text-align: center">
        <h4>
          {{ name
          }}<q-icon
            @click="enterEditMode"
            title="编辑"
            class="hichina-edit-button"
            style="margin-left: 10px; cursor: pointer"
            name="edit"
            size="md"
          />
        </h4>
      </div>
      <div>
        <label style="font-weight: bold">预览图：</label
        ><img style="height: 180px; width: 180px" :src="iconPath" />
      </div>
      <div>
        <label style="font-weight: bold">创建时间:</label>
        {{ createdDate }} &nbsp; &nbsp;&nbsp;
        <label style="font-weight: bold">旅游天数:</label>
        {{ rangeInDays }} &nbsp; &nbsp;&nbsp;
        <label style="font-weight: bold">基础价:</label>{{ basePrice }}
      </div>
      <div><label style="font-weight: bold">线路id:</label> {{ publicId }}</div>
      <h6>正文：</h6>
      <div style="margin-top: 20px" v-html="description"></div>
    </div>
    <div v-if="mode === 'edit'">
      <div class="q-pa-md" style="max-width: 95vw; min-width: 400px">
        <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
          <q-input
            filled
            v-model="name"
            label="线路名称"
            lazy-rules
            :rules="[(val) => (val && val.length > 0) || '不能为空']"
          />

          <q-btn
            push
            color="primary"
            label="变更图标"
            @click="toggleImageUpload()"
          />

          <q-img
            v-if="iconPath != ''"
            :src="iconPath"
            spinner-color="white"
            style="height: 140px; max-width: 150px"
          />

          <my-upload
            field="icon_path"
            @crop-success="cropSuccess"
            @crop-upload-success="cropUploadSuccess"
            @crop-upload-fail="cropUploadFail"
            v-model="showImageUpload"
            :width="200"
            :height="200"
            :url="rest_base + '/api/v1/image/upload'"
            :params="upload_icon_params"
            :headers="headers"
            :withCredentials="true"
            img-format="jpg"
          >
          </my-upload>

          <q-input
            v-model="rangeInDays"
            label="旅行天数"
            type="number"
            filled
            style="max-width: 200px"
          />

          <q-input
            filled
            v-model="basePrice"
            label="起步价"
            type="number"
            input-class="text-right"
            style="max-width: 200px"
          ></q-input>

          <q-editor
            v-model="description"
            @paste="(evt) => pasteCapture(evt)"
            @drop="(evt) => dropCapture(evt)"
            ref="editor_ref2"
            :definitions="definitions"
            :toolbar="[
              ['insert_img','fullscreen'],
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
                  options: ['p', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'code'],
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
          />

          <div>
            <q-btn label="更新" @click="updateLine" color="primary" />
            <q-btn label="取消" color="secondary" @click="mode = 'view'" />
          </div>
        </q-form>
      </div>
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
import myUpload from "vue-image-crop-upload";
import { myMixin } from "boot/commonFunc";
import UploadImage from "v-upload-image";
export default {
  name: "EditAndPreviewPage",
  mixins: [myMixin],
  components: {
    "my-upload": myUpload,
    "upload-image": UploadImage,
  },
  setup() {
    const $q = useQuasar();

    return {
      onRowClick: (row) => alert(`${row.name} clicked`),
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
    };
  },
  data() {
    return {
      imageUploadBox: false,
      headers: {},
      upload_icon_params: {},
      rest_base: "",
      showImageUpload: false,
      iconPath: "",
      mode: "view",
      publicId: this.$route.params.publicid,
      name: "",
      createdDate: "",
      rangeInDays: "",
      basePrice: "",
      definitions: {
        insert_img: {
          tip: "插入图片",
          icon: "photo",
          handler: this.insertImg,
        },
      },
    };
  },
  mounted() {
    this.getRestBase();
    this.loadContent();
  },
  methods: {
    async customProcessImage(image) {
      console.log("process image");

      const formData = new FormData();
      formData.append("expectedType", "blogImage");
      formData.append("icon_path", image);

      api
        .post("/api/v1/image/upload", formData)
        .then((response) => {
          console.log("show the response");
          console.log(response.data);
          this.showNotifyMessageSucceed("成功插入图片");
          this.imageUploadBox = false;
          // this.$refs.editor_ref2.runCmd(
          //   "insertHTML",
          //   '<div><img src="' + response.data.data + '" /></div>'
          // );
          this.$refs.editor_ref2.runCmd("insertImage", response.data.data);

          return { imageId: "somefake", imageUrl: response.data.data };
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    insertImg() {
      this.imageUploadBox = true;
    },
    pasteCapture(evt) {
      // Let inputs do their thing, so we don't break pasting of links.
      if (evt.target.nodeName === "INPUT") return;
      let text, onPasteStripFormattingIEPaste;
      evt.preventDefault();
      if (evt.originalEvent && evt.originalEvent.clipboardData.getData) {
        text = evt.originalEvent.clipboardData.getData("text/plain");
        this.$refs.editor_ref2.runCmd("insertText", text);
      } else if (evt.clipboardData && evt.clipboardData.getData) {
        text = evt.clipboardData.getData("text/plain");
        this.$refs.editor_ref2.runCmd("insertText", text);
      } else if (window.clipboardData && window.clipboardData.getData) {
        if (!onPasteStripFormattingIEPaste) {
          onPasteStripFormattingIEPaste = true;
          this.$refs.editor_ref2.runCmd("ms-pasteTextOnly", text);
        }
        onPasteStripFormattingIEPaste = false;
      }
    },
    dropCapture(evt) {
      console.log(evt);
    },
    updateLine() {
      console.log("update line...");

      var params = {};
      params.name = this.name;
      params.iconPath = this.iconPath;
      params.rangeInDays = this.rangeInDays;
      params.basePrice = this.basePrice;
      params.description = this.description;

      api
        .put("/api/v1/line/" + this.publicId, params)
        .then((response) => {
          console.log(response.data);
          this.mode = "view";
          this.loadContent();
          this.showNotifyMessageSucceed(response.data.message);
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    cropSuccess(imgDataUrl, field) {
      console.log("-------- crop success --------");
      this.imgDataUrl = imgDataUrl;
    },
    cropUploadSuccess(jsonData, field) {
      console.log("-------- upload success --------");
      //console.log(jsonData.data);
      console.log("field: " + field);
      this.iconPath = jsonData.data;
      this.showNotifyMessageSucceed("上传成功");
      this.showImageUpload = false;
    },
    cropUploadFail(status, field) {
      console.log("-------- upload fail --------");
      console.log(status);
      console.log("field: " + field);
      this.showNotifyMessage("上传失败");
    },
    toggleImageUpload() {
      this.showImageUpload = !this.showImageUpload;
    },
    enterEditMode() {
      console.log("entering edit mode...");
      this.mode = "edit";
    },
    loadContent() {
      api
        .get("/api/v1/line/" + this.$route.params.publicid)
        .then((response) => {
          console.log("got detail of line: ");
          console.log(response.data.data);
          this.name = response.data.data.name;
          this.createdDate = response.data.data.created_date;
          this.rangeInDays = response.data.data.range_in_days;
          this.basePrice = response.data.data.base_price;
          this.description = response.data.data.description;
          this.iconPath = response.data.data.icon_path;
        })
        .catch((e) => {});
    },
  },
};
</script>
