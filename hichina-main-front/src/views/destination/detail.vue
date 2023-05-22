<template>
  <div class="notice">
    <p class="nav">Home > Destination > {{ destinationName }}</p>

    <p class="name mt-8">{{ destinationName }}</p>
  </div>

  <div class="wrapper mt-46">
    <div class="head-content flex-between stretch">
      <v-lazy-image class="logo" :src="destinationProfileImage" alt="" />
      <div>
        <div class="info" v-html="description"></div>
        <div class="flex-between mt-24">
          <p class="guidebook">Guidebook of {{ destinationName }}</p>
          <el-button class="download" @click="goDownload" type="primary">Download</el-button>
        </div>
      </div>
    </div>

    <p class="text mt-16">Places to go in {{ destinationName }}</p>

    <div class="list mt-36">
      <div class="list-item flex-between stretch" v-for="item in childDestinations" v-bind:key="item.destinationId">
        <v-lazy-image :src="item.destinationProfileImage" alt="" />

        <div class="content flex-item">
          <p class="title">{{ item.destinationName }}</p>
          <p class="mt-50">{{ item.description }}</p>
        </div>
      </div>
    </div>

    <p class="text mt-50">Relevant Tours of {{ destinationName }}</p>

    <div class="list flex-start mt-16" v-for="(item, index) in relevantToursProduct" v-bind:key="index">
      <other-product-item :productSummary="item" class="other-item" float/>
    </div>

    <!-- <p class="text mt-50">Relevant Blogs of Kunming</p>

    <div class="list flex-start no-bor mt-16">
      <product-item :productSummary="[]" class="product-item" v-for="i in 4" :class="{'no-mar': i % 4 === 0}"/>
    </div> -->

    <p class="more mt-46">鼠标滚动至此处自动加载</p>
  </div>
</template>

<script setup>
  import VLazyImage from "v-lazy-image";
  import { ElNotification } from 'element-plus'  
  import ProductItem from '../home/components/ProductItem'
  import OtherProductItem from '../home/components/OtherProductItem'
  import {AXIOS} from '@/common/http-commons'
  const description = ref("")
  const destinationProfileImage = ref("")
  const downloadUrl = ref("")
  const destinationName = ref("")
  const childDestinations = ref([])
  const relevantToursProduct = ref([])
  function goDownload(){
    if(downloadUrl.value==null || downloadUrl.value=="" ){
      ElNotification({
        title: 'No guidebook',
        message: 'There is no guidebook for this destination',
        type: 'error',
      })
      return
    }else{
      window.location.href = downloadUrl.value
    }
  }

  const route = useRoute()

  function loadDestinations(){
    AXIOS.get('/api/public/destination/'+route.params.destinationId).then(response=>{
       console.log("destination detail: ")
       console.log(response.data.data);
       description.value = response.data.data.description;
       destinationProfileImage.value = response.data.data.destinationProfileImage;
       downloadUrl.value = response.data.data.downloadUrl;
       destinationName.value = response.data.data.destinationName;
    }).catch(e=>{
      console.log("get destination detail err")
      console.log(e)
    })
  }

  function loadChildrenDestination(){
    AXIOS.get('/api/public/destination/children/'+route.params.destinationId).then(response=>{
       console.log("destination children list: ")
       console.log(response.data.data);
       childDestinations.value = response.data.data
       
    }).catch(e=>{
      console.log("get destination detail err")
      console.log(e)
    })
  }

  function loadRelatedTours(){
    AXIOS.get('/api/public/destination/relavanttourproduct/'+route.params.destinationId).then(response=>{
       console.log("relevant products : ")
       console.log(response.data)
       if(response.data.ok===true){
        relevantToursProduct.value = response.data.data;
       }else{
        relevantToursProduct.value = [];
       }
    }).catch(e=>{
      console.log("get relevant tours product err")
      console.log(e)
    })

  }

  onMounted(() => {
    console.log("on mounted destination detail page")
    loadDestinations()
    // get children destinations:
    loadChildrenDestination()
    loadRelatedTours()
  })
</script>

<style scoped lang="scss">
  .notice {
    height: 108px;
    padding: 12px 260px 20px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(229, 242, 250, 1);
    font-size: 14px;
  }

  .nav {
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
  }

  .name {
    color: rgba(80, 80, 80, 1);
    font-size: 32px;
  }

  .head-content {
    padding-bottom: 56px;
    border-bottom: 1px solid #dddddd;
  }

  .logo {
    height: 353px;
    margin-right: 44px;
  }

  .info {
    padding: 12px;
    height: 275px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(255, 249, 198, 1);
    font-size: 14px;
  }

  .guidebook {
    color: rgba(80, 80, 80, 1);
    font-size: 25px;
  }

  .download {
    width: 348px;
    height: 48px;
    border-radius: 5px;
    font-size: 20px;
  }

  .text {
    color: rgba(80, 80, 80, 1);
    font-size: 25px;
    text-align: center;
  }

  .list {
    padding: 0 40px;
    border-bottom: 1px solid #dddddd;
  }

  .list-item {
    margin-bottom: 52px;

    img {
      width: 336px;
      height: 216px;
    }

    .title {
      color: rgba(80, 80, 80, 1);
      font-size: 25px;
    }

    .content {
      margin-left: 31px;
      padding-left: 31px;
      border-left: 1px solid #999999;
      color: rgba(80, 80, 80, 1);
      font-size: 14px;
    }
  }

  .product-item {
    margin-right: 25px;
  }

  .more {
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
    text-align: center;
  }
</style>