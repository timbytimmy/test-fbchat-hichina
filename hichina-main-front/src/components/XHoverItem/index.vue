<template>
  <div class="item" :class="{radius}" @mouseenter="hoverFlag = true"
       @mouseleave="hoverFlag = false">
    <v-lazy-image src="@/assets/images/wrapper-img-1.jpg" alt="" />
    <div class="notice" :class="{ open: hoverFlag }">
      <slot>
        <p>Sanya</p>
        <span class="info">Hainan</span>
      </slot>
    </div>
  </div>
</template>

<script setup>
  import VLazyImage from "v-lazy-image";
  const props = defineProps({
    radius: {
      type: Boolean,
      default: true
    }
  })
  const hoverFlag = ref(false)
</script>

<style scoped lang="scss">
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

  .info {
    color: rgba(255, 235, 235, 1);
    font-size: 13px;
  }


  img {
    width: 100%;
    height: 100%;
    box-shadow: 0px 7px 7px 0px rgba(0, 0, 0, 0.5);
    object-fit: cover;
  }
</style>