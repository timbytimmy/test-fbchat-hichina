<template>
    <div class="user-detail">
        <my-upload field="imageFile"
        :width="300"
        :height="300"
        :url="serviceBase + '/api/v1/image/upload'"
        :params="upload_icon_params"
        :headers="headers"
        @crop-upload-success="cropUploadSuccess"
        lang-type="en"
        :withCredentials="true"
        v-model="showImageUpload"
        img-format="png"></my-upload>

        <div class="flex-between">
          <v-lazy-image :src="updatePackage.profileImageUrl" alt="" />
          <div class="flex-item ml-14">
            <p class="name">My Profile Image</p>
            <el-button class="upload mt-22" @click="showUpload">Upload</el-button>
            <p class="notice mt-16">Support format of jpg.png.bmp, and Picture size is limited at 5M</p>
          </div>
        </div>
        <div class="flex-start mt-18">
          <p class="text">Username:</p>
          <el-input class="uid ml-12" v-model="updatePackage.username"/>
        </div>
        <div class="flex-start mt-22">
          <p class="text">Email:</p>
          <el-input class="uid ml-22" v-model="updatePackage.email"/>
        </div>
        <div class="flex-start mt-22">
          <p class="text">Phone:</p>
          <el-input class="uid ml-22" v-model="updatePackage.phone"/>
        </div>
        <div class="flex-start mt-22">
          <p class="text">Nationality:</p>
          <country-select
                v-model="updatePackage.nationality"
                :country="updatePackage.nationality"
                topCountry="US"
              />
       </div>
        <div>
            <el-button type="primary" @click="updateInfo">Update</el-button>
        </div>

        <hr class="solid">
        <p class="text mt-50">Change My Password</p>
        <div class="flex-start mt-18">
          <p class="text wd-180">Current Password</p>
          <el-input type="password" class="uid ml-12" v-model="currentPass" />
        </div>
        <div class="flex-start mt-18">
          <p class="text wd-180">New       Password</p>
          <el-input type="password" class="uid ml-12" v-model="newPass" />
        </div>
        <div class="flex-start mt-18">
          <p class="text wd-180">Confirm Password</p>
          <el-input type="password" class="uid ml-12" v-model="confirmPass" />
          <el-button class="save ml-30" @click="updatePassword">Update Password</el-button>
        </div>
      </div>
