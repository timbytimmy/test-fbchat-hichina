<template>
  <div class="item">
    <p class="time"> Order No. {{ orderSummary.orderId }} 丨 Created on: {{ orderSummary.createdTime }}</p>
    <div class="content flex-between mt-4">
      <p class="name">{{ orderSummary.productName }}</p>
      <p class="type">{{ orderSummary.packageCategory }}</p>
      <p class="order-time">{{ orderSummary.selectedDate }}</p>
      <p class="price">CNY {{ orderSummary.totalPrice }}</p>
      <p class="pat-way">{{ orderSummary.orderStatus }}</p>
      <div class="detail">
        <el-button @click="goOrderDetail(orderSummary.orderId)">Order Details</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
  import router from "../../../router";
  const props = defineProps({
    orderSummary: {
      type: Object
    }
  })

  function goOrderDetail(orderId){
    console.log("order detail coming...")
    console.log("calling detail of : "+ orderId)
    router.push({path: '/order-detail/'+orderId})
  }
</script>

<style scoped lang="scss">
  .item {
    margin-bottom: 12px;
  }

  .time {
    height: 30px;
    padding-left: 12px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(214, 214, 214, 1);
    font-size: 14px;
    line-height: 30px;
    text-align: left;
  }

  .content {
    padding: 18px 46px 18px 15px;
    border: rgba(184, 181, 181, 1) solid 1px;
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
  }

  .name {
    width: 225px;
  }

  .detail {
    flex: 0 0 120px;
    height: 31px;

    button {
      width: 100%;
      color: rgba(255, 253, 253, 1);
      background-color: rgba(255, 141, 26, 1);
      border-radius: 7px;
      font-size: 14px;
      text-align: center;
    }
  }
</style>