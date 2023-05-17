<template>
  <div class="swiper">
    <div class="swiper-wrapper">
      <swiper-container  autoplay-delay="3000" loop="true">
        <swiper-slide style="cursor: pointer" class="banner swiper-slide" v-for="(item,index) in sliders " :key="index" >
          <v-lazy-image :src="item.imageUrl" alt="" />
        </swiper-slide>
      </swiper-container>
    </div>
    <el-icon class="swiper-button-prev swiper-btn" size="50">
      <svg-icon icon-class="swiper-prev"/>
    </el-icon>
    <el-icon class="swiper-button-next swiper-btn" size="50">
      <svg-icon icon-class="swiper-next"/>
    </el-icon>
    <div class="swiper-pagination"></div>
  </div>

  <!-- <p class="text">Editor's Choice this Week</p> -->

  <div class="wrapper mt-22">
    <!-- <div class="info">
      <p>Municipality</p>
      <p>All</p>
      <p>Shanghai (1)Beijing (1)Tianjin (1)Chongqing (1)</p>
      <p>Province</p>
      <p>All</p>
      <p>Hainan (2)Yunnan (5)Tibet (1)Shandong (6)Zhejiang (4)Fujian</p>
      <p>(2)Shaanxi (1)Shanxi (1)Hunan (3)Hubei (2)Guangdong (4)Guangxi</p>
      <p>(3)Anhui (1)Jiangxi (2)Jiangsu (3)Henan (3)Hebei (2)Xinjiang</p>
      <p>(2)Heilongjiang (2)Jilin (1)Liaoning (3)Qinghai (3)Gansu</p>
      <p>(4)Ningxia (1)Taiwan (1)Hong Kong (1)Macau (1)Sichuan (3)Guizhou (2)</p>
    </div> -->

    <x-tab @updateActiveTab="updateActiveTab" class="mt-70" :list="tabs"/>

    <div class="list flex-start mt-44">
      <guidebook-card class="list-item" :guidebook="gb" :class="{'no-mar': (index+1) % 5 === 0}" v-for="(gb, index) in guidebooks" v-bind:key="index" />
    </div>
  </div>
</template>

<script setup>
  import VLazyImage from "v-lazy-image";
  // import Swiper from 'https://unpkg.com/swiper@8/swiper-bundle.esm.browser.min.js'
  import XTab from '@/components/XTab'
  import GuidebookCard from '@/components/GuidebookCard'
  import {AXIOS} from '@/common/http-commons'
  import router from "../../router";
  const sliders = ref([])
  const guidebooks = ref([])
  const pageSize = ref(100)
  const currentPage = ref(1)
  const data = reactive({
    tabs: [
      {id: 0, title: 'Latest Guidebooks'},
      {id: 1, title: 'Most Downloaded Guidebooks'},
    ]
  })
  const {tabs} = toRefs(data)

  function updateActiveTab(index){
    console.log("activating : "+index)
    loadGuideBooks();
  }

  function loadSliders(){
    AXIOS.get("/api/public/pagecontent/guidebookintrosliders").then(function (response) {
      console.log("guidebookintrosliders:")
      console.log(response.data)
      sliders.value = response.data.data;
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  function loadGuideBooks(){
    console.log("loading latest guidebooks...")
    var params = {}
    params.pageSize = pageSize.value;
    params.page = currentPage.value;
    params.query = "",
    AXIOS.get("/api/public/guidebook", {
        params: params,
      }).then(function (response) {
      console.log("all guidebook list:")
      console.log(response.data)
      guidebooks.value = response.data.data.data
      console.log(guidebooks.value )
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  onMounted(() => {
    loadSliders()
    loadGuideBooks()
  })
</script>

<style scoped lang="scss">
  .banner {
    height: 399px;
    overflow: hidden;

    img {
      width: 100%;
      height: auto;
    }
  }

  .text {
    color: rgba(42, 130, 228, 1);
    font-size: 27px;
    text-align: center;
  }

  .info {
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
    line-height: 30px;
  }

  .list {
    flex-wrap: wrap;

    &-item {
      margin-right: 37px;
      margin-bottom: 34px;
    }
  }
</style>