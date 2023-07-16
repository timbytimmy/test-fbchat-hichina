<template>
  <q-page>
    <div id="container" style="width: 100%" ref="pay"></div>
  </q-page>
</template>

<script>
import { onMounted, nextTick, ref } from "vue";
import { orderPaymentParamStore } from "stores/orderPaymentParamStore";
export default {
  name: "AlipayPage",
  setup() {
    const oStore = orderPaymentParamStore();

    const codeUrl = ref("");
    const orderId = ref("");
    const price = ref("");

    const htmlContent = ref("");

    const renderComponent = ref(true);

    function b64_to_utf8(str) {
      return window.atob(str);
    }

    const forceRerender = async () => {
      renderComponent.value = false;
      // Wait for the change to get flushed to the DOM
      await nextTick();

      // Add the component back in
      renderComponent.value = true;
    };

    onMounted(async () => {
      var allParamsFromPreviousPage = oStore.getPaymentDetail;
      console.log("what I got from book form:");
      console.log(allParamsFromPreviousPage);
      codeUrl.value = allParamsFromPreviousPage.codeUrl;
      console.log("decoded");
      htmlContent.value = b64_to_utf8(codeUrl.value);
      console.log(htmlContent.value);
      orderId.value = allParamsFromPreviousPage.orderId;
      price.value = allParamsFromPreviousPage.price;

      // forceRerender();
      var container = document.getElementById("container");
      container.innerHTML += htmlContent.value;
      // container.innerHTML += "<p>this is a test text</p>";

      // document.write(htmlContent.value);

      forceRerender();
    });

    return {
      renderComponent,
      htmlContent,
    };
  },
};
</script>
