<template>
  <div class="item" style="cursor: pointer;" @click="goPage('/blog-detail/'+scaleItem.blogId)" :class="{'float-item': float}">
    <v-lazy-image class="logo" :src="scaleItem.headImageUrl" src-placeholder="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/tile_224" alt="" />
    <div class="item-content">
      <p class="time">{{ scaleItem.createdTime }}</p>

      <p class="title mt-44"><a :href="'/blog-detail/'+scaleItem.blogId">{{  scaleItem.title }}</a></p>

      <p class="info mt-24">{{ scaleItem.content}}</p>
    </div>
  </div>
</template>
<script setup>
  import VLazyImage from "v-lazy-image";
  import router from "../../../router";
  const PLACEHOLDERURL="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/fit_2048";

  const props = defineProps({
    float: Boolean,
    scaleItem: {
      type: Object
    }
  })
  // function removeHtmlTag(input){
  //   var div = document.createElement("div");
  //   div.innerHTML = input;
  //   var text = div.textContent || div.innerText || "";
  //   return text;
  // }
  function goPage(val){
    router.push({path: val})
  }
</script>
<style scoped lang="scss">
  .item {
    margin-bottom: 20px;
    width: 671px;
    height: 628px;
    border-radius: 8px;
    box-shadow: 0 0 10px 0 rgba(204, 204, 204, 1);
    background: white;
    overflow: hidden;

    &.float-item {
      float: left;
      margin-right: 20px;
    }

    .logo {
      display: block;
      width: 100%;
      height: 321px;
      transition: .3s;

      &:hover {
        transform: scale(1.05);
      }
    }

    &-content {
      padding: 13px 47px 50px;
    }

    .time {
      color: rgba(179, 179, 179, 1);
      font-size: 27px;
    }

    .title {
      color: rgba(86, 85, 85, 1);
      font-size: 35px;
      font-weight: bold;
    }

    .info {
      color: rgba(179, 179, 179, 1);
      font-size: 27px;
    }
  }
</style>