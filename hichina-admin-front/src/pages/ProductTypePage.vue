<template>
  <q-dialog v-model="alert">
    <q-card>
      <q-card-section>
        <div class="text-h6">警告</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        请正确完整填写产品类型名
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="OK" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="popupBindAttributeWindow">
    <q-card>
      <q-card-section>
        <div class="text-h6">产品类型属性设定</div>
      </q-card-section>

      <q-separator />

      <div style="width: 50vw; height: 70vh; display: flex">
        <div style="width: 38%; max-width: 38%; height: 100%">
          <label style="color: red; font-weight: bold">候选属性</label>
          <q-list bordered separator>
            <q-item
              clickable
              v-ripple
              :active="item.attributeId === selected_left_attr"
              @click="setActiveSelect(item.attributeId)"
              active-class="bg-teal-1 text-grey-8"
              v-for="item in candidateAttributes"
              v-bind:key="item.attributeId"
            >
              <q-item-section>
                <q-item-label>属性名： {{ item.attributeName }}</q-item-label>
                <q-item-label overline
                  >数据类型：{{ item.dataType }}</q-item-label
                >
              </q-item-section>
            </q-item>
          </q-list>
        </div>

        <div style="min-width: 10px; height: 100%">
          <div>
            <q-btn
              style="margin-top: 10px"
              round
              color="secondary"
              icon="navigate_next"
              @click="doBind(selected_left_attr)"
            />
          </div>
          <div>
            <q-btn
              style="margin-top: 10px"
              round
              color="secondary"
              @click="unBind(selected_right_attr)"
              icon="navigate_before"
            />
          </div>
        </div>
        <div style="width: 38%; height: 100%">
          <label style="color: red; font-weight: bold">已关联属性</label>
          <q-list bordered separator>
            <q-item
              clickable
              v-ripple
              :active="item.attributeId === selected_right_attr"
              @click="setActiveSelectRight(item.attributeId)"
              active-class="bg-teal-1 text-grey-8"
              v-for="item in bindedAttrs"
              v-bind:key="item.attributeId"
              ou
            >
              <q-item-section>
                <q-item-label>属性名： {{ item.attributeName }}</q-item-label>
                <q-item-label overline
                  >数据类型：{{ item.dataType }}</q-item-label
                >
              </q-item-section>
            </q-item>
          </q-list>
        </div>
      </div>
    </q-card>
  </q-dialog>

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

  <q-dialog v-model="popupNewProductTypeWindow">
    <q-card>
      <q-card-section>
        <div class="text-h6">添加产品类型</div>
      </q-card-section>

      <q-separator />

      <q-card-section style="max-height: 50vh; min-width: 30vw" class="scroll">
        <q-input
          outlined
          :rules="[(val) => !!val || '不能为空']"
          v-model="product_type_name"
          label="产品类型名"
        />
        <q-input
          v-model="product_type_description"
          filled
          type="textarea"
          placeholder="产品类型描述"
        />
      </q-card-section>

      <q-separator />

      <q-card-actions align="right">
        <q-btn flat label="取消" color="primary" v-close-popup />
        <q-btn flat label="创建" @click="doNewProductType" color="primary" />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <div class="row justify-center q-pa-md">
    <q-table
      ref="tableRef"
      style="max-width: 98%; min-width: 90%"
      title="产品类别定义"
      :rows="producttypes"
      :columns="columns"
      v-model:pagination="serverPagination"
      :loading="loading"
      :filter="filter"
      row-key="product_type_id"
      :separator="'cell'"
      @request="onRequest"
      selection="multiple"
      v-model:selected="selected"
    >
      <template v-slot:top-right>
        <q-btn
          color="deep-orange"
          glossy
          @click="newProductType"
          label="新建产品类别"
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
      <template v-slot:body-cell-product_type_name="props">
        <q-td :props="props"> {{ props.value }}</q-td>
      </template>
      <template v-slot:body-cell-product_type_description="props">
        <q-td :props="props"> {{ props.value }} </q-td>
      </template>
      <template v-slot:body-cell-action="props">
        <q-td :props="props">
          <q-btn
            color="purple"
            dense
            label="属性绑定&查看"
            @click="goBindAttributes(props.row.product_type_id)"
          />
        </q-td>
      </template>
    </q-table>
  </div>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
