<template>
  <div>
    <my-upload
      field="imageFile"
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
      v-model="editskudialogopen"
      :maximized="true"
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
          <div v-if="mode === 'edit'" class="text-h6">编辑产品sku</div>
          <div
            v-if="mode === 'create' || mode === 'copycreate'"
            class="text-h6"
          >
            创建产品sku
          </div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div style="width: 100%">
            <div
              v-if="mode === 'edit' || mode === 'copycreate'"
              class="row justify-center"
            >
              <p>产品类别:{{ edit_sku_dialog_product_type_name }}</p>
            </div>
            <div v-if="mode === 'create'" class="row justify-center">
              <q-select
                @update:model-value="(val) => productTypeChange(val)"
                outlined
                class="col-8"
                style="min-width: 200px"
                v-model="newSkuDialogProductType"
                :options="productTypeOptions"
                label="产品类别过滤"
              />
            </div>
            <div class="row justify-center">
              <q-input
                class="col-8"
                filled
                v-model="editSkuDialogProductName"
                label="产品名"
                lazy-rules
                :rules="[(val) => (val && val.length > 0) || '不能为空']"
              />
            </div>
            <div v-if="insertVideoLinkBox" class="row">
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
            <div row justify-center>
              <QuillEditor
                theme="snow"
                v-model:content="editSkuDialogDescription"
                contentType="html"
                toolbar="full"
                :modules="combineModule"
              />
              <!-- <q-editor
                v-model="editSkuDialogDescription"
                ref="editDialogEditor"
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
          </div>
        </q-card-section>
        <q-separator inset class="col" />
        <div class="row justify-center">
          <div class="col-1">自定义属性</div>
        </div>
        <q-separator inset class="col" />
        <q-card-section v-if="mode === 'edit'">
          <div v-for="item in rawPropertyBags" v-bind:key="item.attributeId">
            <div class="row justify-left" v-if="item.dataType === 'string'">
              <q-input
                class="col-8"
                outlined
                type="textarea"
                v-model="item.attributeValue"
                :label="item.attributeName + '(最长200字符)'"
                :rules="[
                  (val) =>
                    val.length <= 200 || 'Please use maximum 200 characters',
                ]"
              />
            </div>
            <div class="row justify-left" v-if="item.dataType === 'integer'">
              <q-input
                class="col-3"
                outlined
                v-model.number="item.attributeValue"
                type="number"
                min="0"
                :label="item.attributeName"
              />
            </div>
            <div v-if="item.dataType === 'date'">
              <label>{{ item.attributeName }}</label>
              <br />
              <q-date v-model="item.attributeValue" minimal />
            </div>
            <div v-if="item.dataType === 'datestring'">
              <label style="word-wrap: break-word"
                >{{ item.attributeName }}, Selected dates:
                {{ dateLabel[item.attributeId] }}</label
              >
              <br />
              <hichina-date-picker
                multi-dates
                inline
                multi-calendars
                :min-date="new Date()"
                :month-change-on-scroll="false"
                @update:model-value="handleDate(item.attributeId)"
                :format="format"
                v-model="item.attributeValue"
              />
            </div>
            <div v-if="item.dataType === 'image'" class="row justify-left">
              <!-- <q-input
                outlined
                class="col-8"
                label="头图URL"
                v-model="item.attributeValue"
              ></q-input> -->
              <div>
                <q-btn
                  label="Profile Image"
                  color="primary"
                  @click="insertImg(item, 'attributeValue')"
                />
              </div>

              <div class="col-8">
                <q-img
                  v-if="
                    item.attributeValue != '' && item.attributeValue != null
                  "
                  :src="item.attributeValue"
                  spinner-color="white"
                  style="height: 340px; max-width: 750px"
                />
              </div>
            </div>
          </div>
        </q-card-section>
        <q-card-section v-if="mode === 'create' || mode === 'copycreate'">
          <div v-for="item in rawPropertyBags" v-bind:key="item.attributeId">
            <div class="row justify-left" v-if="item.dataType === 'string'">
              <q-input
                class="col-8"
                outlined
                type="textarea"
                v-model="
                  new_sku_dialog_customPropertyBag[
                    '[string]' + item.attributeId
                  ]
                "
                :label="item.attributeName + '(最长200字符)'"
                :rules="[
                  (val) =>
                    val.length <= 200 || 'Please use maximum 200 characters',
                ]"
              />
            </div>
            <div class="row justify-left" v-if="item.dataType === 'integer'">
              <q-input
                class="col-3"
                outlined
                v-model.number="
                  new_sku_dialog_customPropertyBag[
                    '[integer]' + item.attributeId
                  ]
                "
                type="number"
                min="0"
                :label="item.attributeName"
              />
            </div>
            <div v-if="item.dataType === 'date'">
              <label>{{ item.attributeName }}</label>
              <br />
              <q-date
                minimal
                v-model="
                  new_sku_dialog_customPropertyBag['[date]' + item.attributeId]
                "
              />
            </div>
            <div v-if="item.dataType === 'datestring'">
              <label style="word-wrap: break-word"
                >{{ item.attributeName }} , Selected dates:
                {{ dateLabel[item.attributeId] }}</label
              >
              <br />
              <hichina-date-picker
                inline
                multi-dates
                multi-calendars
                :min-date="new Date()"
                :month-change-on-scroll="false"
                @update:model-value="handleDate(item.attributeId)"
                :format="format"
                v-model="
                  new_sku_dialog_customPropertyBag[
                    '[datestring]' + item.attributeId
                  ]
                "
              />
            </div>
            <div v-if="item.dataType === 'image'" class="row justify-left">
              <!-- <q-input
                outlined
                class="col-8"
                v-model="
                  new_sku_dialog_customPropertyBag['[image]' + item.attributeId]
                "
                label="头图URL"
              ></q-input> -->
              <div>
                <q-btn
                  label="Profile Image"
                  color="primary"
                  @click="
                    insertImg(
                      new_sku_dialog_customPropertyBag,
                      '[image]' + item.attributeId
                    )
                  "
                />
              </div>

              <div class="col-8">
                <q-img
                  v-if="
                    new_sku_dialog_customPropertyBag[
                      '[image]' + item.attributeId
                    ] != '' &&
                    new_sku_dialog_customPropertyBag[
                      '[image]' + item.attributeId
                    ] != null
                  "
                  :src="
                    new_sku_dialog_customPropertyBag[
                      '[image]' + item.attributeId
                    ]
                  "
                  spinner-color="white"
                  style="height: 340px; max-width: 750px"
                />
              </div>
            </div>
          </div>
        </q-card-section>
        <q-card-section>
          <q-btn
            v-if="mode === 'edit'"
            color="primary"
            label="更新"
            @click="updateWithPropertyBag"
          />
          <q-btn
            v-if="mode === 'create'"
            color="primary"
            label="创建"
            @click="submitWithPropertyBag(false)"
          />
          <q-btn
            v-if="mode === 'copycreate'"
            color="primary"
            label="创建"
            @click="submitWithPropertyBag(true)"
          />
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
import UploadImage from "v-upload-image";
import myUpload from "vue-image-crop-upload";
import { myMixin } from "boot/commonFunc";
import VueDatePicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import ImageUploader from "quill-image-uploader";

