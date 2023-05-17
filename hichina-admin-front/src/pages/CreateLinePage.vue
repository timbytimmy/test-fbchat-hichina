<template>
  <q-page padding>
    <!-- content -->
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
          label="上传图标"
          @click="toggleImageUpload()"
        />

        <q-img
          v-if="icon_image_path != ''"
          :src="icon_image_path"
          spinner-color="white"
          style="height: 140px; max-width: 150px"
        />

        <q-dialog v-model="imageUploadBox">
          <q-card>
            <q-card-section
              style="width: 300px"
              class="row items-center q-pb-none"
            >
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
          v-model="range_in_days"
          label="旅行天数"
          type="number"
          filled
          style="max-width: 200px"
        />

        <q-input
          filled
          v-model="base_price"
          label="起步价"
          type="number"
          input-class="text-right"
          style="max-width: 200px"
        ></q-input>

        <q-editor
          v-model="description"
          ref="editor_ref2"
          :definitions="definitions"
          @paste="(evt) => pasteCapture(evt)"
          @drop="(evt) => dropCapture(evt)"
          :toolbar="[
            ['insert_img', 'fullscreen'],
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
          <q-btn label="提交" @click="submitLine" color="primary" />
          <q-btn
            label="重置"
            type="reset"
            color="primary"
            flat
            class="q-ml-sm"
          />
        </div>
      </q-form>
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import myUpload from "vue-image-crop-upload";
import { useQuasar } from "quasar";
import { myMixin } from "boot/commonFunc";
import UploadImage from "v-upload-image";
export default {
  name: "CreateLinePage",
  mixins: [myMixin],
  components: {
    "my-upload": myUpload,
    "upload-image": UploadImage,
  },
  setup() {
    const $q = useQuasar();

    return {
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
      imageUploadBox: false,
      showImageUpload: false,
      icon_image_path: "",
      name: "",
      rest_base: "",
      icon_path: "",
      range_in_days: 1,
      base_price: 0.0,
      upload_icon_params: {},
      headers: {},
      description: "",
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
    submitLine() {
      var params = {};
      params.name = this.name;
      params.iconPath = this.icon_image_path;
      params.rangeInDays = this.range_in_days;
      params.basePrice = this.base_price;
      params.description = this.description;

      console.log("submmiting params: ");
      console.log(params);

      if (
        this.name == null ||
        this.name.length < 1 ||
        this.icon_image_path == null ||
        this.icon_image_path.length < 1 ||
        this.base_price <= 0 ||
        this.range_in_days < 1 ||
        this.description == null ||
        this.description.length < 1
      ) {
        this.showNotifyMessageFail("提交信息不完整,请仔细检查");
        return;
      }

      api
        .post("/api/v1/line", params)
        .then((response) => {
          //console.log(response.data.message)
          this.showNotifyMessageSucceed(response.data.message);
          this.goPage("/list-line");
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    toggleImageUpload() {
      this.showImageUpload = !this.showImageUpload;
      this.upload_icon_params.expectedType = "thumbnail";
    },
    cropSuccess(imgDataUrl, field) {
      console.log("-------- crop success --------");
      this.imgDataUrl = imgDataUrl;
    },
    cropUploadSuccess(jsonData, field) {
      console.log("field: " + field);
      this.icon_image_path = jsonData.data;
      this.showNotifyMessageSucceed("上传成功");
      this.showImageUpload = false;
    },
    cropUploadFail(status, field) {
      console.log("-------- upload fail --------");
      console.log(status);
      console.log("field: " + field);
      this.showNotifyMessageFail("上传失败");
    },
  },
};
</script>
