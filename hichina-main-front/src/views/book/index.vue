<template>
  <p class="notice">Travel Shop > {{ productName }}</p>
  <div class="wrapper mt-26">
    <p class="title">Product Name: {{ productName }}</p>
    <ul class="options mt-14">
      <li>Package Category：{{ packageCategory }}</li>
      <li v-if="selectedDate != null && selectedDate.length > 0">
        Start date: {{ selectedDate }}
      </li>
      <li v-if="adultCount != null && adultCount > 0">
        Adults: {{ adultCount }}
      </li>
      <li v-if="childCount != null && childCount > 0">
        Child: {{ childCount }}
      </li>
      <li v-if="infantCount != null && infantCount > 0">
        Infant: {{ infantCount }}
      </li>
      <li v-if="buyCount != null && buyCount > 0">
        Purchase number: {{ buyCount }}
      </li>
      
    </ul>
    <el-form label-position="top" label-width="100px" :model="uniform">
      <div
        v-for="index in parseInt(adultCount) +
        parseInt(childCount) +
        parseInt(infantCount)"
        :key="index"
      >
        <p class="num mt-44">Traveler: {{ index }}</p>
        <el-row :gutter="20" class="mt-12">
          <el-col :span="6">
            <el-form-item label="Surname" required>
              <el-input
                v-model="peopleform.value[index - 1]['surName']"
                placeholder="请输入"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Given Name" required>
              <el-input
                v-model="peopleform.value[index - 1]['givenName']"
                placeholder="请输入"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Day of Birth DD/MM/YYYY" required>
              <!-- <el-input v-model="adultform.value[index-1]['birthday']" placeholder="请输入" /> -->
              <el-date-picker
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
                v-model="peopleform.value[index - 1]['birthday']"
                type="date"
                placeholder="Birthday"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Gender" required>
              <!-- <el-input v-model="adultform.value[index-1]['gender']" placeholder="请输入" /> -->
              <el-select
                v-model="peopleform.value[index - 1]['gender']"
                placeholder="Gender"
              >
                <el-option
                  v-for="item in genderOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col class="mt-20" :span="6">
            <el-form-item label="Nationality" required>
              <country-select
                v-model="peopleform.value[index - 1]['nationality']"
                :country="country"
                topCountry="US"
              />
            </el-form-item>
          </el-col>
          <el-col class="mt-20" :span="6">
            <el-form-item label="Passport Number" required>
              <el-input
                v-model="peopleform.value[index - 1]['passportNo']"
                placeholder="请输入"
              />
            </el-form-item>
          </el-col>
          <el-col class="mt-20" :span="6">
            <el-form-item label="Expiry Date of Passport" required>
              <el-date-picker
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
                v-model="peopleform.value[index - 1]['passportExpireDate']"
                type="date"
                placeholder="Passport Expire Date"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <p class="num mt-44">Contact info</p>

      <el-row :gutter="20" class="mt-12">
        <el-col :span="6">
          <el-form-item label="Name" required>
            <el-input v-model="contactform['name']" placeholder="请输入" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="Email Address" required>
            <el-input v-model="contactform['email']" placeholder="请输入" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="Phone Number" required>
            <el-input v-model="contactform['phone']" placeholder="请输入" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="Address in China" required>
            <el-input v-model="contactform['address']" placeholder="请输入" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="6">
          <el-form-item label="Coupon Code">
            <el-input
              v-model="coupon"
              placeholder="Have Coupon? Enter the Code here"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-form-item>
        <el-button type="primary" @click="submitOrder" round>Submit</el-button>
      </el-form-item> -->
    </el-form>

    <div class="num flex-between mt-44">
      <p>Payment</p>
      <p class="money">CNY {{ totalPrice }}</p>
    </div>

    <ul class="pay mt-50">
      <li class="flex-start">
        <v-lazy-image src="@/assets/images/wechat.png" alt="" />
        <el-button type="primary" @click="submitOrder('wechatpay')">WechatPay</el-button>
      </li>
      <li class="flex-start">
        <v-lazy-image src="@/assets/images/zfb.png" alt="" />
        <el-button type="primary" @click="submitOrder('alipay')">AliPay(支付宝)</el-button>
      </li>
    </ul>
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

function checkPassenger(passenger){
  if(checkEmpty(passenger.birthday) ||
  checkEmpty(passenger.gender) ||
  checkEmpty(passenger.givenName) ||
  checkEmpty(passenger.surName) ||
  checkEmpty(passenger.nationality) ||
  checkEmpty(passenger.passportExpireDate) ||
  checkEmpty(passenger.passportNo) ||
  checkEmpty(passenger.surName)){
    return false;
  }
  return true;
}

