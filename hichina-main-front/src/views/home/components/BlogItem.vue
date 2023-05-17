<template>
  <div class="item" style="cursor: pointer;" @click="goPage('/blog-detail/'+blog.blogId)" :class="{'float-item': float}">
    <v-lazy-image class="logo" :src="blog.headImageUrl" src-placeholder="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/tile_224" alt="" />
    <div class="item-content">
      <p class="time">{{ blog.createdTime }}</p>

      <p class="title mt-10"><a :href="'./blog-detail/'+blog.blogId">{{ blog.title }}</a></p>

      <p class="info mt-10">{{ blog.content }}</p>
    </div>
  </div>
</template>
<script setup>
  import VLazyImage from "v-lazy-image";
  import router from "../../../router";

  const PLACEHOLDERURL="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/fit_2048";
  const props = defineProps({
    float: Boolean,
    blog: {
      type: Object
    }
  })
  function goPage(val){
    router.push({path: val})
  }

  // function removeHtmlTag(input){
  //   var div = document.createElement("div");
  //   div.innerHTML = input;
  //   var text = div.textContent || div.innerText || "";
  //   return text;
  // }
</script>
<style scoped lang="scss">
  .item {
    margin-bottom: 30px;
    width: 314px;
    height: 295px;
    border-radius: 4px;
    box-shadow: 0 0 10px 0 rgba(204, 204, 204, 1);
    background: white;
    overflow: hidden;

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
      padding: 6px 20px 22px;
    }

    .time {
      color: rgba(179, 179, 179, 1);
      font-size: 13px;
    }

    .title {
      color: rgba(86, 85, 85, 1);
      font-size: 17px;
      font-weight: bold;
    }

    .info {
      color: rgba(179, 179, 179, 1);
      font-size: 13px;
    }
  }
</style>