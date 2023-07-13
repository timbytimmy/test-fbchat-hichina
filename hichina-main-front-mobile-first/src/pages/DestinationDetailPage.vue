<template>
  <q-page>
    <div class="row" style="height: 110px; background-color: #e5f2fa">
      <div class="col-sm-2"></div>
      <div class="col-12 col-sm-10 column">
        <div class="q-pt-md q-pl-md text-black text-weight-bold">
          Home > Destination > {{ destinationName }}
        </div>
        <div class="col text-weight-bold q-pl-md text-h3">
          {{ destinationName }}
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-12 col-sm-5 row q-px-md q-py-lg" style="height: 450px">
        <div class="col-12" style="background-color: white">
          <q-img :src="destinationProfileImage" fit="fill"> </q-img>
        </div>
      </div>
      <div class="col-12 col-sm-7 row q-px-md q-py-lg" style="height: 450px">
        <div class="column col-12">
          <div
            class="col-9 q-pa-md text-subtitle1"
            style="background-color: #fff9c6"
            v-html="description"
          ></div>
          <div class="col-3 row">
            <div class="col-6 text-h4 flex flex-center">
              Guidbook of {{ destinationName }}
            </div>
            <div class="col-6 flex flex-center">
              <q-btn class="glossy" rounded color="blue-6" @click="goDownload"
                ><q-icon left size="3em" name="download" />
                <div>Download</div></q-btn
              >
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="row flex flex-center text-h4"
      style="border-top: 1px solid gray; height: 80px"
    >
      Places to go in {{ destinationName }}
    </div>
    <div
      v-for="item in childDestinations"
      v-bind:key="item.destinationId"
      class="row q-pa-md"
      style="width: 90%; min-height: 200px; margin: 0 auto"
    >
      <div class="col-3 q-pa-md" style="border-right: 1px solid gray">
        <q-img fit="fill" :src="item.destinationProfileImage"></q-img>
      </div>
      <div class="col-9 column q-pa-md">
        <div class="text-h4 text-weight-bold">{{ item.destinationName }}</div>
        <div class="q-pt-md">
          {{ removeHtmlTag(item.description) }}
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance } from "vue";
import { api } from "boot/axios";
import { useRoute } from "vue-router";
import { useSeoMeta } from "unhead";
import { useQuasar } from "quasar";
export default {
  name: "DestinationDetailPage",
  setup() {
    const instance = getCurrentInstance();
    const app = getCurrentInstance().appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const route = useRoute();

    const description = ref("");
    const destinationProfileImage = ref("");
    const downloadUrl = ref("");
    const destinationName = ref("");
    const childDestinations = ref([]);
    const relevantToursProduct = ref([]);

    function goDownload() {
      if (downloadUrl.value == null || downloadUrl.value == "") {
        gp.$generalNotify(
          $q,
          false,
          "There is no guidebook for this destination"
        );

        return;
      } else {
        window.location.href = downloadUrl.value;
      }
    }

    function logPv() {
      api
        .post(
          "/api/public/pagestats/pv/destination-detail-" +
            route.params.destinationId
        )
        .then((res) => {
          console.log("log pv:");
          console.log(res.data);
        })
        .catch((err) => {
          console.error("Error:", err);
        });
    }
    function logView() {
      api
        .post(
          "/api/public/pagestats/view-destination/" + route.params.destinationId
        )
        .then((res) => {
          console.log("view cnt of this destination:");
          console.log(res.data);
        })
        .catch((err) => {
          console.error("Error:", err);
        });
    }

    function loadRelatedTours() {
      api
        .get(
          "/api/public/destination/relavanttourproduct/" +
            route.params.destinationId
        )
        .then((response) => {
          if (response.data.ok === true) {
            relevantToursProduct.value = response.data.data;
          } else {
            relevantToursProduct.value = [];
          }
        })
        .catch((e) => {
          console.log("get relevant tours product err");
          console.log(e);
        });
    }

    function loadChildrenDestination() {
      api
        .get("/api/public/destination/children/" + route.params.destinationId)
        .then((response) => {
          childDestinations.value = response.data.data;
        })
        .catch((e) => {
          console.log("get destination detail err");
          console.log(e);
        });
    }

    function loadDestinations() {
      api
        .get("/api/public/destination/" + route.params.destinationId)
        .then((response) => {
          description.value = response.data.data.description;
          destinationProfileImage.value = gp.$normalizeMultiImageUrl(
            response.data.data.destinationProfileImage
          );
          console.log(destinationProfileImage.value);
          downloadUrl.value = response.data.data.downloadUrl;
          destinationName.value = response.data.data.destinationName;
          useSeoMeta({
            title: destinationName.value,
            description: description.value,
            ogDescription: description.value,
            ogTitle: destinationName.value,
            ogImage:
              "https://www.hichinatravel.com/static/png/name-67280b81.png",
            twitterCard: "summary_large_image",
          });
        })
        .catch((e) => {
          console.log("get destination detail err");
          console.log(e);
        });
    }

    onMounted(() => {
      logPv();
      logView();
      console.log("on mounted destination detail page");
      loadDestinations();
      loadChildrenDestination();
      loadRelatedTours();
    });
    return {
      destinationName,
      destinationProfileImage,
      description,
      childDestinations,
      goDownload,
    };
  },
};
</script>
