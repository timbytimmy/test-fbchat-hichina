<template>
 <p class="notice">Travel Shop> {{ productTypeName }} > {{ productName }}</p>
  <div class="wrapper flex-between stretch mt-24">
    <div class="left">
      <div class="swiper-min">
        <swiper-container class="swiper-wrapper" autoplay-delay="2000" loop="true">
          <swiper-slide v-for="imageUrl in imageContainer.imageList " :key="imageUrl"><v-lazy-image :src="imageUrl" alt="" /></swiper-slide>
        </swiper-container>
      </div>
      <!-- <VueDatePicker v-model="multipleSelectedDate" inline multi-dates /> -->
      <datepicker v-if="renderComponent && productTypeId!=LOCALSPECIALTYPRODUCTTYPE" @selected="handleSelectDate" use-utc="true" :disabled-dates="state.disabledDates" v-model="selectedDate" :prevent-disable-date-selection="true" inline="true" :icon-width="40" calendar-class="calendarclass"></datepicker>
    </div>

    <div class="content">
      <p class="title">{{ productName }}</p>
      <div class="info" >{{ removeHtmlTag(productDescription).substring(0, 300) }}...</div>
      <div class="days flex-start mt-10" v-if="productTypeId!=LOCALSPECIALTYPRODUCTTYPE">
        <p>Days：</p>
        <span v-if="productTypeId!='d7b95089-e278-4522-8f71-dacac41ba32f'">{{ days-1 }} nights {{ days }} days</span>
        <span v-if="productTypeId==='d7b95089-e278-4522-8f71-dacac41ba32f'">{{ days }} days</span>
      </div>
      <div class="days flex-start stretch mt-10" v-if="productTypeId!=LOCALSPECIALTYPRODUCTTYPE">
        <p>Option：</p>
        <ul class="options more">
          <li :class="{ active: index==activeIndex }" @click="setActiveCate(item, index)" v-for="(item, index) in packageCategories" :key="item">{{ item }}</li>
        </ul>
      </div>

      <p v-if="productTypeId=='d7b95089-e278-4522-8f71-dacac41ba32f' || productTypeId=='3a53caed-b788-4290-896d-7922532ad769' || productTypeId=='e05d07a3-a717-45b8-b009-47a349890e41'" class="pass mt-64">Passengers</p>
      <p v-if="productTypeId==HOTELPRODUCTTYPE" class="pass mt-64">Rooms</p>

      <div class="count flex-start" v-if="productTypeId!=LOCALSPECIALTYPRODUCTTYPE">
        <div class="count-item flex-start">
          <p v-if="adultUnitPrice!=0">Adult({{ adultUnitPrice }}/Each)</p>
          <p>Adult</p>
          <el-input-number min="1" v-model="adultCount"/>
        </div>
        <div class="count-item flex-start no-mar">
          <p v-if="infantUnitPrice!=0">Infant({{ infantUnitPrice }}/Each):</p>
          <p>Infant:</p>
          <el-input-number min="0" v-model="infantCount" />
          <span class="text">(less than 2 years old)</span>
        </div>
        <div class="count-item flex-start">
          <p  v-if="childUnitPrice!=0">Child({{ childUnitPrice }}/Each):</p>
          <p>Child:</p>
          <el-input-number min="0" v-model="childCount"/>
        </div>
      </div>

      <!-- local specialty product type -->
        <div v-if="productTypeId===LOCALSPECIALTYPRODUCTTYPE" class="days flex-start stretch mt-10">
          <p>Option：</p>
          <ul class="options">
            <li :class="{ active: index==activeIndex }" @click="setActiveCateForLocalSpecialty(item, index)" v-for="(item, index) in packageCategories" :key="item">{{ item }}</li>
          </ul>
        </div>

        <div v-if="productTypeId===LOCALSPECIALTYPRODUCTTYPE || productTypeId===HOTELPRODUCTTYPE" class="count flex-start">
          <div class="count-item flex-start">
            <p>Numbers:</p>
            <el-input-number min="1" :max="maxNum" v-model="buyCount" />
          </div>
        </div>

      <div class="cart flex-between">
        <p>CNY</p>
        <p v-if="productTypeId!=LOCALSPECIALTYPRODUCTTYPE && productTypeId!= HOTELPRODUCTTYPE" class="price flex-item">{{ adultCount*adultUnitPrice+childCount*childUnitPrice+infantCount*infantUnitPrice }}</p>
        <p v-if="productTypeId==LOCALSPECIALTYPRODUCTTYPE || productTypeId== HOTELPRODUCTTYPE" class="price flex-item">{{ generalPrice*buyCount }}</p>
        <button class="pre-book" @click="goPage('/contact')">Pre-Book Consult</button>
        <button class="book" @click="collectConfigAndGoPage('book')">Book Now</button>
      </div>
    </div>
  </div>

  <div class="intro mt-20">
    <div class="wrapper">
      <div class="intro-head flex-between">
        <!-- <ul class="category flex-start">
          <li :class="{active: active === 0}" @click="active = 0">Highlights</li>
          <li :class="{active: active === 1}" @click="active = 1">Overview</li>
          <li :class="{active: active === 2}" @click="active = 2">Cost</li>
          <li :class="{active: active === 3}" @click="active = 3">Refund Policy</li>
        </ul> -->
        <!-- <button class="book">Book Now</button> -->
      </div>
      <div class="rich-area mt-8">
        <!-- <p class="title">Highlights</p> -->
        <div class="main" v-html="productDescription"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import router from "../../router";
  import VLazyImage from "v-lazy-image";
  import {AXIOS} from '@/common/http-commons'
  import { useRouter } from 'vue-router';
  import { useSeoMeta } from 'unhead'
  const route = useRoute()
  const change = (obj)=>{
    console.log('obj',obj)
  }
  const activeIndex = ref(0)
  const date = ref("2021-1-27") //设置日期，若不设置，则默认为今天
  const range = [2015,2025] //设置年份范围 默认[2010,2030]
  const swiperOptions = {
    loop: true,
    autoplay: {
      delay: 2000,
      stopOnLastSlide: false,
      disableOnInteraction: true
    }
  }

  function removeHtmlTag(input){
    var div = document.createElement("div");
    div.innerHTML = input;
    var text = div.textContent || div.innerText || "";
    return text;
  }

  function goPage(val){
    router.push({path: val})
  }
  const activeCategory = ref("")
  const adultCount = ref(1)
  const childCount = ref(0)
  const infantCount = ref(0)
  const adultUnitPrice = ref(0)
  const generalPrice = ref(0)
  const childUnitPrice = ref(0)
  const infantUnitPrice = ref(0)
  const maxNum = ref(1000000)
  const active = ref(0)
  const buyCount = ref(1)
  const productName = ref("")
  const productDescription = ref("")
  const productTypeId = ref("")
  const productTypeName = ref("")
  const days = ref("")
  const packageCategories = ref([])
  const imageList = ref([])
  const imageContainer = {imageList:[]}
  const skusInGroup = {skus: []}
  const filteredSkuIndex = ref([])
  const renderComponent = ref(true);
  const singleMatchedSku = ref({})
  const selectedDate = ref(new Date())
  const multipleSelectedDate = ref([])
  const totalPrice = ref(0)

  const TYPEOFPACKAGEPROP="11cd8b32-c4f6-47db-8b8a-486c992bf43b"
  const LOCALSPECIALTYPRODUCTTYPE = "fd264cab-ee8d-4571-a477-03d7e7c090b3";
  const HOTELPRODUCTTYPE="a9f5adbe-c09b-49bc-a614-8a1c5d5e5337";
  const MAXNUM = "720f4f2e-e114-4003-9806-bc56a9366278";
  const PRODUCTIMAGEURLPROP = "2dea54f4-9b9c-413a-8b3a-0caf273283d2"
  const DAYSPROP = "8cc865ff-b30f-4f00-b426-9e64418e5100"
  const AVAILABLEDATEPROP = "f0b807e5-d1a6-4454-a400-7905a4fea492"

  const forceRerender = async () => {
    // Remove MyComponent from the DOM
    renderComponent.value = false;

    // Wait for the change to get flushed to the DOM
    await nextTick();

    // Add the component back in
    renderComponent.value = true;
  };


  var state = {
    disabledDates: {
      to: new Date(2023, 3, 15), // Disable all dates up to specific date
      from: new Date(2023, 4, 20), // Disable all dates after specific date
    
      dates: [ // Disable an array of dates
        new Date(2016, 9, 16),
        new Date(2016, 9, 17),
        new Date(2016, 9, 18)
      ],
      preventDisableDateSelection: true
    },
  }

  function getTotalPrice(){
    if(productTypeId.value!=LOCALSPECIALTYPRODUCTTYPE && productTypeId.value!= HOTELPRODUCTTYPE){
      totalPrice.value = adultCount.value*adultUnitPrice.value+childCount.value*childUnitPrice.value+infantCount.value*infantUnitPrice.value;
      console.log("reach abb")
    }else{
      console.log("reach aba")
      totalPrice.value = generalPrice.value*buyCount.value;
    }
    console.log("this is the total price:")
    console.log(totalPrice.value)
    return totalPrice.value;
  }

  function collectConfigAndGoPage(val){
    if(singleMatchedSku.value == null){
      alert("Not all required config are selected")
      return;
    }
    var params = {}
    params.productTypeId = productTypeId.value;
    params.productName = productName.value;
    params.productSkuId = singleMatchedSku.value['hichinaProductBasicDTO']['skuId'];
    params.productSkuGroupId = route.params.skuGroupId;
    params.packageCategory = activeCategory.value;
    params.productTypeName = productTypeName.value;
    params.profileImageUrl = extractAttributeValueFromProductPropertyBag(singleMatchedSku.value, PRODUCTIMAGEURLPROP);
    params.totalPrice = getTotalPrice();
    //params.totalPrice = 1;
    if(productTypeId.value!=LOCALSPECIALTYPRODUCTTYPE){
      params.adultCount = adultCount.value;
      params.childCount = childCount.value;
      params.infantCount = infantCount.value;
    }
    if(LOCALSPECIALTYPRODUCTTYPE!=productTypeId.value){
      params.selectedDate = selectedDate.value.toISOString().split('T')[0];
    }
    if(productTypeId.value==LOCALSPECIALTYPRODUCTTYPE || productTypeId.value== HOTELPRODUCTTYPE){
      params.buyCount = buyCount.value
    }
    
    console.log("params collected from product")
    console.log(params)
    router.push({ name: val, params: params })
  }

  function setPrice(sku){
    if(sku==null){
      if(productTypeId.value == LOCALSPECIALTYPRODUCTTYPE || productTypeId.value == HOTELPRODUCTTYPE){
        console.log("001")
        generalPrice.value = 0;
      }else{
        adultUnitPrice.value = 0
        childUnitPrice.value = 0
        infantUnitPrice.value = 0
      }
    }else{
      if(productTypeId.value == LOCALSPECIALTYPRODUCTTYPE || productTypeId.value == HOTELPRODUCTTYPE){
        console.log("003")
        generalPrice.value = extractAttributeValueFromProductPropertyBag(sku,"e16df480-b17d-4442-91c2-d6c30d0d7ab0")
      }else{
        console.log("004")
        adultUnitPrice.value = extractAttributeValueFromProductPropertyBag(sku, "e228b843-e054-41f8-91dd-19663460df54")
        childUnitPrice.value = extractAttributeValueFromProductPropertyBag(sku, "c4c845a7-4bef-46d8-a5ad-d72a5464e8b1")
        infantUnitPrice.value = extractAttributeValueFromProductPropertyBag(sku, "448406cb-b68f-439e-9da8-148d78ae8404")
      }
    }
  }

  function handleSelectDate(dt){
    // console.log("dt")
    // console.log(dt)
    // var normalizedDate = Date.UTC(dt.getFullYear(), dt.getMonth(), dt.getDate())
    // console.log("normalizedDate")
    // console.log(normalizedDate)
    // check dt falls into which specific skus
    // selectedDate.value = dt
    console.log("dt")
    console.log(dt)
    var matchedIndex = -1;
    // console.log(filteredSkuIndex.value)
    for(var i in filteredSkuIndex.value){
      var index = filteredSkuIndex.value[i];
      var availableDatesInString = extractAttributeValueFromProductPropertyBag(skusInGroup.skus[index],AVAILABLEDATEPROP);
      var dateObjArray = multiDateString2DateObjectArray(availableDatesInString)
      console.log("dateObjArray")
      console.log(dateObjArray)
      if(dateObjArray.some(e => e.getDate() == dt.getDate())){
        matchedIndex = index;
        break;
      }
    }
    console.log("matchedIndex")
    console.log(matchedIndex)
    singleMatchedSku.value = skusInGroup.skus[matchedIndex]

    maxNum.value = extractAttributeValueFromProductPropertyBag(singleMatchedSku.value, MAXNUM)

    console.log("maxNum")
    console.log(maxNum.value)
    setPrice(singleMatchedSku.value)
  }

  function compareDate( a, b ) {
    if ( a.getDate() < b.getDate() ){
      return -1;
    }
    if ( a.getDate() > b.getDate() ){
      return 1;
    }
    return 0;
  }
    
  function multiDateString2DateObjectArray(candidateAvailableDates){
    var dateObjArray = []
    var datestringArray = candidateAvailableDates.split(";");
    for (var i in datestringArray) {
      var datestr = datestringArray[i];
      if (datestr != null && datestr.length > 0) {
        var dateObj = new Date(datestr);
        // console.log("...converting string to date:"+ datestr)
        // console.log(dateObj)
        dateObjArray.push(dateObj);
      }
    }
    return dateObjArray
  }

  function setCandidateAvailableDates(indexArray){
    var candidateAvailableDates = ""
    for(var i in indexArray){
      var availableDates = extractAttributeValueFromProductPropertyBag(skusInGroup.skus[indexArray[i]],AVAILABLEDATEPROP);
      candidateAvailableDates = candidateAvailableDates + availableDates
    }

    var dateObjArray = multiDateString2DateObjectArray(candidateAvailableDates)

    dateObjArray.sort(compareDate);

    var minDate = dateObjArray[0]
    var maxDate = dateObjArray.slice(-1)[0]

    state.disabledDates.to = minDate
    var nextDayOfMax = new Date(maxDate)
    nextDayOfMax.setDate(nextDayOfMax.getDate()+1)
    state.disabledDates.from = nextDayOfMax

    var segmentDisabledDates = []
    var pointer = new Date(minDate)

    // console.log("...reach after pointer...")
    // console.log(minDate)

    while(pointer<maxDate){
      // check contains
      if(!dateObjArray.some(e => e.getDate() == pointer.getDate())){
        segmentDisabledDates.push(new Date(pointer))
      }
      // update pointer
      pointer.setDate(pointer.getDate() + 1)
    }
    state.disabledDates.dates = segmentDisabledDates

    forceRerender()
  }
  
  function getFilteredSkuIndexAndSetActiveValue(item, index){
    activeCategory.value = item
    filteredSkuIndex.value = []
    for(var i in skusInGroup.skus){
      var sku = skusInGroup.skus[i];
      var selectedSkuPropertyBags = sku['productPropertyBag'].filter((obj) => {
        return obj.attributeId == TYPEOFPACKAGEPROP;
      });
      if(selectedSkuPropertyBags[0].attributeValue == item){
        filteredSkuIndex.value.push(i)
      }
    }
    activeIndex.value = index;
  }
  function setActiveDescription(){
    var indexToFilter = filteredSkuIndex.value.length<1?0:filteredSkuIndex.value[0]
    productDescription.value = skusInGroup.skus[indexToFilter]['hichinaProductBasicDTO']['productContent'];
  }
  function setActiveCate(item, index){
    getFilteredSkuIndexAndSetActiveValue(item,index)

    setCandidateAvailableDates(filteredSkuIndex.value)
    setPrice(null)
    selectedDate.value = new Date();
    singleMatchedSku.value = null;

    setActiveDescription()
  }

  function setActiveCateForLocalSpecialty(item, index){
    getFilteredSkuIndexAndSetActiveValue(item,index)

    singleMatchedSku.value = skusInGroup.skus[filteredSkuIndex.value[0]];
    setPrice(singleMatchedSku.value)

    setActiveDescription()
  }

  function setFlightHotelProductData(inputArray){
    setTourProductData(inputArray)
  }

  function setHotelProductData(inputArray){
    setTourProductData(inputArray)
  }

  function setFligtProductData(inputArray){
    setTourProductData(inputArray)
  }

  function setLocalSpecialtyProductData(inputArray){
    packageCategories.value = []
    imageList.value = []
    for(var index in inputArray){
      var packageCat = extractAttributeValueFromProductPropertyBag(inputArray[index], TYPEOFPACKAGEPROP);
      if(packageCat!=null){
        packageCategories.value.push(packageCat)
      }
      
      
      var url = extractAttributeValueFromProductPropertyBag(inputArray[index], PRODUCTIMAGEURLPROP);
      if(url!=null){
        imageContainer.imageList.push(url)
      }
    }
    // trick: remove duplicates
    packageCategories.value = [...new Set(packageCategories.value)];
    // force select the first package category on entering page
    setActiveCateForLocalSpecialty(packageCategories.value[0], 0);
  }

  function setTourProductData(inputArray){
    packageCategories.value = []
    imageList.value = []
    for(var index in inputArray){
      var packageCat = extractAttributeValueFromProductPropertyBag(inputArray[index], TYPEOFPACKAGEPROP);
      if(packageCat!=null){
        packageCategories.value.push(packageCat)
      }
      
      var url = extractAttributeValueFromProductPropertyBag(inputArray[index], PRODUCTIMAGEURLPROP);
      if(url!=null){
        imageContainer.imageList.push(url)
      }

      // this if means only need to calculate days once because all this property in the same sku group should be same
      if(days.value==""){
        days.value = extractAttributeValueFromProductPropertyBag(inputArray[index], DAYSPROP);
      }
    }
    // trick: remove duplicates
    packageCategories.value = [...new Set(packageCategories.value)];
    // force select the first package category on entering page
    setActiveCate(packageCategories.value[0], 0);
  }

  function extractAttributeValueFromProductPropertyBag(inputObject, attrId){
    var valArray = inputObject.productPropertyBag.filter((obj) => {
      return obj.attributeId == attrId;
    });
    if(valArray.length>0){
      return valArray[0].attributeValue;
    }
    return null;
  }

  function processSkuGroups(inputArray){
    if(inputArray.length>0){
      productName.value=inputArray[0]['hichinaProductBasicDTO']['productName'];
      productTypeId.value = inputArray[0]['hichinaProductBasicDTO']['productTypeId'];
      productTypeName.value = inputArray[0]['hichinaProductBasicDTO']['productTypeName'];

      useSeoMeta({
        title: productName.value,
        description: productName.value,
        ogDescription: productName.value,
        ogTitle: productName.value,
        ogImage: 'https://www.hichinatravel.com/static/png/name-67280b81.png',
        twitterCard: 'summary_large_image',
      })

      if(productTypeId.value === "3a53caed-b788-4290-896d-7922532ad769"){
        // 跟团游，则按照跟团游的页面模板来设置各个参数
        setTourProductData(inputArray);
      }else if(productTypeId.value === "e05d07a3-a717-45b8-b009-47a349890e41"){
        // 机票酒店套餐
        setFlightHotelProductData(inputArray)
      }else if(productTypeId.value === "d7b95089-e278-4522-8f71-dacac41ba32f"){
        // 机票
        setFligtProductData(inputArray)
      }else if(productTypeId.value === HOTELPRODUCTTYPE){
        // 酒店
        setHotelProductData(inputArray)
      }else if( productTypeId.value === LOCALSPECIALTYPRODUCTTYPE){
        // 本地土特产
        setLocalSpecialtyProductData(inputArray)
      }
    }
  }

  function logPv(){
    AXIOS.post("/api/public/pagestats/pv/product-detail-"+route.params.skuGroupId)
    .then((res) => {
      console.log("log pv:")
      console.log(res.data);
    })
    .catch((err) => {
      console.error("Error:", err);
    });
  }

  function logView(){
    AXIOS.post("/api/public/pagestats/view-product/"+route.params.skuGroupId)
    .then((res) => {
      console.log("view cnt of this product:")
      console.log(res.data);
    })
    .catch((err) => {
      console.error("Error:", err);
    });
  }

  function loadSkusInGroup(){
    var params =  {}
    params.skuGroupId = route.params.skuGroupId
    AXIOS.get("/api/public/productsku/bygroupidwithpropertybag", {
      params: params,
    })
    .then(function (response) {
       skusInGroup.skus = response.data.data
       processSkuGroups(skusInGroup.skus)
    })
    .catch(function (error) {
      console.log(error)
    });
  }

  onMounted(() => {
    logPv()
    logView()
    loadSkusInGroup()
  })
