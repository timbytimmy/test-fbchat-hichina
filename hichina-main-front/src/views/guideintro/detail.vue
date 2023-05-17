<template>
  <div class="head flex-between stretch">
    <v-lazy-image class="logo" src="@/assets/images/intro/logo.jpg" alt="" />
    <div class="flex-item">
      <div class="name flex-between">
        <p>Zhangye</p>
        <span class="count">6519154 Downloads</span>
      </div>
      <div class="info mt-16">Zhangye is one of the cities on the ancient Silk Road. Zhangye offers a whole range of unique places. Among them there is Zhangye National Geological Park, which is ranked as one of the world\'s top ten magical places by the National Geographic magazine.</div>
      <p class="click mt-10">Click Here to download</p>
      <p class="guide mt-6">PDF Guidebook</p>
    </div>
  </div>

  <p class="text mt-68">Relevant Destinations</p>

  <div class="wrapper-list flex-between mt-30">
    <x-hover-item class="wrapper-item" :radius="false" v-for="i in 3">
      <p class="wrapper-title">Gansu</p>
    </x-hover-item>
  </div>

  <p class="text mt-40">Relevant Tours</p>

  <div class="wrapper">
    <div class="list flex-start mt-36">
      <other-product-item class="product" :class="{ 'no-mar': i % 4 === 0 }" v-for="i in 4"/>
    </div>
  </div>

  <p class="text mt-40">Relevant Blogs</p>

  <div class="wrapper">
    <div class="list flex-start mt-36">
      <product-item class="product" :class="{ 'no-mar': i % 4 === 0 }" v-for="i in 4"/>
    </div>
  </div>

  <p class="more mt-32">鼠标滑下加载下一栏</p>

</template>

<script setup>
  import VLazyImage from "v-lazy-image";
  import ProductItem from '../home/components/ProductItem'
  import XHoverItem from '@/components/XHoverItem'
  import OtherProductItem from '../home/components/OtherProductItem'
</script>

<style scoped lang="scss">
  .head {
    margin: 40px auto 68px;
    width: 1563px;
  }

  .logo {
    margin-right: 33px;
    width: 675px;
    height: 363px;
    border-radius: 17px;
  }

  .name {
    color: rgba(80, 80, 80, 1);
    font-size: 55px;
  }

  .count {
    color: rgba(42, 130, 228, 1);
    font-size: 32px;
  }

  .info {
    padding: 35px 25px 60px 17px;
    height: 179px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(223, 243, 255, 1);
    border-radius: 6px;
    font-size: 14px;
  }

  .click {
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
  }

  .guide {
    width: 288px;
    height: 48px;
    color: rgba(255, 255, 255, 1);
    background-color: rgba(42, 130, 228, 1);
    border-radius: 6px;
    font-size: 14px;
    text-align: center;
    line-height: 48px;
    border: none;
    cursor: pointer;
  }

  .text {
    color: rgba(42, 130, 228, 1);
    font-size: 24px;
    text-align: center;
  }

  .wrapper-list {
    width: 1190px;
    margin: 0 auto;
  }

  .wrapper-item {
    width: 370px;
    height: 197px;
  }

  .wrapper-title {
    padding-top: 16px;
    color: white;
    font-size: 26px;
    text-align: center;
  }

  .list {
    flex-wrap: wrap;

    .product {
      margin-right: 48px;
    }
  }

  .more {
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
    text-align: center;
  }
</style>