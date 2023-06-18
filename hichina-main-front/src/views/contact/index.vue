<template>
  <div class="contact-container">
    <h2 class="center-text">You need help from our customer serivce?</h2>
    <h3 class="center-text subtitle">Please send your demands to</h3>
    <div class="center-text mailcontact" >
      <a href="mailto:customerservice@hichinatrip.com">customerservice@hichinatrip.com</a>
    </div>
    <div class="center-text subtitle">our co-workers will will response ASAP</div>
    <h2 class="center-text contact-us">Also, add us on Wechat/WhatsAPP!</h2>
    <div class="center-text">
      <v-lazy-image src="https://photoprism.hichinatravel.com/api/v1/t/d68ee79239386b14869f548a423de4bcc7e3cb31/32mcf2k4/fit_2048" alt="" />
    </div>

  </div>
  
</template>

<script setup>
import VLazyImage from "v-lazy-image";
import router from "../../router";
import {AXIOS} from '@/common/http-commons'
import { ElNotification } from 'element-plus'  

const peopleform = reactive([]);
const uniform = ref([]);
const contactform = ref({});
const route = useRoute();

const coupon = ref("");
const country = ref("");
const productName = ref("");
const productTypeId = ref("");
const productSkuId = ref("")
const packageCategory = ref("");
const selectedDate = ref(new Date());
const adultCount = ref(0);
const childCount = ref(0);
const infantCount = ref(0);
const buyCount = ref(1)
const totalPrice = ref(0);

const LOCALSPECIALTYPRODUCTTYPE = "fd264cab-ee8d-4571-a477-03d7e7c090b3";

const genderOptions = [
  {
    value: "1",
    label: "Male",
  },
  {
    value: "0",
    label: "Female",
  },
];

function checkEmpty(val) {
  if (val == null || val.length < 1) {
    return true;
  } else {
    return false;
  }
}


onMounted(() => {
  console.log("mounted contact")
});
</script>

<style scoped lang="scss">
.contact-container{
  margin: 0 auto;
  width: 70%;
  min-width: 500px;
}
.center-text{
  text-align: center;
}
.subtitle{
  color: #90908b;
}
.mailcontact{
  margin: 0 auto;
}
.contact-us{
  color: #90908b;
}
</style>