</script>

<style scoped lang="scss">
  .notice {
    height: 48px;
    padding-left: 260px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(229, 242, 250, 1);
    font-size: 14px;
    line-height: 48px;
  }

  .left {
    width: 565px;
  }

  .swiper-min,
  .banner {
    width: 565px;
    height: 297px;
    min-width: none !important;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
    }
  }

  .calendarclass {
    width: 100%;
    font-size: initial;

    :deep(.el-calendar-day) {
      height: 80px;
    }
  }

  .content {
    flex: 1;
    margin-left: 37px;
  }

  .title {
    height: 144px;
    color: rgba(80, 80, 80, 1);
    font-size: 32px;
  }

  .info {
    padding: 19px 4px;
    height: 171px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(255, 249, 198, 1);
    font-size: 14px;
  }

  .days {
    color: rgba(80, 80, 80, 1);
    font-size: 14px;

    p {
      margin-right: 12px;
      width: 104px;
      color: rgba(80, 80, 80, 1);
      font-size: 24px;
      text-align: right;
    }
  }

  .private {
    padding-left: 119px;
    font-size: 12px;
  }

  .pass {
    height: 27px;
    padding-bottom: 41px;
    color: rgba(80, 80, 80, 1);
    font-size: 18px;
    line-height: 27px;
    text-align: left;
    border-bottom: 1px solid #505050;
  }

  .count {
    flex-wrap: wrap;
    padding: 11px 21px;
    color: rgba(80, 80, 80, 1);
    font-size: 18px;

    &-item {
      margin-right: 61px;
      padding: 6px 0;

      p {
        margin-right: 31px;
      }
    }

    .el-input-number {
      width: 150px;
    }

    .text {
      margin-left: 14px;
      color: rgba(80, 80, 80, 1);
      font-size: 14px;
    }
  }

  .cart {
    margin-top: 21px;
    height: 56px;
    padding-left: 6px;
    color: #2a82e4;
    background-color: rgba(233, 233, 233, 1);
    font-size: 26px;
    line-height: 56px;

    .price {
      text-align: center;
    }

    .pre-book {
      cursor: pointer;
      width: 151px;
      height: 100%;
      color: rgba(250, 250, 250, 1);
      background-color: rgba(255, 141, 26, 1);
      font-size: 17px;
      border: none;
    }
  }

  .book {
    cursor: pointer;
    width: 151px;
    height: 54px;
    color: rgba(243, 242, 242, 1);
    background-color: rgba(42, 130, 228, 1);
    font-size: 25px;
    border: none;
  }

  .intro {
    padding: 24px 0;
    background-color: rgba(217, 217, 217, 1);

    &-head {
      background: white;
    }
  }

  .category {
    padding: 15px 0 15px 8px;
    color: rgba(60, 60, 60, 1);
    font-size: 18px;
    text-align: center;

    li {
      padding: 0 24px;

      &.active {
        color: rgba(42, 130, 228, 1);
        font-size: 18px;
      }
    }
  }

  .rich-area {
    background: white;

    .title {
      height: 47px;
      padding: 12px 0 8px 32px;
      color: rgba(42, 130, 228, 1);
      font-size: 18px;
      border-bottom: 1px solid #999999;
    }

    .main {
      padding: 32px;
    }
  }

  .options {
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    width: 346px;
    li {
      margin-bottom: 12px;
      margin-right: 43px;
      width: 125px;
      height: 36px;
      color: rgba(80, 80, 80, 1);
      background-color: rgba(255, 255, 255, 1);
      border: rgba(204, 204, 204, 1) solid 1px;
      border-radius: 4px;
      font-size: 14px;
      line-height:36px;
      text-align: left;
      cursor: pointer;
    }
    li.active{
      background-color: rgb(165, 201, 107);
    }

    &.more li {
      width: auto;
    }
  }
</style>