<template>
  <div class="header">
    <div class="intro" >
      <ul class="part flex-between">
        <li :class="{active: active === 0}" @click="setActiveTab(0)">
          <el-icon :size="20">
            <svg-icon icon-class="people" :color="active === 0 ? '#ffffff' : '#2a82e4'"/>
          </el-icon>
          <p>Group Tour</p>
        </li>
        <li :class="{active: active === 1}" @click="setActiveTab(1)">
          <el-icon :size="20">
            <svg-icon icon-class="hotel" :color="active === 1 ? '#ffffff' : '#2a82e4'"/>
          </el-icon>
          <p>Hotel Deals</p>
        </li>
        <li :class="{active: active === 2}" @click="setActiveTab(2)">
          <el-icon :size="20">
            <svg-icon icon-class="airport" :color="active === 2 ? '#ffffff' : '#2a82e4'" />
          </el-icon>
          <p>Flight Deals</p>
        </li>
        <li :class="{active: active === 3}" @click="setActiveTab(3)">
          <el-icon :size="20">
            <svg-icon icon-class="holiday" :color="active === 3 ? '#ffffff' : '#2a82e4'" />
          </el-icon>
          <p>Holiday Package</p>
        </li>
        <li :class="{active: active === 4}" @click="setActiveTab(4)">
          <el-icon :size="20">
            <svg-icon icon-class="cart" :color="active === 4 ? '#ffffff' : '#2a82e4'" />
          </el-icon>
          <p>China Stuff </p>
        </li>
      </ul>

      <ul class="info flex-between">
        <el-input style="margin-top:10px;" v-model="query" placeholder="Search by title" @input="search" />
        <!-- <li>
          <p class="sub flex-center">Going to<el-icon color="#2a82e4" size="16"><ArrowDown /></el-icon></p>
          <p class="title">Changbaishan</p>
        </li> -->
        <!-- <li>
          <p class="sub flex-center">Going to<el-icon color="#2a82e4" size="16"><ArrowDown /></el-icon></p>
          <p class="title">1，May, 2023</p>
        </li> -->
        <!-- <li>
          <p class="sub flex-center">Going to<el-icon color="#2a82e4" size="16"><ArrowDown /></el-icon></p>
          <p class="title">1 Adult(s)<br/>2 Children</p>
        </li> -->
      </ul>
    </div>

    <!-- <p class="search">Search</p> -->
  </div>
  <div class="wrapper">
    <p class="title">
      Deals for you
      <a>all</a>
    </p>

    <div class="list flex-start mt-34">
      <other-product-item class="list-item" :product-summary="item" v-for="(item,index) in globalProductList" v-bind:key="index" :class="{'no-mar': (index+1) % 4 === 0}"/>
    </div>
    <div style="width: 100%;">
      <el-button type="primary" plain class="get-more mt-100" @click="loadMore" style="cursor: pointer;">Click to load more</el-button>
    </div>
  </div>
</template>

