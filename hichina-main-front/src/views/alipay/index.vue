<template>
    <div v-if="renderComponent" v-html="htmlContent" ref="pay"></div>
</template>
  
  <script setup>
    import { onMounted, ref } from "vue";
    const route = useRoute();

    const codeUrl = ref('')
    const orderId = ref('')
    const price = ref('')

    const pay = ref(null)

    const renderComponent= ref(true)

    const forceRerender = async () => {
        renderComponent.value = false;
        // Wait for the change to get flushed to the DOM
        await nextTick();

        // Add the component back in
        renderComponent.value = true;
      };

    const htmlContent = ref('')

    function b64_to_utf8(str) {
      return window.atob(str);
    }

    onMounted(() => {
        console.log("what I got from book form:")
        console.log(route.params)
        codeUrl.value = route.params.codeUrl;
        console.log("decoded")
        htmlContent.value = b64_to_utf8(codeUrl.value)
        console.log(htmlContent.value)
        orderId.value = route.params.orderId;
        price.value = route.params.price;

        //forceRerender()
        document.write(htmlContent.value)
    })
  
  </script>
  
  <style scoped lang="scss">
    
  </style>