export default {
  props: ["editProductName"],
  name: "ProductEditorDialog",
  mixins: [myMixin],
  components: {
    "upload-image": UploadImage,
    "my-upload": myUpload,
    "hichina-date-picker": VueDatePicker,
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
      imageObj2Set: {},
      imageObjKey2Set: "",
      tempDateString: "",
      dateLabel: {},
      combinedDateStr: "",
      new_sku_dialog_customPropertyBag: {},
      productTypeOptions: [],
      newSkuDialogProductType: "",
      mode: "edit",
      upload_icon_params: {},
      showImageUpload: false,
      currentProcessingImageKey: "",
      maximizedToggle: false,
      videolink: "",
      insertVideoLinkBox: false,
      imageUploadBox: false,
      currentSkuId: "",
      editskudialogopen: false,
      edit_sku_dialog_customPropertyBag: {},
      editSkuDialogDescription: "",
      rawPropertyBags: [],
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
      editSkuDialogProductName: "",
      edit_sku_dialog_product_type_name: "",
      editSkuDialogProductTypeId: "",
    };
  },
  mounted() {
    this.getRestBase();
    this.loadAllProductTypes();
  },
  methods: {
    format(dateArray) {
      this.tempDateString = this.dateArray2String(dateArray);
      return this.tempDateString;
    },
    dateArray2String(dateArray) {
      var ret = "";
      for (var i in dateArray) {
        var date = dateArray[i];
        var day = date.getDate();
        var month = date.getMonth() + 1;
        var year = date.getFullYear();

        var combined = `${year}/${month}/${day};`;
        ret = ret + combined;
      }
      this.combinedDateStr = ret;
      return ret;
    },
    extractValueFromPropertyBag(propertyBag, attrId) {
      var valArray = propertyBag.filter((obj) => {
        return obj.attributeId == attrId;
      });
      if (valArray.length > 0) {
        return valArray[0].attributeValue;
      }
      return null;
    },
    handleDate(val1) {
      this.dateLabel[val1] = this.tempDateString;

      // console.log(this.rawPropertyBags);
      // this.dateLabel[attributeId] = this.extractValueFromPropertyBag(
      //   this.rawPropertyBags,
      //   attributeId
      // );
      // console.log(this.dateLabel[attributeId]);
      // console.log(this.rawPropertyBags);
      // this.dateLabel[attrId] = attrValue;
    },
    loadAllProductTypes() {
      var params = {};
      // -1 indicates don't want paging at all
      params.page = -1;
      params.pageSize = -1;
      params.query = "";
      var storeThis = this;

      api
        .get("/api/v1/producttype", {
          params: params,
        })
        .then((response) => {
          var allProductTypes = response.data.data.data;
          this.productTypeOptions = [];
          for (var i in allProductTypes) {
            var obj = {};
            obj.value = allProductTypes[i].product_type_id;
            obj.label = allProductTypes[i].product_type_name;
            this.productTypeOptions.push(obj);
          }
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    loadBindedAttributes(selectedProductId) {
      var params = {};
      params.productTypeId = selectedProductId;
      var storeThis = this;
      api
        .get("/api/v1/producttype/bindedattributesforproduct", {
          params: params,
        })
        .then((response) => {
          this.rawPropertyBags = response.data.data;
          console.log("rawPropertyBags:");
          console.log(this.rawPropertyBags);
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    productTypeChange(val) {
      var productTypeId = val.value;
      this.loadBindedAttributes(productTypeId);
    },
    clearForm() {
      console.log("clear form");
      this.dateLabel = {};
      this.newSkuDialogProductType = "";
      this.editSkuDialogProductName = "";
      this.editSkuDialogDescription = "";
      this.new_sku_dialog_customPropertyBag = {};
    },
    formatDateStringInUpdate(rawPropertyBag) {
      for (var index in rawPropertyBag) {
        var item = rawPropertyBag[index];
        if (item.dataType === "datestring") {
          item.attributeValue = this.dateArray2String(item.attributeValue);
        }
      }
      return rawPropertyBag;
    },
    formatDateStringInCreate(propertyBag) {
      for (var prop in propertyBag) {
        if (prop.startsWith("[datestring]")) {
          // assume it's a date array input
          var formatedString = this.format(propertyBag[prop]);
          propertyBag[prop] = formatedString;
        }
      }
    },
    submitWithPropertyBag(isCopyCreate) {
      if (!isCopyCreate) {
        if (this.newSkuDialogProductType == "") {
          this.showNotifyMessageFail("未选定产品类型");
          return;
        }
      }

      if (
        this.editSkuDialogProductName == null ||
        this.editSkuDialogProductName.length < 1
      ) {
        this.showNotifyMessageFail("产品名不得为空");
        return;
      }

      // submit the product to the backend
      var params = {};
      params.productName = this.editSkuDialogProductName;
      params.productDescription = this.editSkuDialogDescription;
      if (!isCopyCreate) {
        params.productTypeId = this.newSkuDialogProductType.value;
      } else {
        params.productTypeId = this.editSkuDialogProductTypeId;
      }
      this.formatDateStringInCreate(this.new_sku_dialog_customPropertyBag);
      params.customPropertyBag = this.new_sku_dialog_customPropertyBag;
      api
        .post("/api/v1/productsku", params)
        .then((response) => {
          if (response.data.ok == true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.editskudialogopen = false;
            this.$emit("close-me-event");
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    cropUploadFail(status, field) {
      console.log("-------- upload fail --------");
      console.log(status);
      console.log("field: " + field);
      this.showNotifyMessageFail("上传失败");
    },
    cropUploadSuccess(jsonData, field) {
      console.log("field: " + field);
      // todo: set attributeId with currentProcessingImageKey the item in rawPropertyBag with attribute value jsonData.data
      for (var i in this.rawPropertyBags) {
        if (
          this.rawPropertyBags[i].attributeId === this.currentProcessingImageKey
        ) {
          this.rawPropertyBags[i].attributeValue = jsonData.data;
        }
      }
      this.showNotifyMessageSucceed("上传成功");
      this.showImageUpload = false;
    },
    cropSuccess(imgDataUrl, field) {
      console.log("-------- crop success --------");
      this.imgDataUrl = imgDataUrl;
    },
    // toggleImageUpload(val) {
    //   this.currentProcessingImageKey = val;
    //   this.showImageUpload = !this.showImageUpload;
    //   this.upload_icon_params.expectedType = "thumbnail";
    // },
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
      this.$refs.editDialogEditor.runCmd(
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
          console.log("show the response");
          console.log(response.data);
          this.showNotifyMessageSucceed("成功插入图片");
          this.imageUploadBox = false;

          // this.$refs.editDialogEditor.runCmd("insertImage", response.data.data);
          this.imageObj2Set[this.imageObjKey2Set] = response.data.data;
          return { imageId: "somefake", imageUrl: response.data.data };
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    recoverDateString2Object(propertyBag) {
      for (var prop in propertyBag) {
        var val = propertyBag[prop];
        if (val.dataType === "datestring") {
          var dateObjArray = [];
          var datestringArray =
            val.attributeValue === null ? "" : val.attributeValue.split(";");
          for (var i in datestringArray) {
            var datestr = datestringArray[i];
            if (datestr != null && datestr.length > 0) {
              // turn in to object
              var dateObj = new Date(datestr);
              dateObjArray.push(dateObj);
            }
          }
          propertyBag[prop].attributeValue = dateObjArray;
        }
      }
      return propertyBag;
    },
    loadProductSkuId(sku_id) {
      console.log("loading sku id: " + sku_id);
      var params = {};
      params.skuId = sku_id;
      var storeThis = this;
      api
        .get("/api/v1/productsku/withpropertybag", {
          params: params,
        })
        .then((response) => {
          console.log("loaded sku with all properties:");
          console.log(response.data.data);
          this.editSkuDialogProductName =
            response.data.data.hichinaProductDTO.product_name;
          this.editSkuDialogDescription =
            response.data.data.hichinaProductDTO.product_content;
          this.edit_sku_dialog_product_type_name =
            response.data.data.hichinaProductDTO.product_type_name;
          this.editSkuDialogProductTypeId =
            response.data.data.hichinaProductDTO.product_type_id;
          if (this.mode === "copycreate") {
            this.loadBindedAttributes(this.editSkuDialogProductTypeId);
          }
          this.rawPropertyBags = response.data.data.productPropertyBag;
          this.rawPropertyBags = this.recoverDateString2Object(
            this.rawPropertyBags
          );
          console.log(
            "this is raw property bags(recovered datestring to date object) got:"
          );
          console.log(this.rawPropertyBags);
          this.editskudialogopen = true;

          if (this.mode === "copycreate") {
            // convert property bag
            for (var index in this.rawPropertyBags) {
              var item = this.rawPropertyBags[index];
              if (item.dataType === "string") {
                this.new_sku_dialog_customPropertyBag[
                  "[string]" + item.attributeId
                ] = this.rawPropertyBags[index].attributeValue;
              } else if (item.dataType === "integer") {
                this.new_sku_dialog_customPropertyBag[
                  "[integer]" + item.attributeId
                ] = this.rawPropertyBags[index].attributeValue;
              } else if (item.dataType === "date") {
                this.new_sku_dialog_customPropertyBag[
                  "[date]" + item.attributeId
                ] = this.rawPropertyBags[index].attributeValue;
              } else if (item.dataType === "datestring") {
                this.new_sku_dialog_customPropertyBag[
                  "[datestring]" + item.attributeId
                ] = this.rawPropertyBags[index].attributeValue;
              } else if (item.dataType === "image") {
                this.new_sku_dialog_customPropertyBag[
                  "[image]" + item.attributeId
                ] = this.rawPropertyBags[index].attributeValue;
              }
              console.log(
                "set new_sku_dialog_customPropertyBag after get value in copy create: "
              );
              console.log(this.new_sku_dialog_customPropertyBag);
            }
          }
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    popMeUp(skuId) {
      this.clearForm();
      this.mode = "edit";
      this.currentSkuId = skuId;
      this.loadProductSkuId(skuId);
    },
    toggleCreateSku() {
      this.clearForm();
      this.mode = "create";
      this.editskudialogopen = true;
    },
    toggleCopyCreateSku(skuId) {
      this.clearForm();
      this.mode = "copycreate";
      this.editskudialogopen = true;
      this.loadProductSkuId(skuId);
    },
    updateWithPropertyBag() {
      if (
        this.editSkuDialogProductName == null ||
        this.editSkuDialogProductName.length < 1
      ) {
        this.showNotifyMessageFail("产品名不能为空");
        return;
      }
      var params = {};
      params.productName = this.editSkuDialogProductName;
      params.productContent = this.editSkuDialogDescription;
      this.editskudialogopen = false;
      params.propertyBags = this.formatDateStringInUpdate(this.rawPropertyBags);
      var storeThis = this;
      api
        .put("/api/v1/productsku/withpropertybags/" + this.currentSkuId, params)
        .then((response) => {
          console.log(response.data);
          this.showNotifyMessageSucceed(response.data.message);
          this.$emit("close-me-event");
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
    insertImg(item, key) {
      this.imageObj2Set = item;
      this.imageObjKey2Set = key;
      this.imageUploadBox = true;
    },
    insertVid() {
      this.insertVideoLinkBox = true;
      this.videolink = "";
    },
    pasteCapture(evt) {
      // Let inputs do their thing, so we don't break pasting of links.
      if (evt.target.nodeName === "INPUT") return;
      let text, onPasteStripFormattingIEPaste;
      evt.preventDefault();
      evt.stopPropagation();
      if (evt.originalEvent && evt.originalEvent.clipboardData.getData) {
        text = evt.originalEvent.clipboardData.getData("text/plain");
        this.$refs.editDialogEditor.runCmd("insertText", text);
      } else if (evt.clipboardData && evt.clipboardData.getData) {
        text = evt.clipboardData.getData("text/plain");
        this.$refs.editDialogEditor.runCmd("insertText", text);
      } else if (window.clipboardData && window.clipboardData.getData) {
        if (!onPasteStripFormattingIEPaste) {
          onPasteStripFormattingIEPaste = true;
          this.$refs.editDialogEditor.runCmd("ms-pasteTextOnly", text);
        }
        onPasteStripFormattingIEPaste = false;
      }
    },
    dropCapture(evt) {
      console.log(evt);
    },
  },
};
</script>
