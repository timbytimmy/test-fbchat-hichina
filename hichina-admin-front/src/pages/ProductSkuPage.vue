<template>
  <q-page padding>
    <!-- <my-upload
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
    </my-upload> -->
    <q-dialog v-model="confirmDelete" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="signal_wifi_off" color="primary" text-color="white" />
          <span class="q-ml-sm">确定要删除所选吗？</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn
            flat
            label="确认删除"
            color="primary"
            @click="executeDelete"
            v-close-popup
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
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
    <ProductEditorDialog
      ref="updateDetailDialog"
      @close-me-event="refreshTable"
    />
    <!-- <q-dialog
      v-model="newskudialog"
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
          <div class="text-h6">新建产品sku</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div style="width: 100%">
            <div class="row justify-center">
              <q-select
                @update:model-value="(val) => productTypeChange(val)"
                outlined
                class="col-8"
                style="min-width: 200px"
                v-model="new_sku_dialog_product_type"
                :options="product_type_options"
                label="产品类别过滤"
              />
            </div>
            <div class="row justify-center">
              <q-input
                class="col-8"
                filled
                v-model="new_sku_dialog_product_name"
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
              <q-editor
                v-model="new_sku_dialog_description"
                ref="newSkuEditor"
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
              />
            </div>
          </div>
        </q-card-section>
        <q-separator inset class="col" />
        <div class="row justify-center">
          <div class="col-1">自定义属性</div>
        </div>
        <q-separator inset class="col" />
        <q-card-section>
          <div v-for="item in rawPropertyBags" v-bind:key="item.attribute_id">
            <div class="row justify-left" v-if="item.data_type === 'string'">
              <q-input
                class="col-8"
                outlined
                type="textarea"
                v-model="
                  new_sku_dialog_customPropertyBag[
                    '[string]' + item.attribute_id
                  ]
                "
                :label="item.attribute_name"
                :rules="[
                  (val) =>
                    val.length <= 200 || 'Please use maximum 200 characters',
                ]"
              />
            </div>
            <div class="row justify-left" v-if="item.data_type === 'integer'">
              <q-input
                class="col-3"
                outlined
                v-model.number="
                  new_sku_dialog_customPropertyBag[
                    '[integer]' + item.attribute_id
                  ]
                "
                type="number"
                :label="item.attribute_name"
              />
            </div>
            <div v-if="item.data_type === 'date'">
              <label>{{ item.attribute_name }}</label>
              <br />
              <q-date
                minimal
                v-model="
                  new_sku_dialog_customPropertyBag['[date]' + item.attribute_id]
                "
              />
            </div>
            <div v-if="item.data_type === 'image'">
              <q-btn
                push
                color="primary"
                label="上传图标"
                @click="toggleImageUpload('[image]' + item.attribute_id)"
              />

              <q-img
                v-if="
                  new_sku_dialog_customPropertyBag[
                    '[image]' + item.attribute_id
                  ] != ''
                "
                :src="
                  new_sku_dialog_customPropertyBag[
                    '[image]' + item.attribute_id
                  ]
                "
                spinner-color="white"
                style="height: 140px; max-width: 150px"
              />
            </div>
          </div>
        </q-card-section>
        <q-card-section>
          <q-btn color="primary" label="提交" @click="submitWithPropertyBag" />
        </q-card-section>
      </q-card>
    </q-dialog> -->

    <div class="row justify-center q-pa-md">
      <q-table
        ref="productSkuTableRef"
        style="max-width: 98%; min-width: 90%"
        title="产品细项（具体产品上架）"
        :rows="productskus"
        :columns="columns"
        v-model:pagination="serverPagination"
        :loading="loading"
        :filter="filter"
        row-key="sku_id"
        :separator="'cell'"
        @request="onRequest"
        selection="multiple"
        v-model:selected="selected"
      >
        <template v-slot:top-right>
          <q-select
            outlined
            @update:model-value="(val) => pageProductTypeChange(val)"
            style="margin-right: 50px; min-width: 200px"
            v-model="product_type_filter"
            :options="product_type_options"
            label="产品类别过滤"
          />
          <q-btn
            color="deep-orange"
            glossy
            @click="newProductSKU"
            label="新建产品SKU"
            style="margin-right: 50px"
          />
          <q-btn
            style="margin-right: 50px"
            round
            color="primary"
            @click="goDelete"
            icon="delete"
          />
          <q-input
            borderless
            dense
            debounce="300"
            v-model="filter"
            placeholder="Search"
          >
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </template>
        <template v-slot:body-cell-action="props">
          <q-td :props="props">
            <q-btn
              color="orange"
              dense
              label="拷贝skuId"
              @click="copyText(props.row.sku_id)"
            />
            <q-btn
              color="primary"
              dense
              label="复制创建"
              @click="copyCreate(props.row.sku_id)"
            />
            <q-btn
              color="purple"
              dense
              label="编辑"
              @click="goProductDetail(props.row.sku_id)"
            />
          </q-td>
        </template>
      </q-table>
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
// import myUpload from "vue-image-crop-upload";
import ProductEditorDialog from "components/ProductEditorDialog.vue";
// import UploadImage from "v-upload-image";
import { myMixin } from "boot/commonFunc";
export default {
  name: "ProductSkuPages",
  mixins: [myMixin],
  components: {
    ProductEditorDialog,
    // "upload-image": UploadImage,
    // "my-upload": myUpload,
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
  methods: {
    copyText(text) {
      const unsecuredCopyToClipboard = (text) => {
        const textArea = document.createElement("textarea");
        textArea.value = text;
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        try {
          document.execCommand("copy");
        } catch (err) {
          console.error("Unable to copy to clipboard", err);
        }
        document.body.removeChild(textArea);
      };

      if (window.isSecureContext && navigator.clipboard) {
        navigator.clipboard.writeText(text);
        this.showNotifyMessageSucceed("成功复制" + text + "到剪贴板");
      } else {
        unsecuredCopyToClipboard(text);
        this.showNotifyMessageSucceed("成功复制" + text + "到剪贴板");
      }
    },
    refreshTable() {
      console.log("post create refresh...");
      this.$refs.productSkuTableRef.requestServerInteraction();
      console.log("ppppppp");
    },
    cropUploadFail(status, field) {
      console.log("-------- upload fail --------");
      console.log(status);
      console.log("field: " + field);
      this.showNotifyMessageFail("上传失败");
    },
    cropUploadSuccess(jsonData, field) {
      console.log("field: " + field);
      this.new_sku_dialog_customPropertyBag[this.currentProcessingImageKey] =
        jsonData.data;
      this.showNotifyMessageSucceed("上传成功");
      this.showImageUpload = false;
    },
    cropSuccess(imgDataUrl, field) {
      console.log("-------- crop success --------");
      this.imgDataUrl = imgDataUrl;
    },
    toggleImageUpload(val) {
      this.currentProcessingImageKey = val;
      this.showImageUpload = !this.showImageUpload;
      this.upload_icon_params.expectedType = "thumbnail";
    },
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
      this.$refs.newSkuEditor.runCmd(
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
          this.$refs.newSkuEditor.runCmd("insertImage", response.data.data);

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
    executeDelete() {
      console.log("to delete selected:");
      console.log(this.selected);

      var listOfId2Del = [];
      for (var i in this.selected) {
        listOfId2Del.push(this.selected[i].sku_id);
      }

      var params = {};
      params.toDelete = listOfId2Del;
      api
        .delete("/api/v1/productsku/batch", { data: params })
        .then((response) => {
          this.showNotifyMessageSucceed(response.data.message);
          this.$refs.productSkuTableRef.requestServerInteraction();
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    goDelete() {
      if (this.selected.length < 1) {
        this.showNotifyMessageFail("没有选中任何项");
        return;
      }
      this.confirmDelete = true;
    },
    pageProductTypeChange() {
      this.$refs.productSkuTableRef.requestServerInteraction();
    },
    onRequest(props) {
      const { page, rowsPerPage } = props.pagination;
      const filter = props.filter;

      var storeThis = this;

      this.loading = true;

      var params = {};
      params.page = page;
      params.pageSize = rowsPerPage;
      params.query = filter;
      console.log("what product type filter it is:");
      console.log(this.product_type_filter);
      params.productTypeId =
        this.product_type_filter == "" ? "" : this.product_type_filter.value;

      api
        .get("/api/v1/productsku", {
          params: params,
        })
        .then((response) => {
          console.log(response.data);
          this.productskus = response.data.data.data;
          console.log("this is the product skus I got:");
          console.log(this.productskus);
          this.serverPagination.page = response.data.data.currentPage;
          this.serverPagination.rowsNumber = response.data.data.total;
          this.serverPagination.rowsPerPage = response.data.data.pageSize;
          console.log(this.serverPagination);
          this.loading = false;
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    pasteCapture(evt) {
      // Let inputs do their thing, so we don't break pasting of links.
      // Let inputs do their thing, so we don't break pasting of links.
      if (evt.target.nodeName === "INPUT") return;
      let text, onPasteStripFormattingIEPaste;
      evt.preventDefault();
      evt.stopPropagation();
      if (evt.originalEvent && evt.originalEvent.clipboardData.getData) {
        text = evt.originalEvent.clipboardData.getData("text/plain");
        this.$refs.newSkuEditor.runCmd("insertText", text);
      } else if (evt.clipboardData && evt.clipboardData.getData) {
        text = evt.clipboardData.getData("text/plain");
        this.$refs.newSkuEditor.runCmd("insertText", text);
      } else if (window.clipboardData && window.clipboardData.getData) {
        if (!onPasteStripFormattingIEPaste) {
          onPasteStripFormattingIEPaste = true;
          this.$refs.newSkuEditor.runCmd("ms-pasteTextOnly", text);
        }
        onPasteStripFormattingIEPaste = false;
      }
    },
    dropCapture(evt) {
      console.log(evt);
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
          this.product_type_options = [];
          for (var i in allProductTypes) {
            var obj = {};
            obj.value = allProductTypes[i].product_type_id;
            obj.label = allProductTypes[i].product_type_name;
            this.product_type_options.push(obj);
          }
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    newProductSKU() {
      // this.newskudialog = true;
      // this.new_sku_dialog_product_name = "";
      // this.new_sku_dialog_description = "";
      // this.new_sku_dialog_product_type = "";
      // this.new_sku_dialog_customPropertyBag = {};
      this.$refs.updateDetailDialog.toggleCreateSku();
    },
    goProductDetail(skuId) {
      this.$refs.updateDetailDialog.popMeUp(skuId);
      this.editskudialogopen = true;
    },
    copyCreate(skuId) {
      this.$refs.updateDetailDialog.toggleCopyCreateSku(skuId);
    },
  },
  mounted() {
    this.getRestBase();
    this.$refs.productSkuTableRef.requestServerInteraction();
    this.loadAllProductTypes();
  },
  data() {
    return {
      currentProcessingImageKey: "",
      upload_icon_params: {},
      rest_base: "",
      image: "",
      showImageUpload: false,
      icon_image_path: false,
      maximizedToggle: false,
      videolink: "",
      insertVideoLinkBox: false,
      imageUploadBox: false,
      confirmDelete: false,
      editskudialogopen: true,
      productskus: [],
      rawPropertyBags: [],
      new_sku_dialog_customPropertyBag: {},
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
      new_sku_dialog_description: "",
      new_sku_dialog_product_type: "",
      new_sku_dialog_product_name: "",
      newskudialog: false,
      product_type_options: [],
      product_type_filter: "",
      selected: [],
      filter: "",
      loading: false,
      serverPagination: {
        page: 1,
        rowsPerPage: 100,
        rowsNumber: 10, // specifying this determines pagination is server-side
      },
      columns: [
        {
          name: "product_name",
          required: true,
          label: "产品名",
          align: "left",
          field: "product_name",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "product_type_name",
          required: true,
          label: "产品类别",
          align: "left",
          field: "product_type_name",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "product_content",
          required: false,
          label: "产品内容",
          align: "left",
          field: "product_content",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "created_time",
          required: true,
          label: "创建时间",
          align: "left",
          field: "created_time",
          sortable: false,
          style:
            "max-width: 120px;min-width: 120px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        { name: "action", label: "操作", field: "action" },
      ],
    };
  },
};
</script>
