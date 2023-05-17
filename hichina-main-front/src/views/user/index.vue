<template>
  <div class="head">
    <ul class="tab flex-start">
      <li :class="{active: active === index}" v-for="(title, index) in list" @click="activateTab(index)">{{title}}</li>
    </ul>

    <div class="user-info">
      <v-lazy-image class="avatar" :src="profileImageUrl" alt="" />
      <el-icon size="24" style="cursor: pointer;" @click="pressSetting">
        <svg-icon icon-class="setting"/>
      </el-icon>
      <p class="name mt-8">{{ username }}</p>
      <div class="follow flex-between">
        <div class="follow-item">
          <p>-1</p>
          <p>Following</p>
        </div>
        <div class="follow-item">
          <p>-1</p>
          <p>Followers</p>
        </div>
      </div>
    </div>
  </div>

  <div class="main" v-if="active === 0">
    <div>
      <blog-item :blog-summary="i" @refreshBlogList="loadMyBlogs" v-for="i in bloglist"/>
      <el-pagination layout="prev, pager, next" :current-page="blogCurrentPage" @current-change="blogPageChange" :total="totalCnt" :page-size="pageSize" />
    </div>
  </div>
  <div class="main" v-if="active === 1">
    <!--我的收藏-->
    <v-lazy-image class="logo" src="@/assets/images/banner-1.jpg" alt="" />

    <p class="title">Qinglai Lake Travel 2023</p>
    <ul class="nums flex-start">
      <li class="flex-start">
        <el-icon size="24">
          <svg-icon icon-class="eye-open" color="#2a82e4"/>
        </el-icon>
        114514
      </li>
      <li class="flex-start">
        <el-icon size="24">
          <svg-icon icon-class="book" color="#2a82e4"/>
        </el-icon>
        114514
      </li>
      <li class="flex-start">
        <el-icon size="24">
          <svg-icon icon-class="clock" color="#2a82e4"/>
        </el-icon>
        11-Mar-2023
      </li>
    </ul>
  </div>
  <div v-if="active === 2">
    <!-- 我的订单列表 -->
    <div class="list">
      <order-item :order-summary="i" v-for="i in orderSummaries"/>
      <el-pagination layout="prev, pager, next" :current-page="currentPage" @current-change="pageChange" :total="totalCnt" :page-size="pageSize" />
    </div>
  </div>
</template>

<script setup>
  import VLazyImage from "v-lazy-image";
  import router from "../../router";
  import {AXIOS} from '@/common/http-commons'
  import OrderItem from './components/OrderItem'
  import BlogItem from './components/BlogItem'
  const username = ref("")
  const active = ref(0)
  const list = ['My Blogs', 'My Collection', 'My Orders']
  const profileImageUrl = ref("")
  const orderSummaries = ref([])
  const totalCnt = ref(0)
  const pageSize = ref(100)
  const currentPage = ref(1)
  const blogCurrentPage = ref(1)
  const bloglist = ref([])

  const route = useRoute();

  function blogPageChange(val){
    console.log("val to change ...")
    console.log(val)
    blogCurrentPage.value = val
    loadMyBlogs()
  }
  function pageChange(val){
    console.log("val to change ...")
    console.log(val)
    currentPage.value = val

    loadMyOrderByPage()
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

  function getMyPrincipal(){
    AXIOS.get("/api/v1/user/mysecurityinfo")
    .then(function (response) {
        console.log("my security data:")
        console.log(response.data.data)
        profileImageUrl.value = response.data.data.profileImageUrl;
        username.value = response.data.data.username;
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  onMounted(() => {
    if(route.params.active==null){
      route.params.active = 0
    }
    console.log("come to user page with active tab: "+ route.params.active)
    active.value = parseInt(route.params.active)
    activateTab(active.value)
    whoami();
    getMyPrincipal();
  })

  function loadMyBlogs(){
    var params = {}
    params.pageSize = pageSize.value;
    params.page = blogCurrentPage.value;
    params.query = "",
    AXIOS.get("/api/v1/blog/mybloglist", {
      params: params,
    })
    .then(function (response) {
      console.log("Got my blogs summary: " )
      console.log(response.data.data)
      bloglist.value = response.data.data.data
      totalCnt.value = response.data.data.total
      pageSize.value = response.data.data.pageSize
      blogCurrentPage.value = response.data.data.currentPage
    })
    .catch(function (error) {
      console.log("currently not logged in my order page: "+error);
      router.push("/auth/login")
    });
  }

  function loadMyOrderByPage(){
    var params = {}
    params.pageSize = pageSize.value;
    params.page = currentPage.value;
    params.query = "",
    AXIOS.get("/api/v1/order/myorders", {
      params: params,
    })
    .then(function (response) {
      console.log("Got my orders summary: " )
      console.log(response.data.data)
      orderSummaries.value = response.data.data.data
      totalCnt.value = response.data.data.total
      pageSize.value = response.data.data.pageSize
      currentPage.value = response.data.data.currentPage
    })
    .catch(function (error) {
      console.log("currently not logged in my order page: "+error);
      router.push("/auth/login")
    });
  }

  function activateTab(index){
    active.value = index
    if(index == 0){
      console.log("my blogs loaded...")
      loadMyBlogs()
    }
    if(index == 2){
      console.log("my order activated...")
      loadMyOrderByPage()
    }
  }

  // watch(() => active.value, (val) => {
  //   if (val !== '') {
  //     showUserInfo.value = false
  //   }
  // })

  function pressSetting() {
    router.push({name: 'user-info'})
  }
</script>

<style scoped lang="scss">
  .head {
    position: relative;
    height: 75px;
    padding-left: 409px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(231, 231, 231, 1);
    font-size: 14px;
    line-height: 75px;
    box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.5);
    text-align: center;
  }

  .tab {
    color: rgba(80, 80, 80, 1);
    font-size: 16px;
    cursor: pointer;

    li {
      margin-right: 50px;
      text-align: center;
    }

    .active {
      color: blue;
    }
  }

  .user-info {
    position: absolute;
    max-width: 20%;
    min-width: 259px;
    height: 261px;
    padding-top: 100px;
    left: 0px;
    top: 75px;
    background-color: rgba(233, 233, 233, 1);
    line-height: 20px;

    &::after {
      position: absolute;
      display: block;
      content: '';
      width: 60px;
      height: 1px;
      left: 50%;
      transform: translateX(-50%);
      bottom: 80px;
      background: #999999;
    }

    .avatar {
      position: absolute;
      width: 150px;
      height: 150px;
      left: 50%;
      transform: translateX(-50%);
      top: -50px;
    }

    .name {
      color: rgba(80, 80, 80, 1);
      font-size: 14px;
      line-height: 20px;
    }

    .follow {
      margin-top: 21px;
      color: rgba(80, 80, 80, 1);
      font-size: 14px;
      line-height: 30px;

      &-item {
        width: 50%;
        text-align: center;
      }
    }
  }

  .main {
    width: 55%;
    min-height: 500px;
    margin: 0 auto;
    font-size: 20px;
    background: #ebe7e7;

    .logo {
      width: 100%;
      height: 460px;
    }

    .title {
      height: 48px;
      color: rgba(80, 80, 80, 1);
      font-size: 32px;
      line-height: 48px;
    }

    .nums {
      color: rgba(80, 80, 80, 1);
      font-size: 14px;

      li {
        margin-right: 24px;
      }
    }
  }

  .list,
  .info {
    max-width: 60%;
    margin: 0 auto;
    padding-top: 18px;
  }

  .text {
    margin-bottom: 11px;
    color: rgba(42, 130, 228, 1);
    font-size: 22px;
  }
</style>