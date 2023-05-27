<template >
<div class="scrollbar">
  <div class="swiper">
    <div class="swiper-wrapper">
      <swiper-container  autoplay-delay="2000" loop="true" css-mode="true" navigation="true" pagination="true" scrollbar="true">
        <swiper-slide class="banner " v-for="(item,index) in sliders " :key="index" @click="goToBlog(item.linkToBlog)" >
          <!-- <p class="title">fffffffff</p>
          <p class="info">{{ item.subTitle }}</p> -->
          <v-lazy-image :src="item.imageUrl" alt="" />
          <div class="card-overlay">
            <h3 class="card-title">{{ item.title }}</h3>
            <p class="card-description">{{ item.subTitle }}</p>
          </div>
        </swiper-slide>
      </swiper-container>
    </div>
  </div>

  <section class="wrapper mt-84">
    <div class="item-title pd-20 flex-between">
      <p>Destinations</p>
      <p class="more-destinations flex-between" @click="goPage('/destination')">More destinations
        <el-icon>
          <ArrowRight/>
        </el-icon>
      </p>
    </div>
    <p class="bat pd-20">Places to go in China!</p>

    <div class="wrapper-img flex-between mt-38">
      <!-- <x-hover-item class="wrapper-item" v-for="i in 6"/> -->
      <div  v-for="item in randDestinations" :key="item.destinationId" class="item radius wrapper-item" @mouseenter="hoverFlag = true"
           @mouseleave="hoverFlag = false">
        <v-lazy-image class="destination-profile" @click="goPage('/destination-detail/'+item.destinationId)" :src="normalizeMultiImageUrl(item.destinationProfileImage)" alt="" />
        <div class="notice" :class="{ open: hoverFlag }">
          <slot>
            <p>{{item.destinationName}}</p>
            <span class="parent-info">{{item.parentDestinationName}}</span>
          </slot>
        </div>
      </div>
    </div>

    <div class="pd-20">
      <button class="help">Want a Tailor Made Trip to China? Click Here for help</button>
    </div>

    <div class="banner-center pd-20 mt-20">
      <div class="title">
        <p>Editor's Choice</p>
        <p class="info">What's New this Week?</p>
      </div>

      <v-lazy-image style="cursor: pointer;" @click="goToBlog(homePostLink)" :src="homePostImageUrl" alt="" />
    </div>

    <div class="item-title pd-20 flex-between mt-36">
      <p>People are traveling</p>
    </div>
    <p class="bat pd-20">What's in China</p>
  </section>

  <div class="float pd-20 mt-28" v-loading="loading">
    <div v-for="(item, index) in globalUnifiedItemList" v-bind:key="index">
      <div v-if="item.type === 'video'" class="video-content">
        <video src="http://mp42.china.com.cn/video_tide/video/2023/3/13/20233131678679710349_367_2.mp4" controls></video>
        <p class="video-info">From Blog Title My 30 days of Going to Tibet</p>
      </div>
      <blog-item class="product-item" :blog="item.value" v-if="item.type==='blog'" float/>
      <other-product-item :productSummary="item.value" :showIcon="true" class="other-item" v-if="item.type === 'product'" float/>
      <special-product-item class="scale-item" :special="item.value" v-if="item.type==='scaleblog'" float/>
    </div>
  </div>
  

  <!-- <div style="width: 100%;">
    <el-button type="primary" plain class="get-more mt-100" @click="loadMore" style="cursor: pointer;">Click to load more</el-button>
  </div> -->
</div>
  
