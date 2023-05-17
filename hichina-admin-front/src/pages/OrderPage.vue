<template>
  <q-page padding>
    <q-dialog
      v-model="detailPopup"
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
          <div class="text-h6">订单号：{{ orderDetail.orderId }}</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          创建时间: {{ orderDetail.createdTime }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          最后更新时间: {{ orderDetail.lastUpdateTime }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          用户Id: {{ orderDetail.userId }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          产品名: {{ orderDetail.productName }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          产品类型: {{ orderDetail.productTypeName }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          skuId: {{ orderDetail.skuId }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          订单总价: {{ orderDetail.price }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          订单状态: {{ orderDetail.status }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          备注: {{ orderDetail.remark }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          支付信息: {{ orderDetail.payingInfo }}
        </q-card-section>
        <q-card-section class="q-pt-none">
          产品详情
          <vue-json-pretty v-model:data="jsonData" />
        </q-card-section>
      </q-card>
    </q-dialog>
    <div class="row justify-center q-pa-md">
      <q-table
        ref="orderTableRef"
        style="max-width: 98%; min-width: 90%"
        title="订单管理"
        :rows="orders"
        :columns="columns"
        v-model:pagination="serverPagination"
        :loading="loading"
        :filter="filter"
        row-key="orderId"
        :separator="'cell'"
        @request="onRequest"
      >
        <template v-slot:top-right>
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
        <template v-slot:body-cell-orderId="props">
          <q-td :props="props">
            <q-btn
              color="blue-grey-3"
              style="cursor: pointer"
              @click="copyText(props.row.orderId)"
              type="button"
              >{{ props.row.orderId }}</q-btn
            >
          </q-td>
        </template>
        <template v-slot:body-cell-userId="props">
          <q-td :props="props">
            <q-btn
              color="blue-grey-3"
              style="cursor: pointer"
              @click="copyText(props.row.userId)"
              type="button"
              >{{ props.row.userId }}</q-btn
            >
          </q-td>
        </template>
        <template v-slot:body-cell-action="props">
          <q-td :props="props">
            <q-btn
              color="purple"
              dense
              label="更多详情"
              @click="goOrderDetail(props.row.orderId)"
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
import VueJsonPretty from "vue-json-pretty";
import "vue-json-pretty/lib/styles.css";
import { ref } from "vue";

export default {
  name: "OrderPage",
  components: {
    VueJsonPretty,
  },
  setup() {
    const $q = useQuasar();

    return {
      jsonData: ref(""),
      orderDetail: ref({}),
      detailPopup: ref(false),
      goOrderDetail(orderId) {
        let Base64 = require("js-base64").Base64;

        this.detailPopup = true;
        api
          .get("/api/v1/order/" + orderId)
          .then((response) => {
            this.orderDetail.orderId = response.data.data.orderId;
            this.orderDetail.createdTime = response.data.data.createdTime;
            this.orderDetail.productName = response.data.data.productName;
            this.orderDetail.userId = response.data.data.userId;
            this.orderDetail.skuId = response.data.data.skuId;
            this.orderDetail.status = response.data.data.status;
            this.orderDetail.lastUpdateTime = response.data.data.lastUpdateTime;
            this.orderDetail.payingInfo = response.data.data.payingInfo;
            this.orderDetail.price = response.data.data.price;
            this.orderDetail.remark = response.data.data.remark;
            this.orderDetail.productTypeName =
              response.data.data.productTypeName;
            this.orderDetail.meta = Base64.decode(response.data.data.meta);

            this.jsonData = JSON.parse(this.orderDetail.meta);
          })
          .catch((e) => {
            console.log(e);
            this.showNotifyMessageFail(e.response);
          });
      },
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
    keyClick(keyName) {
      console.log(keyName, "it was click");
    },
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
        .get("/api/v1/order", {
          params: params,
        })
        .then((response) => {
          this.orders = response.data.data.data;
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
  mounted() {
    this.$refs.orderTableRef.requestServerInteraction();
  },
  data() {
    return {
      escapeJson: "{ key: 'val' }",
      // orderDetail: {
      //   skuId: "",
      //   userId: "",
      //   productName: "",
      //   orderId: "",
      //   createdTime: "",
      //   meta: "",
      // },

      // detailPopup: false,
      orders: [],
      filter: "",
      loading: false,
      serverPagination: {
        page: 1,
        rowsPerPage: 100,
        rowsNumber: 10, // specifying this determines pagination is server-side
      },
      columns: [
        {
          name: "orderId",
          required: true,
          label: "订单号",
          align: "left",
          field: "orderId",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "userId",
          required: true,
          label: "用户ID",
          align: "left",
          field: "userId",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "productName",
          required: true,
          label: "产品名",
          align: "left",
          field: "productName",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "productTypeName",
          required: true,
          label: "产品类型",
          align: "left",
          field: "productTypeName",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "createdTime",
          required: true,
          label: "下单时间",
          align: "left",
          field: "createdTime",
          sortable: false,
          style:
            "max-width: 130px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "price",
          required: true,
          label: "订单价格",
          align: "left",
          field: "price",
          sortable: false,
          style:
            "max-width: 90px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "status",
          required: true,
          label: "订单状态",
          align: "left",
          field: "status",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        { name: "action", label: "操作", field: "action" },
      ],
    };
  },
};
</script>
