<template>
    <div class="main">
        <div v-if="!validated">Validating...</div>
        <div v-if="validated">Your register has finished, please go to login page</div>
    </div>
  </template>
  
  <script setup>
    import { useRouter } from 'vue-router';
    import {AXIOS} from '@/common/http-commons'
    import router from "../../router";
    import { ElNotification } from 'element-plus'  

    const validated = ref(false)
  
    const waitAndGo = async () => {
      setTimeout(() => {
        router.push({ name: "login" });
      }, 2500);
    };

    onMounted(() => {
      console.log("on mounted")
      const route = useRoute()
      AXIOS.post('/api/public/register/pushintodb/'+route.params.regKey).then(response=>{
         console.log("Validating register result: ")
         console.log(response.data);
         if(response.data.ok==true){
             validated.value = true;
             waitAndGo();
         }else{
            ElNotification({
              title: "Error",
              message: "Validating registering failed: "+ response.data.message,
              type: "error",
            });
         }
      }).catch(e=>{
        console.log("Validate registering fail detail err")
        ElNotification({
          title: "Error",
          message: "Validating registering failed: "+ e,
          type: "error",
        });
        console.log(e)
      })
    })
  </script>
  
  <style scoped lang="scss">
    .content-main,
    .main {
      width: 1026px;
      margin: 0 auto;
    }
  
    .main {
      padding-top: 105px;
    }
  
    .title {
      color: rgba(80, 80, 80, 1);
      font-size: 100px;
    }
  
    .logo {
      width: 104px;
      height: 104px;
      border-radius: 50%;
    }
  
    .name {
       color: rgba(80, 80, 80, 1);
       font-size: 26px;
     }
  
    .follow {
      width: 157px;
      height: 30px;
      color: rgba(255, 255, 255, 1);
      background-color: rgba(42, 130, 228, 1);
      border-radius: 5px;
      font-size: 19px;
      line-height: 30px;
      text-align: center;
    }
  
    .info {
      color: rgba(80, 80, 80, 1);
      font-size: 27px;
    }
  
    .content {
      margin: 40px 17px 0;
      border-top: 1px solid #e5e5e5;
    }
  
    .rich-text {
      color: rgba(80, 80, 80, 1);
      font-size: 22px;
    }
  
  
    .content-main {
      padding-top: 28px;
  
      img {
        width: 100%;
      }
    }
  
    .list {
      position: fixed;
      left: -251px;
      bottom: 300px;
      transition: .3s;
  
      &.show {
        left: 20px;
      }
  
      &-item {
        margin-bottom: 33px;
        color: rgba(42, 130, 228, 1);
        font-size: 17px;
        text-align: center;
      }
  
      .img {
        position: relative;
        width: 251px;
        height: 155px;
  
        img {
          width: 100%;
          height: 100%;
        }
  
        .tag {
          position: absolute;
          left: 0;
          top: 25px;
          width: 56px;
          height: 30px;
          color: rgba(255, 255, 255, 1);
          background-color: rgba(42, 130, 228, 1);
          border-radius: 4px;
          font-size: 12px;
          line-height: 30px;
          text-align: center;
        }
      }
    }
  </style>