</template>
  
  <script setup>
    import VLazyImage from "v-lazy-image";
    import {AXIOS} from '@/common/http-commons'
    import router from "../../router";
    import { ElNotification } from "element-plus";
    const country = ref("")
    
    function updatePassword(){
       if(currentPass.value==null || currentPass.value.length<1 || newPass.value==null || newPass.value.length<1){
        alert("Cannot left any password empty")
        return
       }
       if(newPass.value!=confirmPass.value){
        alert("Confirm password not the same with new Password")
        return;
       }
       var params = {}
       params.oldPass = currentPass.value;
       params.newPass = newPass.value
       AXIOS.put('/api/v1/user/updatePass', params).then(res => {
            console.log(res.data)
            if(res.data.ok=='true'){
                ElNotification({
                    title: 'Succeed',
                    message: 'Succeed updating password '+err,
                    type: 'Succeed',
                  })
            }else{
                ElNotification({
                    title: 'Fail',
                    message: res.data.message,
                    type: 'Error',
                  })
            }
          })
          .catch(err => {
            ElNotification({
            title: 'Fail',
            message: 'Fail updating password '+err,
            type: 'Error',
          })
        })
    }
    function updateInfo(){
        console.log(updatePackage.value)
        AXIOS.put('/api/v1/user/basicInfo', updatePackage.value).then(res => {
            console.log(res.data)
            if(res.data.ok==true){
                ElNotification({
                  title: 'Suceed',
                  message: 'Succeed udpating basic info',
                  type: 'success',
                })
            }else{
                ElNotification({
                  title: 'Error',
                  message: 'Failed udpating basic info',
                  type: 'Error',
                })
            }
            
          })
          .catch(err => {
            ElNotification({
            title: 'Fail',
            message: 'Fail creating order'+err,
            type: 'error',
          })
        })
    }

    function whoami(){
        AXIOS.get("/api/v1/user/whoami")
        .then(function (response) {
          console.log("current user in user-info: " + response.data)
        })
        .catch(function (error) {
          console.log("currently not logged in user-info: "+error);
          router.push("/auth/login")
        });
      }
    const upload_icon_params = ref({"expectedType":"thumbnail"})
    const updatePackage = ref({})
    const serviceBase = ref("")
    const showImageUpload = ref(false)
    const currentPass = ref("")
    const newPass = ref("")
    const confirmPass = ref("")
    function showUpload(){
        showImageUpload.value = true
    }
    function getServiceBase(){
        AXIOS.get("/api/public/service/backend-service").then(function (response) {
            console.log("service base:")
            console.log(response.data)
            serviceBase.value = response.data
        })
        .catch(function (error) {
          console.log(error);
        });
    }

    function updateMyProfileImage(url){
        var params = {}
        params.param = url;
        AXIOS.post('/api/v1/user/update-profile-image', params).then(res => {
            ElNotification({
              title: 'Suceed',
              message: 'Succeed updating profile image',
              type: 'success',
            })
            updatePackage.value.profileImageUrl = url;
          })
          .catch(err => {
            ElNotification({
            title: 'Fail',
            message: 'Fail updating user profile image'+err,
            type: 'error',
          })
        })
    }

    function cropUploadSuccess(jsonData, field) {
      var url = jsonData.data
      updateMyProfileImage(url)
      ElNotification({
      title: "Succeed",
      message: "上传成功",
      type: "success",
      });
      showImageUpload.value = false;
    }

    function getMyPrincipal(){
      AXIOS.get("/api/v1/user/mysecurityinfo")
      .then(function (response) {
          console.log("my security data:")
          console.log(response.data.data)
          if(response.data.ok==true){
              country.value = updatePackage.value.nationality;
              updatePackage.value = response.data.data;
              if(updatePackage.value.profileImageUrl==null || updatePackage.value.profileImageUrl.length<1){
                  updatePackage.value.profileImageUrl = "https://photoprism.hichinatravel.com/api/v1/t/07dac61cc5dfec34dd9358f37bd70ce32de68488/32mcf2k4/fit_2048"
              }
          }else{
              ElNotification({
                title: "Error",
                message: response.data.message,
                type: "Error",
              });
          }
      })
      .catch(function (error) {
        console.log(error);
      });
    }

    onMounted(() => {
        whoami()
        getServiceBase()
        getMyPrincipal()
    });
  </script>
  
  <style scoped lang="scss">
    hr.solid {
      margin-top: 20px;
      border-top: 3px solid #bbb;
    }
    .user-detail {
        width: 60%;
        padding-top: 22px;
        margin: 0 auto;
        color: rgba(80, 80, 80, 1);
        font-size: 25px;

        img {
          width: 190px;
          height: 149px;
        }

        .name {
          color: rgba(80, 80, 80, 1);
          font-size: 25px;
        }

        .upload {
          width: 163px;
          height: 48px;
          color: rgba(255, 255, 255, 1);
          background-color: rgba(42, 130, 228, 1);
          border-radius: 7px;
          font-size: 14px;
        }

        .notice {
          color: rgba(80, 80, 80, 1);
          font-size: 14px;
        }

        .save {
          width: 123px;
          height: 26px;
          color: rgba(255, 255, 255, 1);
          background-color: rgba(42, 130, 228, 1);
          border-radius: 7px;
          font-size: 14px;
        }
      }

      .text {
        color: rgba(80, 80, 80, 1);
        font-size: 14px;
      }

      .uid {
        width: 296px;
      }

      .wd-180 {
        flex: 0 0 122px;
        text-align-last: justify;
      }
  </style>