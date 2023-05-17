<template>
  <div class="item" :class="{'float-item': float}" style="cursor: pointer;" @click="goPage('/blog-detail/'+special.blogId)">
    <v-lazy-image class="logo" :src="special.headImageUrl" src-placeholder="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/tile_224" alt="" />
    <div class="item-content">
      <p class="time">{{ special.createdTime }}</p>

      <p class="title mt-20"><a :href="'./blog-detail/'+special.blogId">{{ special.title }}</a></p>

      <p class="info mt-10">{{  removeHtmlTag(special.content).substring(0, 20) }}....</p>
      <p class="more mt-10">Read More</p>
    </div>
  </div>
</template>
<script setup>
  import VLazyImage from "v-lazy-image";
  import router from "../../../router";
  const props = defineProps({
    float: Boolean,
    special: {
      type: Object
    }
  })
  const PLACEHOLDERURL="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/fit_2048";

  function removeHtmlTag(input){
    var div = document.createElement("div");
    div.innerHTML = input;
    var text = div.textContent || div.innerText || "";
    return text;
  }
  function goPage(val){
    router.push({path: val})
  }
</script>
<style scoped lang="scss">
  .item {
    display: inline-block;
    margin-bottom: 20px;
    flex: 0 0 314px;
    width: 314px;
    height: 600px;
    border-radius: 4px;
    box-shadow: 0 0 10px 0 rgba(204, 204, 204, 1);
    overflow: hidden;
    background: white;

    &.float-item {
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
      padding: 26px 31px 20px;
    }

    .time {
      color: rgba(179, 179, 179, 1);
      font-size: 18px;
    }

    .title {
      color: rgba(86, 85, 85, 1);
      font-size: 23px;
      font-weight: bold;
    }

    .info {
      color: rgba(179, 179, 179, 1);
      font-size: 18px;
    }

    .more {
      color: rgba(42, 130, 228, 1);
      font-size: 18px;
    }
  }
</style>