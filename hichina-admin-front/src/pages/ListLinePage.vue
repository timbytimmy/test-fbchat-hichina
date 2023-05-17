<template>
  <q-page padding>
    <!-- content -->
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
        ref="tableRef"
        style="max-width: 98%; min-width: 90%"
        title="旅游线路"
        :rows="lines"
        :columns="columns"
        v-model:pagination="serverPagination"
        :loading="loading"
        :filter="filter"
        row-key="name"
        :separator="'cell'"
        @request="onRequest"
        selection="multiple"
        v-model:selected="selected"
      >
        <template v-slot:top-right>
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
        <template v-slot:body-cell-name="props">
          <q-td :props="props">
            {{ props.value }}
          </q-td>
        </template>
        <template v-slot:body-cell-public_id="props">
          <q-td :props="props">
            <router-link :to="`/edit-and-preview-line/${props.value}`">{{
              props.value
            }}</router-link>
          </q-td>
        </template>
        <template v-slot:body-cell-icon_path="props">
          <q-td :props="props">
            <img style="height: 50px; width: 50px" :src="props.value" />
          </q-td>
        </template>
        <template v-slot:body-cell-description="props">
          <q-td :props="props">
            {{ props.row.description }}
          </q-td>
        </template>
      </q-table>
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
import { myMixin } from "boot/commonFunc";
import { ref } from "vue";
export default {
  name: "ListLinePage",
  mixins: [myMixin],
  data() {
    return {
      confirmDelete: false,
      selected: [],
      loading: false,
      filter: "",
      lines: [],
      tableRef: ref(),
      columns: [
        {
          name: "name",
          required: true,
          label: "线路名",
          align: "left",
          field: "name",
          sortable: false,
          style:
            "max-width: 20vw;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "public_id",
          required: true,
          label: "线路id",
          align: "left",
          field: "public_id",
          sortable: false,
          style:
            "max-width: 15vw;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "icon_path",
          required: true,
          label: "图标",
          align: "left",
          field: "icon_path",
          sortable: false,
        },
        {
          name: "range_in_days",
          required: true,
          label: "天数",
          align: "left",
          field: "range_in_days",
          sortable: false,
        },
        {
          name: "base_price",
          required: true,
          label: "起步价",
          align: "left",
          field: "base_price",
          sortable: false,
        },
        {
          name: "description",
          required: true,
          label: "内容详情（缩略）",
          align: "left",
          field: "description",
          sortable: false,
          style:
            "max-width: 20vw;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
      ],
      serverPagination: {
        page: 1,
        rowsPerPage: 100,
        rowsNumber: 10, // specifying this determines pagination is server-side
      },
    };
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
  mounted() {
    this.$refs.tableRef.requestServerInteraction();
  },
  methods: {
    goDelete() {
      this.confirmDelete = true;
    },
    executeDelete() {
      console.log("really executing delete...");
      var listOfId2Del = [];

      for (var i in this.selected) {
        listOfId2Del.push(this.selected[i].public_id);
        console.log("things to del:");
        console.log(listOfId2Del);
      }
      var params = {};
      params.toDelete = listOfId2Del;
      api
        .delete("/api/v1/line/batch", { data: params })
        .then((response) => {
          //console.log(response.data.message)
          this.showNotifyMessageSucceed(response.data.message);
          this.$refs.tableRef.requestServerInteraction();
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });

      // refresh table
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
        .get("/api/v1/line", {
          params: params,
        })
        .then((response) => {
          console.log(response.data);
          this.lines = response.data.data.data;
          console.log("this is the lines I got:");
          console.log(this.lines);

          // this.selected = {};
          // for (var index in this.lines) {
          //   this.selected[this.lines[index].name] = false;
          // }
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
