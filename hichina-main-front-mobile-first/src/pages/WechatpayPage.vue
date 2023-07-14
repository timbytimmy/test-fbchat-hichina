<template>
  <q-page>
    <div class="row">
      <div class="text-center text-h5 col-12" style="background-color: #e7e7e7">
        Product Name: {{ productName }}
      </div>
    </div>
    <div class="row justify-center" style="min-height: 500px">
      <div
        class="column shadow-7 rounded-borders"
        style="
          position: relative;
          width: 340px;
          max-width: 95%;
          min-height: 450px;
          height: 600;
          background-color: yellow;
          background-image: url('https://photoprism.hichinatravel.com/api/v1/t/d6f91f961d96953e9a61f331f328468caddd05b7/32mcf2k4/fit_2048');
          background-size: cover;
        "
      >
        <div class="col-2"></div>
        <div class="col-2 row justify-center">
          <div class="text-left col-7">Total Price: {{ price }}</div>
        </div>
        <div class="col row justify-center" v-if="renderComponent">
          <div class="col-7">
            <QRCodeVue3 :width="220" :height="220" :value="htmlContent" />
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import QRCodeVue3 from "qrcode-vue3";
import { onMounted, ref, nextTick, getCurrentInstance } from "vue";
import { api } from "boot/axios";
import { orderPaymentParamStore } from "stores/orderPaymentParamStore";
import { useQuasar } from "quasar";
export default {
  name: "WechatpayPage",
  components: {
    QRCodeVue3: QRCodeVue3,
  },
  setup() {
    const instance = getCurrentInstance();
    const app = instance.appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const oStore = orderPaymentParamStore();

    const refreshIntervalId = ref("");

    const codeUrl = ref("");
    const orderId = ref("");
    const price = ref("");
    const productName = ref("");
    const htmlContent = ref("");
    const renderComponent = ref(true);

    const forceRerender = async () => {
      renderComponent.value = false;
      // Wait for the change to get flushed to the DOM
      await nextTick();

      // Add the component back in
      renderComponent.value = true;
    };

    function checkPaymentStatus() {
      api
        .get("/api/v1/order/wechatpaystatus/" + orderId.value)
        .then((response) => {
          console.log("status is:");
          console.log(response.data);
          if ("SUCCESS" == response.data) {
            // jump to my order page
            clearInterval(refreshIntervalId.value);
            gp.$goPage("/my-orders");
          } else {
            forceRerender();
          }
        })
        .catch((e) => {
          console.log("Error getting /api/v1/order/wechatpaystatus");
          console.log(e);
        });
    }

    function b64_to_utf8(str) {
      return window.atob(str);
    }

    onMounted(() => {
      var allParamsFromPreviousPage = oStore.getPaymentDetail;
      productName.value = allParamsFromPreviousPage.productName;
      if (productName.value === "") {
        gp.$goPage("/");
        return;
      }
      console.log("what I got from book form:");
      console.log(allParamsFromPreviousPage);
      codeUrl.value = allParamsFromPreviousPage.codeUrl;
      forceRerender();
      console.log("decoded");
      htmlContent.value = b64_to_utf8(codeUrl.value);
      console.log(htmlContent.value);
      orderId.value = allParamsFromPreviousPage.orderId;
      price.value = allParamsFromPreviousPage.price;
      refreshIntervalId.value = setInterval(() => {
        checkPaymentStatus();
      }, 3000);
    });
    return {
      htmlContent,
      productName,
      price,
      forceRerender,
    };
  },
};
</script>
