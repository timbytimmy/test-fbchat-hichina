<template>
  <div class="main-container">
    <el-button type="primary" @click="goUserPageOrderPart">Return to order list</el-button>
    <div class="table">
      <ul class="table-column item flex-start">
        <li>Item</li>
        <li>Package Item</li>
        <li>Total Price</li>
        <li>Status</li>
        <li v-if="selectedDate!=null && selectedDate.length>0">Travel Time</li>
      </ul>
      <ul class="table-body flex-start">
        <li class="align-left">
          <a :href="frontendBase+'/product-detail/'+skuGroupId">{{ productName }} </a>
        </li>
        <li>{{ packageCategory }}</li>
        <li>{{ totalPrice }}</li>
        <li>{{ orderStatus }}</li>
        <li v-if="selectedDate!=null && selectedDate.length>0">{{ selectedDate }}</li>
      </ul>
    </div>

    <div v-if="passengerInfo!=null && passengerInfo.length>0" class="table">
      <p style="font: bold;">Passenger Info</p>
      <el-table :data="passengerInfo" border style="width: 100%">
        <el-table-column prop="surName" label="Surname" width="180" />
        <el-table-column prop="givenName" label="Given Name" width="180" />
        <el-table-column prop="nationality" label="Nationality" />
        <el-table-column prop="birthday" label="Birth Day" />
        <el-table-column prop="gender" label="Gender" />
        <el-table-column prop="passportNo" label="Passport No." />
        <el-table-column prop="passportExpireDate" label="Passport Expire Date" />
      </el-table>
    </div>

    <div class="table">
      <ul class="table-column item flex-start">
        <li>Contact Person</li>
        <li>E-mail</li>
        <li>Phone Number</li>
        <li>Address</li>
      </ul>
      <ul class="table-body flex-start">
        <li>{{ contactInfo.name }}</li>
        <li>{{ contactInfo.email }}</li>
        <li>{{contactInfo.phone}}</li>
        <li>{{contactInfo.address}}</li>
      </ul>
    </div>
  </div>
</template>
   
<script setup>
import { AXIOS } from "@/common/http-commons";
import router from "../../router";
const route = useRoute();
const orderDetail = ref({})
const productName = ref("")
const packageCategory = ref("")
const selectedDate = ref("")
const contactInfo = ref({"address": "", "email": "", "name": "", "phone":""})
const passengerInfo = ref([])
const totalPrice = ref(0)
const orderStatus = ref("")
const skuGroupId = ref("")
const frontendBase = ref("")

function goUserPageOrderPart(){
    var param2Pass = {}
    param2Pass.active = 2
    router.push({name: 'user', params: param2Pass })
}

function loadOrderDetail(orderId) {
  AXIOS.get("/api/v1/order/orderDetail/" + orderId)
    .then((response) => {
      console.log("order detail: ");
      console.log(response.data.data);
      orderDetail.value = response.data.data

      productName.value = orderDetail.value.meta.productInfo.productName
      packageCategory.value = orderDetail.value.meta.productInfo.packageCategory
      selectedDate.value = orderDetail.value.meta.productInfo.selectedDate

      contactInfo.value = orderDetail.value.meta.contactInfo;
      totalPrice.value = orderDetail.value.price;

      orderStatus.value = orderDetail.value.status;

      passengerInfo.value = orderDetail.value.meta.passengerInfo
      
      skuGroupId.value = orderDetail.value.meta.productInfo.productSkuGroupId
    })
    .catch((e) => {
      console.log("get order detail err");
      console.log(e);
    });
}

function whoami(){
    AXIOS.get("/api/v1/user/whoami")
    .then(function (response) {
      console.log("current user in user page: " + response.data)
    })
    .catch(function (error) {
      console.log("currently not logged in setup: "+error);
      router.push({name: 'home'})
    });
  }

function loadFrontendBase(){
    AXIOS.get("/api/public/service").then(function (response) {
      console.log("base front url: " + response.data)
      frontendBase.value = response.data
    })
    .catch(function (error) {
      console.log("currently not logged in setup: "+error);
      router.push({name: 'home'})
    });
}

onMounted(() => {
    whoami();
  console.log("route.params");
  console.log(route.params.orderId);
  loadOrderDetail(route.params.orderId);

  loadFrontendBase()
});
</script>
   
   <style scoped lang="scss">
.main-container{
    width: 60%;
    margin: 0 auto;
}
.table {
  margin-bottom: 21px;
}

.table-column {
  height: 46px;
  left: 409px;
  top: 361px;
  background-color: rgba(231, 231, 231, 1);
}

.item {
  color: rgba(80, 80, 80, 1);
  font-size: 14px;
  li {
    width: calc(100% / 3);
    text-align: center;
  }
}

.table-body {
  margin-top: 2px;
  color: rgba(80, 80, 80, 1);
  font-size: 14px;
  text-align: center;
  border: rgba(184, 181, 181, 1) solid 1px;
  li {
    width: calc(100% / 3);
    padding: 19px 32px;
  }

  .align-left {
    text-align: left;
  }
}

.passport {
  flex: 0 0 30%;
}
</style>