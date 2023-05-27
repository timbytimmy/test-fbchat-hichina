<template>
  <q-page padding>
    <q-dialog v-model="confirmDraft" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="signal_wifi_off" color="primary" text-color="white" />
          <span class="q-ml-sm">确定要将所选放入草稿吗？</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn
            flat
            label="确认放入草稿"
            color="primary"
            @click="executeDraft"
            v-close-popup
          />
        </q-card-actions>
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
    <div class="row justify-center q-pa-md">
      <q-table
        ref="blogTableRef"
        style="max-width: 98%; min-width: 90%"
        title="博文管理"
        :rows="blogs"
        :columns="columns"
        v-model:pagination="serverPagination"
        :loading="loading"
        :filter="filter"
        row-key="blogId"
        :separator="'cell'"
        @request="onRequest"
        selection="multiple"
        v-model:selected="selected"
      >
        <template v-slot:top-right>
          <q-btn
            style="margin-right: 50px"
            round
            color="red"
            @click="goDelete"
            icon="delete"
          />
          <q-btn
            style="margin-right: 50px"
            round
            color="orange"
            @click="goDraft"
            icon="drafts"
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
      </q-table>
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
export default {
  name: "BlogManagePage",
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
  mounted() {
    this.$refs.blogTableRef.requestServerInteraction();
  },
  methods: {
    executeDraft() {
      console.log(this.selected);

      var listOfId2Draft = [];
      for (var i in this.selected) {
        listOfId2Draft.push(this.selected[i].blogId);
      }

      var params = {};
      params.toDraft = listOfId2Draft;
      api
        .put("/api/v1/blog/batchDraft", params)
        .then((response) => {
          this.showNotifyMessageSucceed(response.data.message);
          this.$refs.blogTableRef.requestServerInteraction();
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    executeDelete() {
      console.log("to delete selected:");
      console.log(this.selected);

      var listOfId2Del = [];
      for (var i in this.selected) {
        listOfId2Del.push(this.selected[i].blogId);
      }

      var params = {};
      params.toDelete = listOfId2Del;
      api
        .delete("/api/v1/blog/batch", { data: params })
        .then((response) => {
          this.showNotifyMessageSucceed(response.data.message);
          this.$refs.blogTableRef.requestServerInteraction();
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
    goDraft() {
      if (this.selected.length < 1) {
        this.showNotifyMessageFail("没有选中任何项");
        return;
      }
      this.confirmDraft = true;
    },
    onRequest(props) {
      const { page, rowsPerPage } = props.pagination;
      const filter = props.filter;
      this.loading = true;

      var params = {};
      params.page = page;
      params.pageSize = rowsPerPage;
      params.query = filter;
      api
        .get("/api/v1/blog/list", {
          params: params,
        })
        .then((response) => {
          this.blogs = response.data.data.data;
          this.serverPagination.page = response.data.data.currentPage;
          this.serverPagination.rowsNumber = response.data.data.total;
          this.serverPagination.rowsPerPage = response.data.data.pageSize;

          this.loading = false;
        })
        .catch((e) => {
          console.log(e);
          this.showNotifyMessageFail(e.response);
        });
    },
  },
  data() {
    return {
      confirmDraft: false,
      confirmDelete: false,
      selected: [],
      loading: false,
      filter: "",
      blogs: [],
      serverPagination: {
        page: 1,
        rowsPerPage: 100,
        rowsNumber: 10, // specifying this determines pagination is server-side
      },
      columns: [
        {
          name: "blogId",
          required: true,
          label: "博客ID",
          align: "left",
          field: "blogId",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "title",
          required: true,
          label: "标题",
          align: "left",
          field: "title",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "createdTime",
          required: true,
          label: "创建时间",
          align: "left",
          field: "createdTime",
          sortable: false,
          style:
            "max-width: 120px;min-width: 120px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "draft",
          required: true,
          label: "是否草稿状态",
          align: "left",
          field: "draft",
          sortable: false,
          style:
            "max-width: 120px;min-width: 120px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
      ],
    };
  },
};
</script>
