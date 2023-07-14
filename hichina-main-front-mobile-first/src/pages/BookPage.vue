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
          <q-date
            mask="YYYY-MM-DD"
            v-model="peopleform.value[index - 1]['birthday']"
          />
        </div>
        <div class="row q-mt-md">
          <q-select
            outlined
            dense
            emit-value
            map-options
            class="col-12 col-sm-6 col-md-4"
            v-model="peopleform.value[index - 1]['gender']"
            :options="genderOption"
            option-value="value"
            option-label="label"
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
          <q-date
            mask="YYYY-MM-DD"
            v-model="peopleform.value[index - 1]['passportExpireDate']"
          />
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
        @click="submitOrder('alipay')"
      />
      <q-btn
        icon="lab la-weixin"
        glossy
        color="green-6"
        label="Pay with Wechat Pay"
        class="q-ma-sm"
        @click="submitOrder('wechatpay')"
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
import { orderPaymentParamStore } from "stores/orderPaymentParamStore";

export default {
  name: "BookPage",
  setup() {
    const bStore = bookParamStore();
    const oStore = orderPaymentParamStore();

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

    const LOCALSPECIALTYPRODUCTTYPE = "fd264cab-ee8d-4571-a477-03d7e7c090b3";

    function checkPassenger(passenger) {
      if (
        gp.$checkEmpty(passenger.birthday) ||
        gp.$checkEmpty(passenger.gender) ||
        gp.$checkEmpty(passenger.givenName) ||
        gp.$checkEmpty(passenger.surName) ||
        gp.$checkEmpty(passenger.nationality) ||
        gp.$checkEmpty(passenger.passportExpireDate) ||
        gp.$checkEmpty(passenger.passportNo) ||
        gp.$checkEmpty(passenger.surName)
      ) {
        return false;
      }
      return true;
    }

    function validatePreOrderParams(finalParams) {
      console.log("validating finalParams");
      console.log(finalParams);
      // must have CNY value valid
      if (
        gp.$checkEmpty(finalParams.productInfo["totalPrice"]) ||
        !Number.isInteger(parseInt(finalParams.productInfo["totalPrice"]))
      ) {
        gp.$generalNotify($q, false, "something wrong with price");
        return false;
      }
      // must have full contact info
      if (
        gp.$checkEmpty(finalParams.contactInfo["email"]) ||
        gp.$checkEmpty(finalParams.contactInfo["phone"]) ||
        gp.$checkEmpty(finalParams.contactInfo["name"]) ||
        gp.$checkEmpty(finalParams.contactInfo["address"])
      ) {
        gp.$generalNotify(
          $q,
          false,
          "Please check your contact info is filled correctly"
        );
        return false;
      }
      // must have full product info
      if (gp.$checkEmpty(finalParams.productInfo)) {
        gp.$generalNotify(
          $q,
          false,
          "Please check your product info is correct"
        );
        return false;
      }
      // must have full passenger info if not localspecialty type, if no passenger , no check
      for (var index in finalParams.passengerInfo) {
        if (!checkPassenger(finalParams.passengerInfo[index])) {
          gp.$generalNotify(
            $q,
            false,
            "Please check your passenger info has been filled correctly"
          );
          return false;
        }
      }

      return true;
    }

    function submitOrder(paymethod) {
      if (paymethod == "paymethod") {
        alert("Feature not available yet, will be online soon!");
        return;
      }
      var finalParams = {};
      if (LOCALSPECIALTYPRODUCTTYPE != productTypeId.value) {
        finalParams.passengerInfo = peopleform.value;
      }
      finalParams.contactInfo = contactform.value;
      finalParams.coupon = coupon.value;
      finalParams.productInfo = bStore.getOrderDetail;
      console.log("finalParams");
      console.log(finalParams);
      if (validatePreOrderParams(finalParams)) {
        // do submit
        var params = {};
        params.meta = JSON.stringify(finalParams);
        params.skuId = productSkuId.value;
        params.status = "SUBMITTED";
        params.price = totalPrice.value;
        params.payMethod = paymethod;
        api
          .post("/api/v1/order", params)
          .then((res) => {
            gp.$generalNotify($q, true, "Succeed creating order");
            if (paymethod == "wechatpay") {
              // go to we chat pay page
              // go to alipay page
              console.log(res.data.data);
              var param2Pass = {};
              param2Pass.price = res.data.data.price;
              param2Pass.orderId = res.data.data.orderId;
              param2Pass.codeUrl = res.data.data.codeUrl;
              param2Pass.productName = productName.value;

              oStore.setPaymentDetail(param2Pass);
              gp.$goPage("/wechatpay");
            } else if (paymethod == "alipay") {
              // go to alipay page
              console.log(res.data.data);
              var param2Pass = {};
              param2Pass.price = res.data.data.price;
              param2Pass.orderId = res.data.data.orderId;
              param2Pass.codeUrl = res.data.data.codeUrl;

              oStore.setPaymentDetail(param2Pass);
              gp.$goPage("/alipay");
            }
          })
          .catch((err) => {
            gp.$generalNotify($q, false, "Fail creating order" + err);
          });
      }
    }

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
      return `${year}-${month}-${day}`;
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
      var allParamsFromPreviousPage = bStore.getOrderDetail;

      console.log("allParamsFromPreviousPage");
      console.log(allParamsFromPreviousPage);
      if (
        allParamsFromPreviousPage.productName == null ||
        allParamsFromPreviousPage.productName.length < 1 ||
        allParamsFromPreviousPage.productSkuId == null ||
        allParamsFromPreviousPage.productSkuId.length < 1 ||
        allParamsFromPreviousPage.packageCategory == null ||
        allParamsFromPreviousPage.packageCategory.length < 1
      ) {
        gp.$goPage("/");
        return;
      }

      productName.value = allParamsFromPreviousPage.productName;
      productSkuId.value = allParamsFromPreviousPage.productSkuId;
      packageCategory.value = allParamsFromPreviousPage.packageCategory;
      selectedDate.value = allParamsFromPreviousPage.selectedDate;
      productTypeId.value = allParamsFromPreviousPage.productTypeId;
      adultCount.value =
        allParamsFromPreviousPage.adultCount == null
          ? 0
          : allParamsFromPreviousPage.adultCount;
      childCount.value =
        allParamsFromPreviousPage.childCount == null
          ? 0
          : allParamsFromPreviousPage.childCount;
      infantCount.value =
        allParamsFromPreviousPage.infantCount == null
          ? 0
          : allParamsFromPreviousPage.infantCount;
      buyCount.value =
        allParamsFromPreviousPage.buyCount == null
          ? 0
          : allParamsFromPreviousPage.buyCount;
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
      submitOrder,
    };
  },
};
</script>