export default {
  name: "ProductType",
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
      alert: false,
      selected_left_attr: "",
      selected_right_attr: "",
      active: true,
      popupBindAttributeWindow: false,
      confirmDelete: false,
      selected: [],
      clicked_product_type_id: "",
      columns: [
        {
          name: "product_type_name",
          required: true,
          label: "产品类型名",
          align: "left",
          field: "product_type_name",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "product_type_description",
          required: true,
          label: "产品类型描述",
          align: "left",
          field: "product_type_description",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        { name: "action", label: "操作", field: "action" },
      ],
      filter: "",
      product_type_name: "",
      product_type_description: "",
      popupNewProductTypeWindow: false,
      loading: false,
      candidateAttributes: [],
      producttypes: [],
      bindedAttrs: [],
      serverPagination: {
        page: 1,
        rowsPerPage: 100,
        rowsNumber: 10, // specifying this determines pagination is server-side
      },
    };
  },
  mounted() {
    this.$refs.tableRef.requestServerInteraction();
  },
  methods: {
    unBind(attr_id) {
      if (attr_id == null || attr_id.length < 1) {
        this.showNotifyMessageFail("未选定属性");
        return;
      }
      var params = {};
      params.productTypeId = this.clicked_product_type_id;
      params.attributeId = attr_id;

      api
        .post("/api/v1/producttype/unbindattr", params)
        .then((response) => {
          if (response.data.ok == true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.loadBindedAttributes(this.clicked_product_type_id);
            this.selected_right_attr = "";
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    doBind(attr_id) {
      if (attr_id == null || attr_id.length < 1) {
        this.showNotifyMessageFail("未选定属性");
        return;
      }
      var params = {};
      params.productTypeId = this.clicked_product_type_id;
      params.attributeId = attr_id;

      api
        .post("/api/v1/producttype/bindattr", params)
        .then((response) => {
          if (response.data.ok == true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.loadBindedAttributes(this.clicked_product_type_id);
            this.selected_left_attr = "";
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    setActiveSelect(attr_id) {
      this.selected_left_attr = attr_id;
    },
    setActiveSelectRight(attr_id) {
      this.selected_right_attr = attr_id;
    },
    loadAllAttributes() {
      var params = {};
      // -1 indicates don't want paging at all
      params.page = -1;
      params.pageSize = -1;
      params.query = "";
      var storeThis = this;

      api
        .get("/api/v1/productsharedattributes", {
          params: params,
        })
        .then((response) => {
          this.candidateAttributes = response.data.data.data;
          console.log("candidateattributes:");
          console.log(this.candidateAttributes);
          var toDelete = this.bindedAttrs.map((a) => a.attributeId);
          // do exclude those already binded ones
          this.candidateAttributes = this.candidateAttributes.filter(function (
            el
          ) {
            return toDelete.indexOf(el.attributeId) < 0;
          });
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    goBindAttributes(val) {
      this.clicked_product_type_id = val;
      this.popupBindAttributeWindow = true;
      this.selected_right_attr = "";
      this.selected_left_attr = "";
      this.loadBindedAttributes(this.clicked_product_type_id);
      console.log("绑定属性操作");
    },
    loadBindedAttributes(selected_product_type_id) {
      var params = {};
      params.productTypeId = selected_product_type_id;
      var storeThis = this;
      api
        .get("/api/v1/producttype/bindedattributesforproduct", {
          params: params,
        })
        .then((response) => {
          this.bindedAttrs = response.data.data;
          console.log("logging of binded attr for " + selected_product_type_id);
          console.log(this.bindedAttrs);
          this.loadAllAttributes();
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    executeDelete() {
      var listOfId2Del = [];

      for (var i in this.selected) {
        listOfId2Del.push(this.selected[i].product_type_id);
        console.log("things to del:");
        console.log(listOfId2Del);
      }
      var params = {};
      params.toDelete = listOfId2Del;
      api
        .delete("/api/v1/producttype/batch", { data: params })
        .then((response) => {
          this.showNotifyMessageSucceed(response.data.message);
          this.$refs.tableRef.requestServerInteraction();
        })
        .catch((e) => {
          this.showNotifyMessageFail("请确认当前类别无引用");
        });
    },
    goDelete() {
      if (this.selected.length < 1) {
        this.showNotifyMessageFail("没有选中任何一项");
        return;
      }
      this.confirmDelete = true;
    },
    doNewProductType() {
      if (this.product_type_name == null || this.product_type_name.length < 1) {
        this.alert = true;
        return;
      }

      var params = {};
      params.name = this.product_type_name;
      params.description = this.product_type_description;

      api
        .post("/api/v1/producttype", params)
        .then((response) => {
          console.log(response.data);
          if (response.data.ok == true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.popupNewProductTypeWindow = false;
            this.$refs.tableRef.requestServerInteraction();
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    newProductType() {
      this.product_type_name = "";
      this.product_type_description = "";
      this.popupNewProductTypeWindow = true;
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

      api
        .get("/api/v1/producttype", {
          params: params,
        })
        .then((response) => {
          this.producttypes = response.data.data.data;
          console.log("logging of producttypes");
          console.log(this.producttypes);
          this.serverPagination.page = response.data.data.currentPage;
          this.serverPagination.rowsNumber = response.data.data.total;
          this.serverPagination.rowsPerPage = response.data.data.pageSize;
          this.loading = false;
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
  },
};
</script>
