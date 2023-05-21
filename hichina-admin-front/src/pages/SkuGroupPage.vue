<template>
  <q-page padding>
    <q-dialog v-model="popupBindDestinationWindow">
      <q-card>
        <q-card-section>
          <div class="text-h6">产品-目的地关联</div>
        </q-card-section>
        <q-card-section>
          <div class="text-h6">
            <q-input
              outlined
              @update:model-value="(val) => goFilterDestination(val)"
              :debounce="500"
              v-model="filterDestination"
              label="Filter Destination"
            />
          </div>
        </q-card-section>

        <q-separator />

        <div style="width: 50vw; height: 70vh; display: flex">
          <div style="width: 38%; max-width: 38%; height: 100%">
            <label style="color: red; font-weight: bold">候选目的地</label>
            <q-list bordered separator>
              <q-item
                clickable
                v-ripple
                :active="item.destinationId === selected_left_destination"
                @click="setActiveSelectLeft(item.destinationId)"
                active-class="bg-teal-1 text-grey-8"
                v-for="item in candidateDestinations"
                v-bind:key="item.destinationId"
              >
                <q-item-section>
                  <q-item-label
                    >目的地: {{ item.destinationName }}</q-item-label
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
                @click="doBind"
              />
            </div>
            <div>
              <q-btn
                style="margin-top: 10px"
                round
                color="secondary"
                @click="unBind"
                icon="navigate_before"
              />
            </div>
          </div>
          <div style="width: 38%; height: 100%">
            <label style="color: red; font-weight: bold">已关联目的地</label>
            <q-list bordered separator>
              <q-item
                clickable
                v-ripple
                :active="item.destinationId === selected_right_destination"
                @click="setActiveSelectRight(item.destinationId)"
                active-class="bg-teal-1 text-grey-8"
                v-for="item in bindedDestinations"
                v-bind:key="item.destinationId"
                ou
              >
                <q-item-section>
                  <q-item-label
                    >目的地： {{ item.destinationName }}</q-item-label
                  >
                </q-item-section>
              </q-item>
            </q-list>
          </div>
        </div>
      </q-card>
    </q-dialog>
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
            <q-btn
              color="orange"
              dense
              label="绑定目的地"
              @click="bindDestination(props.row.skuGroupId)"
            />
            <q-btn
              v-if="props.row.enabled == false"
              color="blue-grey-3"
              style="cursor: pointer"
              @click="gotoProductDetail(props.row.skuGroupId)"
              type="button"
              ><a>前往产品</a></q-btn
            >
            <q-btn
              v-if="props.row.enabled == true"
              color="green"
              style="cursor: pointer"
              @click="gotoProductDetail(props.row.skuGroupId)"
              type="button"
              ><a>前往产品</a></q-btn
            >
          </q-td>
        </template>
      </q-table>
    </div></q-page
  >
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
    this.loadMainsiteBaseUrl();
    this.$refs.skuGroupTableRef.requestServerInteraction();
  },
  methods: {
    loadMainsiteBaseUrl() {
      api
        .get("/api/public/mainsitebaseurl")
        .then((response) => {
          console.log("main site base:");
          this.mainsiteBaseUrl = response.data;
          console.log(response.data);
        })
        .catch((e) => {
          console.log(e);
          this.showNotifyMessageFail(e.response);
        });
    },
    unBind() {
      if (!this.selected_right_destination?.trim()) {
        alert("Haven't selected anything");
        return;
      }
      console.log("unbind: " + this.selected_right_destination);
      var params = {};
      params.skuGroupId = this.currentSkuGroupId;
      params.destinationId = this.selected_right_destination;
      api
        .delete("/api/v1/destination/unbind-skugroup-destination", {
          data: params,
        })
        .then((response) => {
          if (response.data.ok == true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.loadBindedDestinations(this.currentSkuGroupId);
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
          this.selected_right_destination = "";
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    doBind() {
      if (!this.selected_left_destination?.trim()) {
        alert("Haven't selected anything");
        return;
      }

      var params = {};
      params.skuGroupId = this.currentSkuGroupId;
      params.destinationId = this.selected_left_destination;
      api
        .post("/api/v1/destination/bind-skugroup-destination", params)
        .then((response) => {
          if (response.data.ok == true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.loadBindedDestinations(this.currentSkuGroupId);
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
          this.selected_left_destination = "";
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    setActiveSelectRight(val) {
      this.selected_right_destination = val;
    },
    setActiveSelectLeft(val) {
      this.selected_left_destination = val;
    },
    goFilterDestination(val) {
      console.log(val);
      var params = {};
      params.query = val;
      api
        .get("/api/v1/destination/filter-short", {
          params: params,
        })
        .then((response) => {
          if (response.data.ok == true) {
            this.candidateDestinations = response.data.data;
          } else {
            this.candidateDestinations = [];
          }
        })
        .catch((e) => {
          console.log(e);
          this.showNotifyMessageFail(e.response);
        });
    },
    loadBindedDestinations(skuGroupId) {
      console.log(
        "loading already binded destination for product sku group:" + skuGroupId
      );
      // after this, should have bindedDestinations ready, once loaded
      api
        .get("/api/v1/destination/sku-group-binded/" + this.currentSkuGroupId)
        .then((response) => {
          console.log("get all binded:");
          console.log(response.data.data);
          if (response.data.ok === true) {
            this.bindedDestinations = response.data.data;
          } else {
            this.bindedDestinations = [];
          }
        })
        .catch((e) => {
          console.log(e);
          this.showNotifyMessageFail(e.response);
        });
    },
    bindDestination(skuGroupId) {
      this.currentSkuGroupId = skuGroupId;
      this.selected_left_destination = "";
      this.selected_right_destination = "";
      this.filterDestination = "";
      this.candidateDestinations = [];
      this.loadBindedDestinations(skuGroupId);
      this.popupBindDestinationWindow = true;
    },
    gotoProductDetail(text) {
      var goto = this.mainsiteBaseUrl + "/product-detail/" + text;
      if (goto?.trim()) {
        window.location.href = goto;
      }
    },
    copyText(text, goto) {
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
          // console.log("this is the group stats I got:");
          // console.log(this.skuGroups);

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
      mainsiteBaseUrl: "",
      bindedDestinations: [],
      currentSkuGroupId: "",
      selected_left_destination: "",
      selected_right_destination: "",
      filterDestination: "",
      popupBindDestinationWindow: false,
      candidateDestinations: [],
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