function validatePreOrderParams(finalParams) {
  console.log("validating finalParams");
  console.log(finalParams);
  // must have CNY value valid
  if (
    checkEmpty(finalParams.productInfo["totalPrice"]) ||
    !Number.isInteger(parseInt(finalParams.productInfo["totalPrice"]))
  ) {
    alert("something wrong with price");
    return false;
  }
  // must have full contact info
  if (
    checkEmpty(finalParams.contactInfo["email"]) ||
    checkEmpty(finalParams.contactInfo["phone"]) ||
    checkEmpty(finalParams.contactInfo["name"]) ||
    checkEmpty(finalParams.contactInfo["address"])
  ) {
    alert("Please check your contact info is filled correctly");
    return false;
  }
  // must have full product info
  if (
    checkEmpty(finalParams.productInfo)
  ) {
    alert("Please check your product info is correct");
    return false;
  }
  // must have full passenger info if not localspecialty type, if no passenger , no check
  for(var index in finalParams.passengerInfo){
    if(!checkPassenger(finalParams.passengerInfo[index])){
      alert("Please check your passenger info has been filled correctly")
      return false
    }
  }

  return true;
}

function submitOrder(paymethod) {
  var finalParams = {};
  if (LOCALSPECIALTYPRODUCTTYPE != productTypeId.value) {
    finalParams.passengerInfo = peopleform.value;
  }
  finalParams.contactInfo = contactform.value;
  finalParams.coupon = coupon.value;
  finalParams.productInfo = route.params;
  console.log("finalParams");
  console.log(finalParams);
  if (validatePreOrderParams(finalParams)) {
    // do submit
    var params = {}
    params.meta = JSON.stringify(finalParams) 
    params.skuId = productSkuId.value;
    params.status = "SUBMITTED"
    params.price = totalPrice.value;
    params.payMethod = paymethod
    AXIOS.post('/api/v1/order', params).then(res => {
        ElNotification({
          title: 'Suceed',
          message: 'Succeed creating order',
          type: 'success',
        })
        if(paymethod=="wechatpay"){
          // go to we chat pay page
          // go to alipay page
          console.log(res.data.data)
          var param2Pass = {}
          param2Pass.price = res.data.data.price;
          param2Pass.orderId = res.data.data.orderId;
          param2Pass.codeUrl = res.data.data.codeUrl;
          param2Pass.productName = productName.value;

          router.push({name: 'wechatpay', params: param2Pass});
        }else if(paymethod=="alipay"){
          // go to alipay page
          console.log(res.data.data)
          var param2Pass = {}
          param2Pass.price = res.data.data.price;
          param2Pass.orderId = res.data.data.orderId;
          param2Pass.codeUrl = res.data.data.codeUrl;

          router.push({name: 'alipay', params: param2Pass});
        }
      })
      .catch(err => {
        ElNotification({
        title: 'Fail',
        message: 'Fail creating order'+err,
        type: 'error',
      })
    })
  }
}
function whoami(){
  AXIOS.get("/api/v1/user/whoami")
  .then(function (response) {
    console.log("current user in book page: " + response.data)
  })
  .catch(function (error) {
    console.log("currently not logged in setup: "+error);
    router.push("/auth/login")
  });
}

onMounted(() => {
  whoami();

  peopleform.value = [];
  contactform.value = {
    name: "",
    email: "",
    phone: "",
    address: "",
  };
  if (route.params.productName == null 
  || route.params.productName.length < 1
  || route.params.productSkuId == null
  || route.params.productSkuId.length < 1
  || route.params.packageCategory == null
  || route.params.packageCategory.length< 1
  ) {
    router.push({ name: "home" });
    return;
  }
  productName.value = route.params.productName;
  packageCategory.value = route.params.packageCategory;
  selectedDate.value = route.params.selectedDate;
  totalPrice.value = route.params.totalPrice;
  productSkuId.value = route.params.productSkuId;
  adultCount.value =
    route.params.adultCount == null ? 0 : route.params.adultCount;
  childCount.value =
    route.params.childCount == null ? 0 : route.params.childCount;
  infantCount.value =
    route.params.infantCount == null ? 0 : route.params.infantCount;
  buyCount.value = route.params.buyCount == null? 0: route.params.buyCount;
  
  productTypeId.value = route.params.productTypeId;
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
      birthday: "",
      gender: "",
      nationality: "",
      passportNo: "",
      passportExpireDate: "",
    });
  }
});
</script>

<style scoped lang="scss">
.notice {
  min-width: $min-width;
  height: 48px;
  padding-left: 260px;
  color: rgba(80, 80, 80, 1);
  background-color: rgba(229, 242, 250, 1);
  font-size: 14px;
  line-height: 48px;
}

.title {
  color: rgba(80, 80, 80, 1);
  font-size: 30px;
}

.options {
  color: rgba(80, 80, 80, 1);
  font-size: 14px;
}

.num {
  width: 321px;
  padding-bottom: 7px;
  color: rgba(80, 80, 80, 1);
  font-size: 14px;
  border-bottom: 1px solid #2a82e4;

  .money {
    color: rgba(42, 130, 228, 1);
    font-size: 28px;
  }
}

.submit {
  margin-top: 118px;
  width: 500px;

  button {
    margin-left: 38px;
  }
}

.pay {
  font-size: 14px;

  li {
    margin-bottom: 8px;
  }
  img {
    width: 64px;
    height: 64px;
  }

  button {
    margin-left: 18px;
    width: 190px;
    color: rgba(255, 255, 255, 1);
    background-color: rgba(42, 130, 228, 1);
    border-radius: 7px;
    font-size: 14px;
  }
}
</style>