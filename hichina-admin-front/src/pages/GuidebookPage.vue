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
    <GuidebookCreateDialog
      ref="guidebookCreateDialgRef"
      @close-me-event="refreshTable"
    />
    <div class="row justify-center q-pa-md">
      <q-table
        ref="guidebookTableRef"
        style="max-width: 98%; min-width: 90%"
        title="攻略书管理"
        :rows="guidebooks"
        :columns="columns"
        v-model:pagination="serverPagination"
        :loading="loading"
        :filter="filter"
        row-key="guideId"
        :separator="'cell'"
        @request="onRequest"
        selection="multiple"
        v-model:selected="selected"
      >
        <template v-slot:top-right>
          <q-btn
            color="deep-orange"
            glossy
            @click="newGuideBook"
            label="新建攻略书"
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
        <template v-slot:body-cell-downloadUrl="props">
          <q-td :props="props">
            <a :href="props.row.downloadUrl">下载链接</a>
          </q-td>
        </template>
        <template v-slot:body-cell-coverImageUrl="props">
          <q-td :props="props">
            <img
              style="height: 140px; width: 150px"
              :src="props.row.coverImageUrl"
            />
          </q-td>
        </template>
        <template v-slot:body-cell-action="props">
          <q-td :props="props">
            <q-btn
              color="purple"
              dense
              label="更多详情"
              @click="goDetail(props.row.guideId)"
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
import GuidebookCreateDialog from "components/GuidebookCreateDialog.vue";
export default {
  name: "GuidebookPage",
  components: {
    GuidebookCreateDialog,
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
    executeDelete() {
      var listOfId2Del = [];
      for (var i in this.selected) {
        listOfId2Del.push(this.selected[i].guideId);
      }

      var storeThis = this;
      var params = {};
      params.toDelete = listOfId2Del;
      api
        .delete("/api/v1/guidebook/batch", { data: params })
        .then((response) => {
          this.showNotifyMessageSucceed(response.data.message);
          this.refreshTable();
          this.selected = [];
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
    goDetail(guidebookId) {
      this.$refs.guidebookCreateDialgRef.toggleDialogEditMode(guidebookId);
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
        .get("/api/v1/guidebook", {
          params: params,
        })
        .then((response) => {
          console.log("response data for guidebook");
          console.log(response.data);
          this.guidebooks = response.data.data.data;
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
    refreshTable() {
      this.$refs.guidebookTableRef.requestServerInteraction();
    },
    goDelete() {
      if (this.selected.length < 1) {
        this.showNotifyMessageFail("没有选中任何项");
        return;
      }
      this.confirmDelete = true;
    },
    newGuideBook() {
      this.$refs.guidebookCreateDialgRef.toggleDialog();
    },
    loadGuideBooks() {},
  },
  mounted() {
    this.refreshTable();
    this.loadGuideBooks();
  },
  data() {
    return {
      confirmDelete: false,
      selected: [],
      guidebooks: [],
      columns: [
        {
          name: "guideId",
          required: true,
          label: "攻略书ID",
          align: "left",
          field: "guideId",
          sortable: false,
          style:
            "max-width: 250px;min-width: 250px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "downloadUrl",
          required: true,
          label: "下载地址",
          align: "left",
          field: "downloadUrl",
          sortable: false,
          style:
            "max-width: 170px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "coverImageUrl",
          required: true,
          label: "封面图",
          align: "left",
          field: "coverImageUrl",
          sortable: false,
          style:
            "max-width: 170px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "shortDescription",
          required: true,
          label: "描述",
          align: "left",
          field: "shortDescription",
          sortable: false,
          style:
            "max-width: 120px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "destinationName",
          required: true,
          label: "目的地名",
          align: "left",
          field: "destinationName",
          sortable: false,
          style:
            "max-width: 170px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        { name: "action", label: "操作", field: "action" },
      ],
      serverPagination: {
        page: 1,
        rowsPerPage: 100,
        rowsNumber: 10, // specifying this determines pagination is server-side
      },
      loading: false,
      filter: "",
    };
  },
};
</script>
