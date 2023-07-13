<template>
  <q-page>
    <div v-html="htmlContent" ref="pay"></div>
  </q-page>
</template>

<script>
import { onMounted, ref } from "vue";
import { orderPaymentParamStore } from "stores/orderPaymentParamStore";
export default {
  name: "AlipayPage",
  setup() {
    const oStore = orderPaymentParamStore();

    const codeUrl = ref("");
    const orderId = ref("");
    const price = ref("");

    const htmlContent = ref("");

    function b64_to_utf8(str) {
      return window.atob(str);
    }

    onMounted(() => {
      var allParamsFromPreviousPage = oStore.getPaymentDetail;
      console.log("what I got from book form:");
      console.log(allParamsFromPreviousPage);
      codeUrl.value = allParamsFromPreviousPage.codeUrl;
      console.log("decoded");
      htmlContent.value = b64_to_utf8(codeUrl.value);
      console.log(htmlContent.value);
      orderId.value = allParamsFromPreviousPage.orderId;
      price.value = allParamsFromPreviousPage.price;

      document.addEventListener("DOMContentLoaded", function () {
        document.write(htmlContent.value);
      });
    });
  },
};
</script>
