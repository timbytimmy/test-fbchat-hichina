<template>
  <p class="title mt-24">Blogs of the Week</p>

  <div class="wrapper mt-12">
    <div class="swiper">
      <div class="swiper-wrapper">
        <swiper-container  autoplay-delay="3000" loop="true" css-mode="true" navigation="true" pagination="true" scrollbar="true">
          <swiper-slide style="cursor: pointer" class="banner swiper-slide" v-for="(item,index) in sliders " :key="index" >
            <p class="title">{{ item.title }}</p>
            <p class="info"> {{ item.subTitle }} </p>
            <v-lazy-image :src="item.imageUrl" @click="redirect(item.linkToBlog)" alt="" />
          </swiper-slide>
        </swiper-container>
      </div>
    </div>

    
    <el-row   justify="center">
      <el-button class="write login-button"
        type="primary"
        @click="goPage('/blog-create')"
      >Write your blog</el-button>
    </el-row>

    <x-tab class="mt-60" :list="list"/>
  </div>

  <div class="float mt-28">
    <template v-for="(item, index) in globalUnifiedItemList" v-bind:key="index">
      <div v-if="item.type === 'video'" class="video-content">
        <video src="http://mp42.china.com.cn/video_tide/video/2023/3/13/20233131678679710349_367_2.mp4" controls></video>
        <p class="video-info">From Blog Title My 30 days of Going to Tibet</p>
      </div>
      <blog-item class="product-item" :blog="item.value" v-if="item.type==='blog'" float/>
      <!-- <other-product-item :productSummary="item.value" class="other-item" v-if="item.type === 'product'" float/> -->
      <!-- <scale-product-item class="scale-item" :scaleItem="item.value" v-if="item.type==='scaleblog'" float/> -->
    </template>
  </div>

  <div style="width: 100%;">
    <el-button type="primary" plain class="get-more mt-100" @click="loadMore" style="cursor: pointer;">Click to load more</el-button>
  </div>

</template>

<script setup>
  import VLazyImage from "v-lazy-image";
  import XTab from '@/components/XTab'
  import BlogItem from '../home/components/BlogItem'
  import OtherProductItem from '../home/components/OtherProductItem'
  import ScaleProductItem from '../home/components/ScaleProductItem'
  import { useRouter } from 'vue-router';
  import {AXIOS} from '@/common/http-commons'
  const data = reactive({
    list: [
      {id: 0, title: 'Latest Blogs'},
      {id: 1, title: 'Most Viewed in a month'},
      {id: 2, title: 'Bloggers I am following'},
    ]
  })

  const currentPage = ref(1)
  const blogPageSizePage = ref(20)
  
  const productPageSizePage = ref(5)

  const sliders = ref([])
  const bloglist =ref([])
  const productlist = ref([])

  const unifiedItemList = ref([])
  const globalUnifiedItemList = ref([])

  const totalBlogCount = ref(0)

  function redirect(url){
    window.location = url;
  }

  function loadMore(){
    currentPage.value+=1
    var maxPage = totalBlogCount.value/blogPageSizePage.value
    if(currentPage.value<=maxPage+1){
      loadBlogList()
    }
  }

  function loadBlogList(){
    unifiedItemList.value = []
    var params = {}
    params.pageSize = blogPageSizePage.value;
    params.query="";
    params.page = currentPage.value;
    AXIOS.get("/api/public/blog/list", {params: params}).then(function (response) {
      bloglist.value = response.data.data.data
      totalBlogCount.value = response.data.data.total;

      // for(let i = 0; i < 100; i++){
      //   console.log()
      // }
      for(var index in bloglist.value){
        var obj = {}
        // obj.type=parseInt(Math.random() * 2)==0?'blog':'scaleblog';
        obj.type='blog'
        obj.value = bloglist.value[index]
        unifiedItemList.value.push(obj)
      }
      globalUnifiedItemList.value=globalUnifiedItemList.value.concat(unifiedItemList.value)

      // loadProducts();
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
      console.log("product list:")
      console.log(response.data)
      productlist.value = response.data.data.data

      for(var index in productlist.value){
        var obj = {}
        obj.type='product';
        obj.value = productlist.value[index]
        unifiedItemList.value.push(obj)
      }

      console.log("unifiedItemList.value")
      console.log(unifiedItemList.value)

      globalUnifiedItemList.value=globalUnifiedItemList.value.concat(unifiedItemList.value)

      // shuffleArray(unifiedItemList.value)
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  function loadSliders(){
    AXIOS.get("/api/public/pagecontent/bloghomesliders").then(function (response) {
      console.log("blog sliders:")
      console.log(response.data)
      sliders.value = response.data.data;
      // console.log("sliders.value")
      // console.log(sliders.value)
      loadBlogList();
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  function loadVideos(){
    console.log("...loading video")
  }

  onMounted(() => {
    loadVideos();
    loadSliders();
  })

  const {list} = toRefs(data)
  const router = useRouter()

  function goPage(val){
    router.push({ path: val })
  }


  const arr = getRandomArr()

  function getRandomArr() {
    // 这是你xiaopiu上的默认布局，下面的20个是随机生成
    const arr = [0, 1, 1, 2, 2, 2, 2, 3]
    for (let i = 0; i < 20; i++) {
      arr.push(parseInt(Math.random() * 4 ))
    }
    return arr
  }
</script>

<style scoped lang="scss">
  .float {
    max-width: 1400px;
    margin: 0 auto;
    overflow: hidden;

    &::after {
      height: 0;
      content: '';
      display: block;
      clear: both;
    }

    .product-item,
    .other-item {
      width: 327px;
      margin-right: 18px;
      margin-bottom: 20px;
      margin-left: 0;
    }
  }

  .title {
    color: rgba(42, 130, 228, 1);
    font-size: 32px;
    text-align: center;
  }

  .banner {
    position: relative;
    height: 547px;
    overflow: hidden;

    .title {
      position: absolute;
      left: 107px;
      top: 175px;
      color: rgba(255, 255, 255, 1);
      font-size: 111px;
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
    }
  }

  .write {
    cursor: pointer;
    margin: 36px auto 0;
    width: 369px;
    height: 61px;
    color: rgba(243, 243, 243, 1);
    background-color: rgba(42, 130, 228, 1);
    border-radius: 8px;
    font-size: 18px;
    line-height: 61px;
    text-align: center;
  }

  .video-content {
    float: left;
    margin-left: 0;
    margin-right: 20px;
    margin-bottom: 20px;
    width: 1020px;
    height: 602px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(250, 250, 250, 1);
    border-radius: 7px;
    font-size: 14px;
    box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.5);
    text-align: center;

    video {
      display: block;
      width: 100%;
      height: 537px
    }

    .video-info {
      padding: 16px 0 14px 19px;
      color: rgba(80, 80, 80, 1);
      font-size: 19px;
      text-align: left;
      font-weight: bold;
    }
  }

  .product-item {
    margin-bottom: 12px;
  }

  .list {
    margin-right: 21px;
    flex: 1;
    flex-wrap: wrap;

    &-item {
      margin-right: 40px;
      margin-bottom: 31px;
    }
  }

  .special-item {
    width: 669px;
  }

  .more {
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
    text-align: center;
  }
  .get-more {
    margin: 0 auto;
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
    width: 100%;
    text-align: center;
  }
</style>