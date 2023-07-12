<template>
  <q-page> book page </q-page>
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

      console.log("got parmas from previous page for store pinia");
      console.log(store.getOrderDetail);

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
  },
};
</script>
