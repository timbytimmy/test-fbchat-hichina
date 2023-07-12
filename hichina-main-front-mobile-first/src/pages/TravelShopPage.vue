<template>
  <q-page>
    <div
      class="row justify-center"
      style="height: 376px; background-color: #2a82e4; position: relative"
    >
      <div
        class="col-12 col-sm-9 col-md-7 flex flex-center row shadow-7"
        style="
          background-color: white;
          height: 180px;
          position: absolute;
          top: 120px;
          border-radius: 10px;
        "
      >
        <q-input
          class="col-10"
          rounded
          outlined
          v-model="query"
          @update:model-value="(val) => updateQuery(val)"
          label="Search by title"
        />
      </div>
      <q-btn-toggle
        class="col-11 col-sm-8 col-md-6 row shadow-7"
        style="
          background-color: white;
          height: 95px;
          position: absolute;
          top: 70px;
          border-radius: 20px;
        "
        v-model="btnToggle"
        toggle-color="primary"
        :options="[
          { label: 'Group Tour', value: 'groupTour', icon: 'group' },
          { label: 'Hotel Deals', value: 'hotelDeals', icon: 'business' },
          { label: 'Flight Deals', value: 'flightDeals', icon: 'flight' },
          {
            label: 'Holiday Package',
            value: 'holidayPackage',
            icon: 'beach_access',
          },
          { label: 'China Stuff', value: 'chinaStuff', icon: 'shopping_cart' },
        ]"
      />
    </div>
    <div class="row" style="height: 50px"></div>
    <div class="row justify-center text-blue text-h4 q-mt-xl">
      Deals for you
    </div>
    <div class="row justify-left">
      <div
        class="col-12 col-sm-6 col-md-3"
        v-for="(item, index) in globalProductList"
        v-bind:key="index"
      >
        <div class="q-pa-md">
          <q-card
            @click="goPage('/product-detail/' + item.skuGroupId)"
            class="cursor-pointer"
            flat
            bordered
          >
            <q-img
              :src="item.imageUrl"
              placeholder-src="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/tile_224"
            />

            <q-card-section>
              <div class="text-subtitle1 text-pink">
                Start from ￥{{ item.minPrice }}
              </div>
              <div class="text-h5 q-mt-sm q-mb-xs">
                <a :href="'./product-detail/' + item.skkuGroupId">{{
                  item.skuGroupName
                }}</a>
              </div>
            </q-card-section>
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
import { debounce } from "lodash";
import { api } from "boot/axios";
export default {
  name: "TravelShopPage",
  setup() {
    const btnToggle = ref("groupTour");
    const query = ref("");
    const globalProductList = ref([]);
    const currentPage = ref(1);
    const pageSize = ref(100);
    const productTypeId = ref("");
    const productList = ref([]);

    const totalProductCount = ref(-1);

    const TOURPACKAGETYPE = "3a53caed-b788-4290-896d-7922532ad769";
    const HOTELPACKAGETYPE = "a9f5adbe-c09b-49bc-a614-8a1c5d5e5337";
    const FLIGTHPACKAGETYPE = "d7b95089-e278-4522-8f71-dacac41ba32f";
    const HOLIDAYPACKAGETYPE = "e05d07a3-a717-45b8-b009-47a349890e41";
    const CHINALOCALPACKAGETYPE = "fd264cab-ee8d-4571-a477-03d7e7c090b3";

    const updateQuery = debounce((value) => {
      console.log(value);
      globalProductList.value = [];
      currentPage.value = 1;
      loadAllProducts();
    }, 500);

    function loadAllProducts() {
      var params = {};
      params.pageSize = pageSize.value;
      params.page = currentPage.value;
      params.query = query.value;
      params.productTypeId = productTypeId.value;
      api
        .get("/api/public/productsku/productskugrouplist", { params: params })
        .then(function (response) {
          console.log("response.data.data");
          console.log(response.data.data);
          productList.value = response.data.data.data;
          if (totalProductCount.value == -1) {
            totalProductCount.value = response.data.data.total;
          }
          globalProductList.value = globalProductList.value.concat(
            productList.value
          );
          console.log("globalProductList....");
          console.log(globalProductList.value);
        })
        .catch(function (error) {
          console.log(error);
        });
    }

    function setActiveTab(index) {
      globalProductList.value = [];
      currentPage.value = 1;
      console.log("activating..." + index);
      btnToggle.value = index;
      if (btnToggle.value == "groupTour") {
        productTypeId.value = TOURPACKAGETYPE;
      } else if (btnToggle.value == "hotelDeals") {
        productTypeId.value = HOTELPACKAGETYPE;
      } else if (btnToggle.value == "flightDeals") {
        productTypeId.value = FLIGTHPACKAGETYPE;
      } else if (btnToggle.value == "holidayPackage") {
        productTypeId.value = HOLIDAYPACKAGETYPE;
      } else if (btnToggle.value == "chinaStuff") {
        productTypeId.value = CHINALOCALPACKAGETYPE;
      } else {
        productTypeId.value = "";
      }
      loadAllProducts();
    }

    function loadMore() {
      currentPage.value += 1;
      var maxPage = totalProductCount.value / pageSize.value;
      if (currentPage.value <= maxPage + 1) {
        loadAllProducts();
      } else {
        console.log("Notify no more pages");
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

    function logPv() {
      api
        .post("/api/public/pagestats/pv/product")
        .then((res) => {
          console.log("log pv:");
          console.log(res.data);
        })
        .catch((err) => {
          console.error("Error:", err);
        });
    }

    onMounted(() => {
      logPv();
      setActiveTab("groupTour");
      getNextBatch();
    });

    return {
      query,
      globalProductList,
      updateQuery,
    };
  },
};
</script>
<style lang="sass" scoped>
</style>

