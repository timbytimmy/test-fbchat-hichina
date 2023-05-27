<template>
  <q-page padding>
    <DestinationCreateDialog
      ref="destinationCreateDialgRef"
      @close-me-event="refreshTable"
    />
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
    <div class="row justify-center q-pa-md">
      <q-table
        ref="destinationTableRef"
        style="max-width: 98%; min-width: 90%"
        title="目的地管理"
        :rows="destinations"
        :columns="columns"
        v-model:pagination="serverPagination"
        :loading="loading"
        :filter="filter"
        row-key="destinationId"
        :separator="'cell'"
        @request="onRequest"
        selection="multiple"
        v-model:selected="selected"
      >
        <template v-slot:top-right>
          <q-btn
            color="deep-orange"
            glossy
            @click="newDestination"
            label="新建目的地"
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
              color="purple"
              dense
              label="编辑"
              @click="goDestinationDetail(props.row.destinationId)"
            />
            <q-btn
              color="green"
              dense
              label="ChatGpt生成简述"
              @click="chatGptGenerateDesc(props.row.destinationId)"
            />
          </q-td>
        </template>
        <template v-slot:body-cell-level="props">
          <q-td :props="props"> {{ levelMap[props.value] }} </q-td>
        </template>
      </q-table>
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
import DestinationCreateDialog from "components/DestinationCreateDialog.vue";
export default {
  name: "DestinationPage",
  components: {
    DestinationCreateDialog,
  },
  setup() {
    const $q = useQuasar();

    return {
      showLoading() {
        $q.loading.show();
      },
      hideLoading() {
        $q.loading.hide();
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
  data() {
    return {
      confirmDelete: false,
      levelMap: {
        5: "省",
        6: "市",
        7: "景点",
      },
      selected: [],
      filter: "",
      loading: false,
      serverPagination: {
        page: 1,
        rowsPerPage: 100,
        rowsNumber: 10, // specifying this determines pagination is server-side
      },
      productskus: [],
      columns: [
        {
          name: "destinationId",
          required: true,
          label: "目的地ID",
          align: "left",
          field: "destinationId",
          sortable: false,
          style:
            "max-width: 170px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "destinationName",
          required: true,
          label: "目的地名",
          align: "left",
          field: "destinationName",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "level",
          required: true,
          label: "级别",
          align: "left",
          field: "level",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "description",
          required: false,
          label: "描述",
          align: "left",
          field: "description",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        { name: "action", label: "操作", field: "action" },
      ],
    };
  },
  mounted() {
    this.refreshTable();
  },
  methods: {
    chatGptGenerateDesc(destId) {
      this.showLoading();
      api
        .put("/api/v1/destination/openai-gen-desc/" + destId)
        .then((response) => {
          console.log("generating description with chatgpt");
          console.log(response.data);
          if (response.data.ok === true) {
            this.showNotifyMessageSucceed(response.data.message);
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
          this.hideLoading();
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
          this.hideLoading();
        });
    },
    goDestinationDetail(val) {
      this.$refs.destinationCreateDialgRef.toggleEditMode(val);
    },
    executeDelete() {
      console.log("to delete selected:");
      console.log(this.selected);

      var listOfId2Del = [];
      for (var i in this.selected) {
        listOfId2Del.push(this.selected[i].destinationId);
      }

      var storeThis = this;
      var params = {};
      params.toDelete = listOfId2Del;
      api
        .delete("/api/v1/destination/batch", { data: params })
        .then((response) => {
          this.showNotifyMessageSucceed(response.data.message);
          this.refreshTable();
          this.selected = [];
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
    refreshTable() {
      this.$refs.destinationTableRef.requestServerInteraction();
    },
    goDelete() {
      if (this.selected.length < 1) {
        this.showNotifyMessageFail("没有选中任何项");
        return;
      }
      this.confirmDelete = true;
    },
    newDestination() {
      this.$refs.destinationCreateDialgRef.toggleDialog();
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
      // -1 represents all level
      params.level = -1;
      api
        .get("/api/v1/destination", {
          params: params,
        })
        .then((response) => {
          console.log("response data for destination");
          console.log(response.data);
          this.destinations = response.data.data.data;
          console.log("destinations: ");
          console.log(this.destinations);
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
  },
};
</script>
