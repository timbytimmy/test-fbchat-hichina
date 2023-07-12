<template>
  <q-page>
    <div class="row" style="height: 50px; background-color: #e5f2fa">
      <div class="col-sm-1"></div>
      <div
        class="col-12 col-sm flex text-subtitle1"
        style="align-items: center"
      >
        Travel shop > {{ productName }}
      </div>
    </div>
    <div class="row text-weight-bold text-h5 q-pa-md">
      Product Name: {{ productName }}
    </div>
    <div class="text-body1 q-px-md">
      <div>Package Category: {{ packageCategory }}</div>
      <div v-if="selectedDate != null && selectedDate.length > 0">
        Start Date: {{ selectedDate }}
      </div>
      <div v-if="adultCount != null && adultCount > 0">
        Adults: {{ adultCount }}
      </div>
      <div v-if="childCount != null && childCount > 0">
        Child: {{ childCount }}
      </div>
      <div v-if="infantCount != null && infantCount > 0">
        Infant: {{ infantCount }}
      </div>
      <div v-if="buyCount != null && buyCount > 0">
        Purchase Count: {{ buyCount }}
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance, reactive } from "vue";
import { useQuasar } from "quasar";
import { useRoute } from "vue-router";
import { api } from "boot/axios";
import { bookParamStore } from "stores/bookParamStore";
export default {
  name: "BookPage",
  setup() {
    const store = bookParamStore();

    const instance = getCurrentInstance();
    const app = instance.appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const route = useRoute();

    const coupon = ref("");
    const country = ref("");
    const productName = ref("");
    const productTypeId = ref("");
    const productSkuId = ref("");
    const packageCategory = ref("");
    const selectedDate = ref(new Date());
    const adultCount = ref(0);
    const childCount = ref(0);
    const infantCount = ref(0);
    const buyCount = ref(1);
    const totalPrice = ref(0);

    const peopleform = reactive([]);
    const contactform = ref({});

    function whoami() {
      api
        .get("/api/v1/user/whoami")
        .then(function (response) {
          console.log("current user in book page: " + response.data);
        })
        .catch(function (error) {
          console.log("currently not logged in setup: " + error);
          gp.$goPage("/auth/login");
        });
    }
    onMounted(() => {
      whoami();

      console.log("allParamsFromPreviousPage");
      var allParamsFromPreviousPage = store.getOrderDetail;
      console.log(allParamsFromPreviousPage);
      productName.value = allParamsFromPreviousPage.productName;
      packageCategory.value = allParamsFromPreviousPage.packageCategory;
      selectedDate.value = allParamsFromPreviousPage.selectedDate;
      adultCount.value = allParamsFromPreviousPage.adultCount;
      childCount.value = allParamsFromPreviousPage.childCount;
      infantCount.value = allParamsFromPreviousPage.infantCount;
      buyCount.value = allParamsFromPreviousPage.buyCount;

      console.log("productName.value");
      console.log(productName.value);

      // peopleform.value = [];
      // contactform.value = {
      //   name: "",
      //   email: "",
      //   phone: "",
      //   address: "",
      // };
      // if (
      //   route.params.productName == null ||
      //   route.params.productName.length < 1 ||
      //   route.params.productSkuId == null ||
      //   route.params.productSkuId.length < 1 ||
      //   route.params.packageCategory == null ||
      //   route.params.packageCategory.length < 1
      // ) {
      //   gp.$goPage("/");
      //   return;
      // }
      // productName.value = route.params.productName;
      // packageCategory.value = route.params.packageCategory;
      // selectedDate.value = route.params.selectedDate;
      // totalPrice.value = route.params.totalPrice;
      // productSkuId.value = route.params.productSkuId;
      // adultCount.value =
      //   route.params.adultCount == null ? 0 : route.params.adultCount;
      // childCount.value =
      //   route.params.childCount == null ? 0 : route.params.childCount;
      // infantCount.value =
      //   route.params.infantCount == null ? 0 : route.params.infantCount;
      // buyCount.value =
      //   route.params.buyCount == null ? 0 : route.params.buyCount;

      // productTypeId.value = route.params.productTypeId;
      // for (
      //   let index = 0;
      //   index <
      //   parseInt(adultCount.value) +
      //     parseInt(childCount.value) +
      //     parseInt(infantCount.value);
      //   index++
      // ) {
      //   peopleform.value.push({
      //     surName: "",
      //     givenName: "",
      //     birthday: "",
      //     gender: "",
      //     nationality: "",
      //     passportNo: "",
      //     passportExpireDate: "",
      //   });
      // }
    });

    return {
      productName,
      packageCategory,
      selectedDate,
      adultCount,
      childCount,
      infantCount,
      buyCount,
    };
  },
};
</script>
