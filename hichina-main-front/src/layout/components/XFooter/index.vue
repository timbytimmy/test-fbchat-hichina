<template>
  <div class="footer">
    <div class="menu">
      <p class="menu-title">About HiChinaTravel</p>
      <p class="menu-item mt-14">About Us</p>
      <p class="menu-item mt-12">Contact Us</p>
      <p class="menu-item mt-12">Copyright</p>
      <p class="menu-item mt-12">Privacy</p>
      <p class="menu-item mt-12">Join Us</p>
    </div>
    <div class="content">
      Facebook: HiChinaTravel<br/>
      WeChat Official Account: HCTravel<br/>
      Instagram: HiChinaTravel<br/>
      Support: customerservice@hichinatrip.com<br/>
      Address: 4,Zhixin Rd. Qixia District Nanjing City, Jiangsu Province, P.R China<br/>
    </div>

    <div class="info">
      <p>COPYRIGHT © 2015-2023 WWW.HICHINATRAVEL.COM, ALL RIGHTS RESERVED </p>
      <p>备案号： 京ICP备16006305号-1</p>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped lang="scss">
  .footer {
    position: relative;
    margin-top: 0px;
    width: 100%;
    height: 325px;
    color: rgba(80, 80, 80, 1);
    background-color: rgba(42, 130, 228, 1);
    padding: 65px 343px 20px;

    .menu {
      position: absolute;
      left: 94px;
      top: 37px;

      &-title {
        color: rgba(255, 255, 255, 1);
        font-size: 16px;
      }

      &-item {
        color: rgba(255, 255, 255, 1);
        font-size: 14px;
      }
    }

    .content {
      color: rgba(255, 255, 255, 1);
      font-size: 16px;
      line-height: 30px;
    }

    .info {
      margin-top: 45px;
      color: rgba(255, 255, 255, 1);
      font-size: 14px;
      text-align: center;
    }
  }
</style>