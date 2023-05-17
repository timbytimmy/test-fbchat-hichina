<template>
  <div class="item" :class="{'float-item': float}">
    <v-lazy-image class="logo" src="@/assets/images/p.jpg" src-placeholder="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/tile_224" alt="" />
    <div class="item-content">
      <p class="time">26 December 2021</p>

      <p class="title mt-10">===Product My 30 Days of Going to Tibet</p>

      <p class="info mt-10">I spent 30 days of going to Tibet and found
        what I love to see there in Lhasa and xi....</p>
    </div>
  </div>
</template>
<script setup>
  import VLazyImage from "v-lazy-image";
  const props = defineProps({
    float: Boolean
  })
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