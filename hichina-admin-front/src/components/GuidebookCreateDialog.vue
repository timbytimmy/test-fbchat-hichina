<template>
  <div>
    <q-dialog
      v-model="dialogOpen"
      :maximized="maximizedToggle"
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
          <div class="text-h6">创建攻略书</div>
        </q-card-section>

        <q-card-section>
          <div style="width: 100vw" class="row justify-center">
            <div class="col-8">
              <q-input
                outlined
                class="col-8"
                label="头图URL"
                v-model="coverImageUrl"
              />
            </div>
            <div class="col-8">
              <img
                v-if="coverImageUrl != ''"
                style="height: 240px; width: 290px"
                :src="coverImageUrl"
              />
            </div>
            <div class="col-8">
              <q-input
                outlined
                class="col-8"
                label="下载地址"
                v-model="downloadUrl"
              />
            </div>
            <div class="col-8">
              <q-select
                outlined
                use-input
                input-debounce="100"
                @filter="filterFn"
                v-model="bindedDestination"
                :options="destinations"
                label="目的地"
              />
            </div>
            <div class="col-8">
              <q-input
                v-model="shortDescription"
                filled
                label="简述"
                type="textarea"
              />
            </div>
            <div class="col-8">
              <q-btn
                v-if="mode != 'edit'"
                label="提交"
                @click="submitNewGuidebook"
                color="primary"
              />
              <q-btn
                v-if="mode == 'edit'"
                label="更新"
                @click="submitUpdateGuidebook"
                color="primary"
              />
            </div>
          </div>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
export default {
  name: "GuideBookCreateComponent",
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
    this.loadAllDestinationOption();
  },
  methods: {
    filterFn(val, update) {
      if (val === "") {
        update(() => {
          this.destinations = this.destinationsbak;
        });
        return;
      }

      update(() => {
        const needle = val.toLowerCase();
        this.destinations = this.destinationsbak.filter(
          (v) => v.label.toLowerCase().indexOf(needle) > -1
        );
      });
    },
    loadAllDestinationOption() {
      var params = {};
      params.page = -1;
      params.pageSize = 100;
      params.query = "";
      params.level = -1;
      var storeThis = this;
      api
        .get("/api/v1/destination", {
          params: params,
        })
        .then((response) => {
          this.destinations = [];
          for (var i in response.data.data.data) {
            var obj = {};
            obj.label = response.data.data.data[i].destinationName;
            obj.value = response.data.data.data[i].destinationId;
            this.destinations.push(obj);
          }
          this.destinationsbak = this.destinations;
          console.log("parent destination candidates:");
          console.log(this.destinations);
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    clearForm() {
      this.coverImageUrl = "";
      this.downloadUrl = "";
      this.shortDescription = "";
      this.bindedDestination = "";
    },
    validateParams() {
      if (this.coverImageUrl == null || this.coverImageUrl == "") {
        this.showNotifyMessageFail("封面图不得为空");
        return false;
      }
      if (this.downloadUrl == null || this.downloadUrl == "") {
        this.showNotifyMessageFail("下载链接不得为空");
        return false;
      }
      if (this.bindedDestination == null || this.bindedDestination == "") {
        this.showNotifyMessageFail("目的地不得为空");
        return false;
      }

      if (this.shortDescription == null || this.shortDescription == "") {
        this.showNotifyMessageFail("简述不得为空");
        return false;
      }
      return true;
    },
    submitUpdateGuidebook() {
      var params = {};
      params.destinationId = this.bindedDestination.value;
      params.coverImageUrl = this.coverImageUrl;
      params.shortDescription = this.shortDescription;
      params.downloadUrl = this.downloadUrl;
      if (!this.validateParams()) {
        return;
      }
      api
        .put("/api/v1/guidebook/" + this.focusedGuideId, params)
        .then((response) => {
          console.log(response.data);
          if (response.data.ok === true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.dialogOpen = false;
            this.$emit("close-me-event");
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
    submitNewGuidebook() {
      var params = {};
      params.destinationId = this.bindedDestination.value;
      params.coverImageUrl = this.coverImageUrl;
      params.shortDescription = this.shortDescription;
      params.downloadUrl = this.downloadUrl;
      if (!this.validateParams()) {
        return;
      }
      api
        .post("/api/v1/guidebook", params)
        .then((response) => {
          console.log(response.data);
          if (response.data.ok === true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.dialogOpen = false;
            this.$emit("close-me-event");
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
    toggleDialog() {
      this.mode = "";
      this.clearForm();
      this.dialogOpen = true;
    },
    toggleDialogEditMode(guideId) {
      this.clearForm();
      this.dialogOpen = true;
      this.focusedGuideId = guideId;
      this.loadDetailByGuideId(guideId);
      this.mode = "edit";
    },
    loadDetailByGuideId(guideId) {
      var storeThis = this;
      api
        .get("/api/v1/guidebook/" + guideId)
        .then((response) => {
          if (response.data.ok === true) {
            console.log("guide book detail: ");
            console.log(response.data.data);
            this.coverImageUrl = response.data.data.coverImageUrl;
            this.downloadUrl = response.data.data.downloadUrl;
            this.bindedDestination = response.data.data.destinationId;

            this.bindedDestination = response.data.data.destinationId;
            var filteredDestinations = this.destinationsbak.filter((item) => {
              return item.value == response.data.data.destinationId;
            });
            this.bindedDestination = filteredDestinations[0];

            this.shortDescription = response.data.data.shortDescription;
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
  },
  data() {
    return {
      focusedGuideId: "",
      mode: "",
      shortDescription: "",
      downloadUrl: "",
      destinations: [],
      destinationsbak: [],
      bindedDestination: "",
      coverImageUrl: "",
      maximizedToggle: true,
      dialogOpen: false,
    };
  },
};
</script>
