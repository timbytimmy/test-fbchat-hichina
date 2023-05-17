<template>
    <div class="main-container" v-if="renderComponent">
      <div>Product Name: {{ productName }}</div>
      <div>Total Price: {{ price }}</div>
      <QRCodeVue3 :width="220"
        :height="220" class="headshot headshot-1"
        :value="htmlContent"
      />
    </div>
</template>
  
  <script setup>
    import {AXIOS} from '@/common/http-commons'
    import router from "../../router";
    import { onMounted, ref } from "vue";
    const route = useRoute();

    const codeUrl = ref('')
    const orderId = ref('')
    const price = ref('')
    const productName = ref('')

    const pay = ref(null)
    const refreshIntervalId = ref("")

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

    function checkPaymentStatus(){
      AXIOS.get('/api/v1/order/wechatpaystatus/'+orderId.value).then(response=>{
         console.log("status is:")
         console.log(response.data)
         if('SUCCESS'==response.data){
            // jump to my order page
            var params2pass = {}
            params2pass.active = 2
            clearInterval(refreshIntervalId.value);
            router.push({name: 'user', params: params2pass})
         }
      }).catch(e=>{
        console.log("Error getting /api/v1/order/wechatpaystatus")
        console.log(e)
      })
    }

    onMounted(() => {
      refreshIntervalId.value = setInterval(() => {
        checkPaymentStatus()
      }, 3000)

      productName.value = route.params.productName;
      if(productName.value===''){
          router.push({name: 'home'});
          return;
        }
        console.log("what I got from book form:")
        console.log(route.params)
        codeUrl.value = route.params.codeUrl;
        console.log("decoded")
        htmlContent.value = b64_to_utf8(codeUrl.value)
        console.log(htmlContent.value)
        orderId.value = route.params.orderId;
        price.value = route.params.price;
        

        
        //forceRerender()
    })
  
  </script>
  
  <style scoped lang="scss">
    .main-container{
      position: fixed;
      top: 20%;
      left: 50%;
      margin-top: -50px;
      margin-left: -100px;
    

    }
    .headshot-1 {
      border-radius: 30%;
      background-image: var(--img-url), linear-gradient(#f9f047, #0fd850);
    }

    .headshot {
      flex-shrink: 0;
      margin: 10px;
      height: calc(160px + 6vw);
      width: calc(160px + 6vw);
      border: calc(10px + 0.3vw) solid transparent;
      background-origin: border-box;
      background-clip: content-box, border-box;
      background-size: cover;
      box-sizing: border-box;
      box-shadow: 0 0 5px 5px rgba(0, 0, 0, 0.5);
    }
  </style>