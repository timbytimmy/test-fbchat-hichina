<template>
  <q-page padding>
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

    <q-dialog v-model="alert">
      <q-card>
        <q-card-section>
          <div class="text-h6">警告</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          请正确完整填写属性值和类型
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="OK" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="popupNewAttributeWindow">
      <q-card>
        <q-card-section>
          <div class="text-h6">添加属性</div>
        </q-card-section>

        <q-separator />

        <q-card-section
          style="max-height: 50vh; min-width: 30vw"
          class="scroll"
        >
          <q-input
            outlined
            :rules="[(val) => !!val || '不能为空']"
            v-model="attr_name"
            label="属性名"
          />
          <q-select
            outlined
            v-model="attr_data_type"
            :options="data_type_options"
            label="数据类型"
          />
        </q-card-section>

        <q-separator />

        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn flat label="创建" @click="doNewAttribute" color="primary" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <div class="row justify-center q-pa-md">
      <q-table
        ref="tableRef"
        style="max-width: 98%; min-width: 90%"
        title="产品属性库"
        :rows="attributes"
        :columns="columns"
        v-model:pagination="serverPagination"
        :loading="loading"
        :filter="filter"
        row-key="attributeId"
        :separator="'cell'"
        @request="onRequest"
        selection="multiple"
        v-model:selected="selected"
      >
        <template v-slot:top-right>
          <q-btn
            color="deep-orange"
            glossy
            @click="newAttribute"
            label="新建属性"
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
        <template v-slot:body-cell-attributeName="props">
          <q-td :props="props"> {{ props.value }}</q-td>
        </template>
        <template v-slot:body-cell-dataType="props">
          <q-td :props="props"> {{ props.value }} </q-td>
        </template>
      </q-table>
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
export default {
  name: "ProductSharedAttributesPage",
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
      confirmDelete: false,
      selected: [],
      columns: [
        // {
        //   name: "attribute_id",
        //   required: true,
        //   label: "属性id",
        //   align: "left",
        //   field: "attribute_id",
        //   sortable: false,
        //   style:
        //     "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        // },
        {
          name: "attributeName",
          required: true,
          label: "属性名",
          align: "left",
          field: "attributeName",
          sortable: false,
          style:
            "max-width: 15vw;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "dataType",
          required: true,
          label: "数据类型",
          align: "left",
          field: "dataType",
          sortable: false,
          style:
            "max-width: 15vw;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
      ],
      attributes: [],
      loading: false,
      filter: "",
      serverPagination: {
        page: 1,
        rowsPerPage: 100,
        rowsNumber: 10, // specifying this determines pagination is server-side
      },
      alert: false,
      attr_data_type: {
        label: "字符串",
        value: "string",
      },
      attr_name: "",
      popupNewAttributeWindow: false,
      data_type_options: [
        {
          label: "字符串",
          value: "string",
        },
        {
          label: "整数",
          value: "integer",
        },
        {
          label: "日期",
          value: "date",
        },
        {
          label: "图片",
          value: "image",
        },
      ],
    };
  },
  mounted() {
    this.$refs.tableRef.requestServerInteraction();
  },
  methods: {
    executeDelete() {
      var listOfId2Del = [];

      for (var i in this.selected) {
        listOfId2Del.push(this.selected[i].attributeId);
        console.log("things to del:");
        console.log(listOfId2Del);
      }
      var params = {};
      params.toDelete = listOfId2Del;
      api
        .delete("/api/v1/productsharedattributes/batch", { data: params })
        .then((response) => {
          this.showNotifyMessageSucceed(response.data.message);
          this.$refs.tableRef.requestServerInteraction();
        })
        .catch((e) => {
          this.showNotifyMessageFail("请确认改属性当前无引用");
        });
    },
    goDelete() {
      if (this.selected.length < 1) {
        this.showNotifyMessageFail("没有选中任何一项");
        return;
      }
      this.confirmDelete = true;
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
        .get("/api/v1/productsharedattributes", {
          params: params,
        })
        .then((response) => {
          this.attributes = response.data.data.data;
          console.log("logging of attributes");
          console.log(this.attributes);
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
    doNewAttribute() {
      if (
        this.attr_name == null ||
        this.attr_name.length < 1 ||
        this.attr_data_type == ""
      ) {
        this.alert = true;
        return;
      }

      var params = {};
      params.name = this.attr_name;
      params.dataType = this.attr_data_type.value;

      api
        .post("/api/v1/productsharedattributes", params)
        .then((response) => {
          console.log(response.data);
          if (response.data.ok == true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.popupNewAttributeWindow = false;
            this.$refs.tableRef.requestServerInteraction();
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    newAttribute() {
      // create attribute, with just input name and data type
      this.attr_name = "";
      this.attr_data_type = "";
      this.popupNewAttributeWindow = true;
    },
  },
};
</script>
