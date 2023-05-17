<template>
  <q-page padding>
    <div class="row justify-center q-pa-md">
      <q-table
        ref="skuGroupTableRef"
        style="max-width: 98%; min-width: 90%"
        title="产品管理"
        :rows="skuGroups"
        :columns="columns"
        v-model:pagination="serverPagination"
        :loading="loading"
        :filter="filter"
        row-key="skuGroupId"
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
        <template v-slot:body-cell-skuGroupId="props">
          <q-td :props="props">
            <q-btn
              v-if="props.row.enabled == false"
              color="blue-grey-3"
              style="cursor: pointer"
              @click="copyText(props.row.skuGroupId)"
              type="button"
              >{{ props.row.skuGroupId }}</q-btn
            >
            <q-btn
              v-if="props.row.enabled == true"
              color="green"
              style="cursor: pointer"
              @click="copyText(props.row.skuGroupId)"
              type="button"
              >{{ props.row.skuGroupId }}</q-btn
            >
          </q-td>
        </template>
        <template v-slot:body-cell-action="props">
          <q-td :props="props">
            <q-btn
              v-if="props.row.enabled == false"
              color="purple"
              dense
              label="启用"
              @click="enableGroup(props.row.skuGroupId)"
            />
            <q-btn
              v-if="props.row.enabled == true"
              color="purple"
              dense
              label="停用"
              @click="disableGroup(props.row.skuGroupId)"
            />
          </q-td>
        </template>
      </q-table></div
  ></q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
export default {
  name: "SkuGroupPage",
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
  mounted() {
    this.$refs.skuGroupTableRef.requestServerInteraction();
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
    enableGroup(skuGroupId) {
      api
        .put("/api/v1/skugroup/enable/" + skuGroupId)
        .then((response) => {
          if (response.data.ok === true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.$refs.skuGroupTableRef.requestServerInteraction();
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
    disableGroup(skuGroupId) {
      api
        .put("/api/v1/skugroup/disable/" + skuGroupId)
        .then((response) => {
          if (response.data.ok === true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.$refs.skuGroupTableRef.requestServerInteraction();
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
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
        .get("/api/v1/skugroup/groupstats", {
          params: params,
        })
        .then((response) => {
          this.skuGroups = response.data.data.data;
          console.log("this is the group stats I got:");
          console.log(this.skuGroups);

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
  data() {
    return {
      selected: [],
      filter: "",
      loading: false,
      serverPagination: {
        page: 1,
        rowsPerPage: 100,
        rowsNumber: 10, // specifying this determines pagination is server-side
      },
      skuGroups: [],
      columns: [
        {
          name: "skuGroupId",
          required: true,
          label: "SKU组ID",
          align: "left",
          field: "skuGroupId",
          sortable: false,
          style:
            "max-width: 20vw;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "skuGroupName",
          required: true,
          label: "SKU组名（同产品名）",
          align: "left",
          field: "skuGroupName",
          sortable: false,
          style:
            "max-width: 20vw;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "productTypeName",
          required: true,
          label: "产品类型",
          align: "left",
          field: "productTypeName",
          sortable: false,
          style:
            "max-width: 20vw;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "productCount",
          required: true,
          label: "SKU组成数",
          align: "left",
          field: "productCount",
          sortable: false,
          style:
            "max-width: 20vw;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        { name: "action", label: "操作", field: "action" },
      ],
    };
  },
};
</script>
