<template>
  <!-- <div class="banner">
    <div class="logo">
      <img src="@/assets/images/logo.png" alt="Logo" />
    </div>
    <el-menu
      class="menu"
      mode="horizontal"
      :router="true"
      :default-active="activeMenuItem"
      @select="handleMenuSelect"
    >
      <el-menu-item index="home">Home</el-menu-item>
      <el-menu-item index="products">Products</el-menu-item>
      <el-menu-item index="about">About</el-menu-item>
      <el-menu-item index="contact">Contact</el-menu-item>
    </el-menu>
  </div> -->
  <div class="nav flex-between" @scroll="handleScroll">
    <img class="logo" src="@/assets/images/logo.png" @click="goPage('/home')" alt="" />

    <el-menu :default-active="activeIndex"  class="flex-item el-menu-demo" mode="horizontal">
      <el-menu-item class="nav-title menuitem" index="1" @click="goPage('/guideintro')" >Guidebooks</el-menu-item>
      <el-menu-item class="nav-title menuitem" index="2" @click="goPage('/blog')" >Blogs/Vlogs</el-menu-item>
      <el-menu-item class="nav-title menuitem" index="3" @click="goPage('/product')" >Travel Shop</el-menu-item>
      <el-menu-item class="nav-title menuitem" index="4" @click="goPage('/destination')" >Destinations</el-menu-item>
    </el-menu>

    <div class="flex-between">
      <div class="flex-between">
        <svg-icon icon-class="user"/>
        <el-select v-model="value1" class="m-2" size="mini" placeholder="请选择">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </div>
      <span v-if="currentUser==''" class="login"><el-button type="primary" @click="goPage('/auth/register')">Register</el-button> / <el-button type="primary" @click="goPage('/auth/login')">Login</el-button></span>
      <el-dropdown style="cursor: pointer;" v-if="currentUser!=''">
        <span class="el-dropdown-link">
          {{ currentUser }}
          <el-icon class="el-icon--right">
            <arrow-down />
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="goPage('/user')">My Content</el-dropdown-item>
          </el-dropdown-menu>
          <el-dropdown-menu>
            <el-dropdown-item @click="goPage('/user-info')">Edit Profile</el-dropdown-item>
          </el-dropdown-menu>
          <el-dropdown-menu>
            <el-dropdown-item @click="logout">Logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>  
  import VLazyImage from "v-lazy-image";
  import { useRouter } from 'vue-router';
  import router from "../../../router";
  import { ElNotification } from 'element-plus'
  import {AXIOS} from '@/common/http-commons'
  import { ArrowDown } from '@element-plus/icons-vue'
  const activeIndex = ref(1)

  const activeMenuItem =ref('home')

  function handleMenuSelect(index) {
    activeMenuItem.value = index;
    // Handle menu selection logic here
  }

  function handleScroll(el) {
    console.log("scoll me")
    if((el.srcElement.offsetHeight + el.srcElement.scrollTop) >= el.srcElement.scrollHeight) {
      this.hasScrolledToBottom = true;
    }
  }
  function goPage(val){
    router.push({ path: val })
  }
  function logout(){
    console.log("logging out...")
    AXIOS.post(
      "/logout",
      {},
      { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
    )
    .then((response) => {
      location.reload();
    })
    .catch((e) => {
      location.reload();
    });
  }

  const currentUser = ref("")

  function whoami(){
    AXIOS.get("/api/v1/user/whoami")
    .then(function (response) {
       console.log("current user:" + response.data)
       currentUser.value = response.data
    })
    .catch(function (error) {
      console.log("not logged in err");
      // router.push({name: 'home'})
    });
  }

  onMounted(() => {
    whoami();
  })
</script>

<style scoped lang="scss">

  .nav {
    min-width: 1400px;
    height: 78px;
    padding: 0 54px 0 57px;

    .logo {
      margin-right: 67px;
      width: 114px;
      height: 60px;
      cursor: pointer;
    }

    .el-menu {
      border-bottom: none;
    }

    &-title {
      color: rgba(80, 80, 80, 1);
      font-size: 14px;
      line-height: 150%;
      text-align: left;
    }

    .el-select {
      margin-right: 55px;
      width: 100px;
      height: 32px;
    }

    .login {
      color: rgba(80, 80, 80, 1);
      font-size: 14px;
      line-height: 150%;
      text-align: left;
    }
  }

  .example-showcase .el-dropdown-link {
    cursor: pointer;
    color: var(--el-color-primary);
    display: flex;
    align-items: center;
  }

  .menuitem{
    margin-left: 20px;
  }
  // .banner {
  //   display: flex;
  //   justify-content: space-between;
  //   align-items: center;
  //   padding: 10px;
  //   background-color: #f5f5f5;
  // }

  // .logo img {
  //   height: 40px;
  //   width: auto;
  // }

  // .menu {
  //   background-color: #f5f5f5;
  // }
</style>