<script setup>
  import {AXIOS} from '@/common/http-commons'
  import OtherProductItem from '../home/components/OtherProductItem'
  const active = ref(0)

  const query=ref("")

  const pageSize = ref(100)
  const currentPage = ref(1)
  const productList = ref([])
  const globalProductList = ref([])
  const productTypeId = ref("")
  const TOURPACKAGETYPE= "3a53caed-b788-4290-896d-7922532ad769";
  const HOTELPACKAGETYPE="a9f5adbe-c09b-49bc-a614-8a1c5d5e5337";
  const FLIGTHPACKAGETYPE="d7b95089-e278-4522-8f71-dacac41ba32f";
  const HOLIDAYPACKAGETYPE="e05d07a3-a717-45b8-b009-47a349890e41";
  const CHINALOCALPACKAGETYPE="fd264cab-ee8d-4571-a477-03d7e7c090b3";

  function debounce(cb, delay){
      let timer
      return function(...args){
        if (timer){
          clearTimeout(timer)
        }
        timer = setTimeout(() =>{
          cb.call(this,...args)
        },delay)
      }
  }

  const search = debounce(value =>{
    console.log(value)
    globalProductList.value=[]
    currentPage.value = 1
    loadAllProducts()
  },500)

  function setActiveTab(index){
    globalProductList.value = []
    currentPage.value = 1;
    console.log("activating..."+index)
    active.value = index
    if(active.value==0){
      productTypeId.value = TOURPACKAGETYPE;
    }else if(active.value == 1){
      productTypeId.value = HOTELPACKAGETYPE;
    }else if(active.value == 2){
      productTypeId.value = FLIGTHPACKAGETYPE
    }else if(active.value==3){
      productTypeId.value = HOLIDAYPACKAGETYPE
    }else if(active.value==4){
      productTypeId.value = CHINALOCALPACKAGETYPE;
    }else{
      productTypeId.value=""
    }
    loadAllProducts()
  }

  function loadMore(){
    currentPage.value+=1;
    loadAllProducts()
  }

  function loadAllProducts(){
    var params = {}
    params.pageSize = pageSize.value;
    params.page = currentPage.value;
    params.query = query.value,
    params.productTypeId = productTypeId.value
    AXIOS.get('/api/public/productsku/productskugrouplist', {params: params}).then(function (response) {
      console.log("Got product list of all type : " )
      console.log(response.data.data)
      productList.value = response.data.data.data
      globalProductList.value = globalProductList.value.concat(productList.value)
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  onMounted(() => {
    setActiveTab(0)
      // loadAllProducts()
  })

  const isFive = computed(() => {
    return active.value === 4
  })
</script>

<style scoped lang="scss">
  .header {
    height: 374px;
    padding-top: 116px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(42, 130, 228, 1);
  }

  .intro {
    position: relative;
    margin: 0 auto;
    width: 747px;
    height: 172px;
    padding: 45px 25px 24px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(255, 255, 255, 1);
    border-radius: 10px;
    font-size: 14px;
    border: rgba(169, 169, 169, 1) solid 1px;
    box-shadow: 0px 3px 3px 0px rgba(0, 0, 0, 0.5);

    &.transparent {
      background: transparent;
      box-shadow: none;
      border: none;
    }
  }

  .part {
    position: absolute;
    width: 656px;
    height: 92px;
    padding: 0 22px;
    left: 45px;
    top: -50px;
    background-color: rgba(255, 255, 255, 1);
    border-radius: 16px;
    box-shadow: 0 5px 5px 0 rgba(0, 0, 0, 0.5);
    text-align: center;
    cursor: pointer;
    transition: .3s;

    &.down {
      top: 50px;
    }

    li {
      width: 20%;
      height: 100%;
      padding: 22px 0 15px;
      color: rgba(42, 130, 228, 1);
      font-size: 14px;

      &.active {
        color: white;
        background: rgba(42, 130, 228, 1);
      }
    }
  }

  .info {
    text-align: center;
    align-items: stretch;
    li {
      width: calc(100% / 3);
    }

    .title {
      margin-top: 21px;
      color: rgba(80, 80, 80, 1);
      font-size: 23px;
      font-weight: bold;
    }

    .sub {
      color: rgba(79, 79, 79, 1);
      font-size: 14px;
    }

    .el-icon {
      margin-left: 8px;
    }
  }

  .search {
    margin: 22px auto 0;
    width: 257px;
    height: 48px;
    color: rgba(42, 130, 228, 1);
    background-color: rgba(250, 250, 250, 1);
    box-shadow: rgba(42, 130, 228, 1) solid 1px;
    border-radius: 8px;
    font-size: 19px;
    line-height: 48px;
    border: rgba(250, 250, 250, 1) solid 1px;
    text-align: center;
  }

  .wrapper {
    padding-top: 102px;
  }

  .title {
    position: relative;
    color: rgba(42, 130, 228, 1);
    font-size: 32px;
    text-align: center;

    a {
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-50%);
      font-size: 20px;
    }
  }

  .list {
    flex-wrap: wrap;

    &-item {
      margin-right: 47px;
      margin-bottom: 20px;
    }
  }
  .get-more {
    margin: 0 auto;
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
    width: 100%;
    text-align: center;
  }
</style>