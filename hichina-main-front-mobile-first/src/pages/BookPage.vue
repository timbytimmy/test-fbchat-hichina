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
    <div class="row">
      <div
        class="q-pa-md col-12"
        v-for="index in parseInt(adultCount) +
        parseInt(childCount) +
        parseInt(infantCount)"
        :key="index"
      >
        <div class="text-weight-bold text-h6">Traveler: {{ index }}</div>
        <div class="row">
          <q-input
            class="col-12 col-sm-6 col-md-4"
            rounded
            dense
            outlined
            v-model="peopleform.value[index - 1]['surName']"
            label="Surname*"
            :rules="[(val) => !!val || 'Field is required']"
          />
        </div>
        <div class="row">
          <q-input
            class="col-12 col-sm-6 col-md-4"
            rounded
            dense
            outlined
            v-model="peopleform.value[index - 1]['givenName']"
            label="Given Name*"
            :rules="[(val) => !!val || 'Field is required']"
          />
        </div>
        <div>
          <div class="q-pb-sm text-weight-bold">Birthday</div>
          <q-date v-model="peopleform.value[index - 1]['birthday']" />
        </div>
        <div class="row q-mt-md">
          <q-select
            outlined
            dense
            class="col-12 col-sm-6 col-md-4"
            v-model="peopleform.value[index - 1]['gender']"
            :options="genderOption"
            label="Gender"
          />
        </div>
        <div class="row q-mt-md">
          Nationality:
          <country-select
            v-model="peopleform.value[index - 1]['nationality']"
            :country="country"
            topCountry="US"
          />
        </div>
        <div class="row q-mt-md">
          <q-input
            class="col-12 col-sm-6 col-md-4"
            rounded
            dense
            outlined
            v-model="peopleform.value[index - 1]['passportNo']"
            label="Passport Number*"
            :rules="[(val) => !!val || 'Field is required']"
          />
        </div>
        <div>
          <div class="q-pb-sm text-weight-bold">Expiry date of Passport</div>
          <q-date v-model="peopleform.value[index - 1]['passportExpireDate']" />
        </div>
      </div>
    </div>
    <div class="row text-h6 text-weight-bold q-pa-md">Contact Info</div>
    <div class="row">
      <q-input
        class="q-pa-md col-12 col-sm-6 col-md-4"
        dense
        outlined
        v-model="contactform['name']"
        label="Name*"
        :rules="[(val) => !!val || 'Field is required']"
      />
    </div>
    <div class="row">
      <q-input
        class="q-pa-md col-12 col-sm-6 col-md-4"
        dense
        outlined
        v-model="contactform['email']"
        label="Email Address*"
        :rules="[(val) => !!val || 'Field is required']"
      />
    </div>
    <div class="row">
      <q-input
        class="q-pa-md col-12 col-sm-6 col-md-4"
        dense
        outlined
        v-model="contactform['phone']"
        label="Phone Number*"
        :rules="[(val) => !!val || 'Field is required']"
      />
    </div>
    <div class="row">
      <q-input
        class="q-pa-md col-12 col-sm-6 col-md-4"
        dense
        outlined
        v-model="contactform['address']"
        label="Address in China*"
        :rules="[(val) => !!val || 'Field is required']"
      />
    </div>
    <div class="row text-h6 text-weight-bold q-pa-md">Coupon Code</div>
    <div class="row">
      <q-input
        class="q-px-md col-12 col-sm-6 col-md-4"
        dense
        outlined
        v-model="coupon"
        label="Have Coupon? Enter code here"
      />
    </div>
    <div class="row q-pa-md">
      <div class="row col-6">
        <div
          class="col-6 col-sm-4 text-blue-6 text-h5 q-py-md"
          style="border-bottom: 1px solid black"
        >
          Payment CNY:
        </div>
        <div class="col-6 col-sm-8 text-red text-h3 q-py-md">
          {{ totalPrice }}￥
        </div>
      </div>
    </div>
    <div class="row">
      <q-btn
        icon="lab la-alipay"
        glossy
        color="blue-6"
        label="Pay with Alipay"
        class="q-ma-sm"
        @click="payWithAlipay"
      />
      <q-btn
        icon="lab la-weixin"
        glossy
        color="green-6"
        label="Pay with Wechat Pay"
        class="q-ma-sm"
        @click="payWithWechatPay"
      />
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

    function todayString() {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, "0");
      const day = String(today.getDate()).padStart(2, "0");
      return `${year}/${month}/${day}`;
    }

    function initPeopleform() {
      peopleform.value = [];

      for (
        let index = 0;
        index <
        parseInt(adultCount.value) +
          parseInt(childCount.value) +
          parseInt(infantCount.value);
        index++
      ) {
        peopleform.value.push({
          surName: "",
          givenName: "",
          birthday: ref(todayString()),
          gender: "",
          nationality: "",
          passportNo: "",
          passportExpireDate: ref(todayString()),
        });
      }
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
      totalPrice.value = allParamsFromPreviousPage.totalPrice;

      console.log("productName.value");
      console.log(productName.value);

      initPeopleform();
      contactform.value = {
        name: "",
        email: "",
        phone: "",
        address: "",
      };
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
    });

    return {
      productName,
      packageCategory,
      selectedDate,
      adultCount,
      childCount,
      infantCount,
      buyCount,
      peopleform,
      genderOption: [
        { label: "Male", value: 1 },
        { label: "Female", value: 0 },
      ],
      country,
      contactform,
      coupon,
      totalPrice,
    };
  },
};
</script>
