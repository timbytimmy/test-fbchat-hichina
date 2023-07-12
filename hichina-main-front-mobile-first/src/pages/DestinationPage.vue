<template>
  <q-page>
    <div class="row justify-center text-blue-6 text-h4 q-pt-xl">
      Seasonal Recommendation
    </div>
    <div class="row justify-center q-mt-md">
      <q-input
        class="col-12 col-sm-8 col-md-6"
        rounded
        dense
        outlined
        v-model="query"
        @update:model-value="(val) => updateQuery(val)"
        label="Search by title"
      />
    </div>
    <div class="row justify-left">
      <div
        class="col-12 col-sm-4 col-md-3"
        v-for="item in globalDestinationCards"
        :key="item.destinationId"
      >
        <div class="q-pa-md">
          <q-card
            class="destination-card cursor-pointer rounded-borders"
            @mouseenter="hoverFlag = true"
            @mouseleave="hoverFlag = true"
            @click="goPage('/destination-detail/' + item.destinationid)"
          >
            <q-img
              class="rounded-borders"
              style="height: 100%"
              :src="normalizeMultiImageUrl(item.destinationProfileImage)"
            >
              <div class="absolute-bottom">
                <div class="text-h6 text-white">
                  <a
                    style="color: inherit"
                    :href="'./destination-detail/' + item.destinationId"
                    >{{ item.destinationName }}</a
                  >
                </div>
                <div class="text-subtitle2">
                  {{ item.parentDestinationName }}
                </div>
              </div>
            </q-img>
          </q-card>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-12">
        <p class="text-center" style="background-color: #b4b4b4">
          Scroll to load more
        </p>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted } from "vue";
import { api } from "boot/axios";
import { debounce } from "lodash";
export default {
  name: "DestinationPage",
  setup() {
    const pageSize = ref(44);
    const currentPage = ref(1);
    const destinationCards = ref([]);
    const globalDestinationCards = ref([]);
    const totalDestinationCount = ref(-1);
    const query = ref("");
    const hoverFlag = ref(true);

    function loadMore() {
      currentPage.value += 1;

      var maxPage = totalDestinationCount.value / pageSize.value;
      if (currentPage.value <= maxPage + 1) {
        loadDestinations();
      }
    }

    function getNextBatch() {
      window.onscroll = debounce(function () {
        let bottomOfWindow =
          document.documentElement.scrollTop + window.innerHeight + 100 >
          document.documentElement.scrollHeight;

        if (bottomOfWindow) {
          loadMore();
        }
      }, 500);
    }

    const updateQuery = debounce((value) => {
      globalDestinationCards.value = [];
      currentPage.value = 1;
      loadDestinations();
    }, 500);

    function loadDestinations() {
      destinationCards.value = [];
      var params = {};
      params.pageSize = pageSize.value;
      console.log("loading page: " + currentPage.value);
      params.page = currentPage.value;
      params.query = query.value;
      api
        .get("/api/public/destination/list", { params: params })
        .then(function (response) {
          destinationCards.value = response.data.data.data;
          totalDestinationCount.value = response.data.data.total;
          globalDestinationCards.value = globalDestinationCards.value.concat(
            destinationCards.value
          );
        })
        .catch(function (error) {
          console.log(error);
        });
    }
    onMounted(() => {
      loadDestinations();
      getNextBatch();
    });

    return {
      query,
      globalDestinationCards,
      hoverFlag,
      updateQuery,
    };
  },
};
</script>
<style lang="sass" scoped>
.destination-card
  width: 100%
  max-width: 550px
  height: 300px
</style>