</template>
<script setup>
  import VLazyImage from "v-lazy-image";
  import BlogItem from '../home/components/BlogItem'
  import {AXIOS} from '@/common/http-commons'
  import ProductItem from './components/ProductItem'
  import XHoverItem from '@/components/XHoverItem'
  import OtherProductItem from './components/OtherProductItem'
  import SpecialProductItem from './components/SpecialProductItem'
  import { useRouter } from 'vue-router';
  import { debounce } from 'lodash';

  const sliders = ref([])
  const randDestinations = ref([])

  const activeIndex = ref(1)
  const imgPopupShowFlag = ref([false, false, false, false, false, false])
  const router = useRouter()

  const currentPage = ref(1)

  const loading= ref(false)

  const blogPageSizePage = ref(30)
  const productPageSizePage = ref(5)

  const bloglist =ref([])
  const productlist = ref([])

  const totalBlogCount=ref(-1)

  const globalUnifiedItemList = ref([])
  const unifiedItemList = ref([])

  const homePostLink = ref("")
  const homePostImageUrl = ref("")

  function loadMore(){
    currentPage.value+=1
    var maxPage = totalBlogCount.value/blogPageSizePage.value
    if(currentPage.value<=maxPage+1){
      loadBlogList()
    }
  }

  function normalizeMultiImageUrl(input){
    if(input.indexOf(',')>-1){
       return input.split(',').shift();
     }else if(input.indexOf(';')>-1){
      return input.split(';').shift();
     }
     return input;
  }

  function loadBlogList(){
    loading.value = true;
    unifiedItemList.value=[]
    var params = {}
    params.pageSize = blogPageSizePage.value;
    params.query="";
    console.log("loading page: "+currentPage.value)
    params.page = currentPage.value;
    AXIOS.get("/api/public/blog/list", {params: params}).then(function (response) {
      if(totalBlogCount.value==-1){
        totalBlogCount.value = response.data.data.total
      }
      bloglist.value = response.data.data.data
      for(var index in bloglist.value){
        var obj = {}
        // obj.type=parseInt(Math.random() * 2)==0?'blog':'scaleblog';
        obj.type='blog';
        obj.value = bloglist.value[index]
        unifiedItemList.value.push(obj)
      }
      loadProducts();
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  function shuffleArray(anyarr){
    if(anyarr==null || anyarr.length<=1){
      return
    }else{
      for (let i = 0; i < 20; i++) {
        var left = parseInt(Math.random() * anyarr.length )
        var right = parseInt(Math.random() * anyarr.length )

        var tmp = anyarr[left]
        anyarr[left]=anyarr[right]
        anyarr[right]=tmp
      }
    }
  }

  function loadProducts(){
    var params = {}
    params.pageSize = productPageSizePage.value;
    params.query="";
    params.page = currentPage.value;
    params.productTypeId=""
    AXIOS.get("/api/public/productsku/productskugrouplist", {params: params}).then(function (response) {
      // console.log("product list:")
      // console.log(response.data)
      productlist.value = response.data.data.data

      for(var index in productlist.value){
        var obj = {}
        obj.type='product';
        obj.value = productlist.value[index]
        unifiedItemList.value.push(obj)
      }

      // console.log("unifiedItemList.value")
      // console.log(unifiedItemList.value)

      // shuffleArray(unifiedItemList.value)
      globalUnifiedItemList.value=globalUnifiedItemList.value.concat(unifiedItemList.value)
      // console.log("globalUnifiedItemList.value")
      // console.log(globalUnifiedItemList.value)
      loading.value = false;
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  function goPage(val){
    router.push({ path: val })
  }

  function loadHomeSliders(){
    AXIOS.get('/api/public/pagecontent/homesliders').then(response=>{
       sliders.value = response.data.data
       console.log("sliders.value")
       console.log(sliders.value)
       loadBlogList()
    }).catch(e=>{
      console.log(e)
    })
  }

  function loadHomePost(){
    AXIOS.get('/api/public/pagecontent/homepost').then(response=>{
      console.log("loaded home post: ")
       console.log(response.data.data)
       homePostLink.value = response.data.data.postLink;
       homePostImageUrl.value = response.data.data.postImageUrl;
    }).catch(e=>{
      console.log(e)
    })
  }

  function loadRand6Destinations(){
    AXIOS.get('/api/public/destination/rand6').then(response=>{
      randDestinations.value = response.data.data
    }).catch(e=>{
      alert(e)
    })
  }

  onMounted(() => {
    // load slider
    loadHomeSliders()

    loadHomePost()

    // load destination
    loadRand6Destinations()

    getNextBatch()
  })

  const hoverFlag = ref(false)
  const arr = getRandomArr()

  function getRandomArr() {
    // 这是你xiaopiu上的默认布局，下面的20个是随机生成
    const arr = [0, 1, 1, 3, 2, 2, 2, 2, 2, 2]
    for (let i = 0; i < 20; i++) {
      arr.push(parseInt(Math.random() * 4 ))
    }
    return arr
  }

  function goToBlog(url){
    window.location.href = url
  }

  function getNextBatch() {
    window.onscroll = debounce(function() {
      let bottomOfWindow = document.documentElement.scrollTop + window.innerHeight + 100 > document.documentElement.scrollHeight;
     
      if(bottomOfWindow){
        loadMore()
      }
    }, 500)
  }

</script>


<style scoped lang="scss">

  .scrollbar{
      overflow: scroll;
  }
  .float {
    max-width: $min-width;
    margin: 0 auto;
    overflow: hidden;

    &::after {
      height: 0;
      content: '';
      display: block;
      clear: both;
    }
  }


  .banner {
    cursor: pointer;
    position: relative;
    height: 547px;
    overflow: hidden;

    .title {
      position: absolute;
      left: 107px;
      top: 175px;
      // color: rgba(255, 255, 255, 1);
      color: red;
      font-size: 24px;
      line-height: 150%;
      text-align: left;
    }

    .info {
      position: absolute;
      width: 498px;
      height: 63px;
      left: 112px;
      top: 342px;
      color: rgba(255, 255, 255, 1);
      font-size: 21px;
      line-height: 150%;
      text-align: left;
    }

    img {
      width: 100%;
      height: auto;
      max-height: 547px;
    }
  }

  .bat {
    color: rgba(80, 80, 80, 1);
    font-size: 48px;
    line-height: 150%;
  }

  .wrapper-img {

    .wrapper-item {
      width: 208px;
      height: 276px;
    }
  }

  .help {
    margin-top: 73px;
    width: 100%;
    height: 78px;
    left: 260px;
    top: 293px;
    text-indent: 31px;
    color: rgba(255, 255, 255, 1);
    background-color: rgba(42, 130, 228, 1);
    border-radius: 14px;
    font-size: 22px;
    line-height: 150%;
    text-align: center;
    font-weight: bold;
  }

  .video {
    float: left;
    margin-bottom: 20px;
    width: 997px;
    height: 595px;
  }

  .banner-center {
    position: relative;
    height: 192px;

    img {
      width: 100%;
      height: 100%;
    }

    .title {
      position: absolute;
      top: 29px;
      left: 55px;
      color: rgba(255, 255, 255, 1);
      font-size: 66px;
      font-weight: bold;
    }

    .info {
      color: rgba(255, 255, 255, 1);
      font-size: 24px;
      font-weight: 400;
    }
  }

  .list {
    flex-wrap: wrap;

    .product {
      margin-right: 25px;
    }
  }

  .get-more {
    margin: 0 auto;
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
    width: 100%;
    text-align: center;
  }
  .item {
    position: relative;
    width: 244px;
    height: 324px;
    overflow: hidden;

    &.radius {
      border-radius: 10px;
    }
  }
  .notice {
    position: absolute;
    width: 100%;
    height: 74px;
    padding: 17px 14px 7px;
    left: 0;
    bottom: -74px;
    color: white;
    background-color: rgba(153, 153, 153, .51);
    border-radius: 2px;
    font-size: 23px;
    transition: .3s;
    line-height: 23px;

    &.open {
      bottom: 0;
    }
  }
  .parent-info {
    color: rgba(255, 235, 235, 1);
    font-size: 13px;
  }


  .destination-profile {
    cursor: pointer;
    width: 100%;
    height: 100%;
    box-shadow: 0px 7px 7px 0px rgba(0, 0, 0, 0.5);
    object-fit: cover;
  }

  

  .more-destinations{
    cursor: pointer;
    color: rgba(42, 130, 228, 1);
    font-size: 14px;
    line-height: 150%;
  }

  .card-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;

    background-color: rgba(0, 0, 0, 0.1); /* Adjust the overlay color as needed */
    color: #fff; /* Adjust the text color as needed */
    padding: 50px; /* Adjust the padding as needed */
    box-sizing: border-box;
  }

  .card-title {
    font-size: 150px; /* Adjust the title font size as needed */
    margin-bottom: 10px; /* Adjust the title margin as needed */
  }

  .card-description {
    font-size: 24px; /* Adjust the description font size as needed */
  }

</style>
