<template>
  <div class="item" :class="{'float-item': float}" style="cursor: pointer;" @click="goPage('/product-detail/'+productSummary.skuGroupId)">
    <v-lazy-image class="logo" :src="productSummary.imageUrl" src-placeholder="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/tile_224" alt="" />
    <div class="item-content">
      <p class="time">Start from ¥ {{ productSummary.minPrice }}</p>
      <p class="title mt-20"><a :href="'./product-detail/'+productSummary.skuGroupId">{{ productSummary.skuGroupName }}</a></p>
    </div>
  </div>
</template>
<script setup>
  import VLazyImage from "v-lazy-image";
  import router from "../../../router";
  const props = defineProps({
    float: Boolean,
    productSummary: {
      type: Object
    }
  })
  function goPage(val){
    router.push({path: val})
  }
</script>
<style scoped lang="scss">
  .item {
    margin-bottom: 30px;
    width: 314px;
    height: 295px;
    border-radius: 4px;
    box-shadow: 0 0 10px 0 rgba(204, 204, 204, 1);
    overflow: hidden;
    background: white;

    &.float-item {
      display: inline-block;
      float: left;
      margin-left: 10px;
      margin-right: 10px;
    }

    .logo {
      display: block;
      width: 100%;
      height: 151px;
      transition: .3s;

      &:hover {
        transform: scale(1.05);
      }
    }

    &-content {
      padding: 22px 20px 35px;
    }

    .time {
      color: blue;
      font-size: 17px;
      text-align: center;
    }

    .title {
      color: rgba(86, 85, 85, 1);
      font-size: 17px;
      font-weight: bold;
    }
  }
</style>