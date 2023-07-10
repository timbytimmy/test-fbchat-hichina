<template>
  <q-page>
    <div class="row">
      <div class="col-md-2"></div>
      <div class="q-px-md col-12 col-md-8">
        <div class="q-pa-md">
          <my-upload
            field="imageFile"
            :width="300"
            :height="300"
            :url="serviceBase + '/api/v1/image/upload'"
            :params="upload_icon_params"
            @crop-upload-success="cropUploadSuccess"
            lang-type="en"
            :withCredentials="true"
            v-model="showImageUpload"
            img-format="png"
          ></my-upload>
        </div>
        <div class="row">
          <div style="width: 200px">
            <q-img :src="updatePackage.profileImageUrl" />
          </div>
          <div class="col q-px-md">
            <div class="text-weight-bold text-h4">My Profile Image</div>
            <q-btn
              @click="showUpload"
              round
              color="secondary"
              icon="cloud_upload"
            />
            <p class="q-py-md notice mt-16">
              Support format of jpg.png.bmp, and Picture size is limited at 5M
            </p>
          </div>
        </div>
        <div class="row">
          <div class="col-12 col-sm-6 col-md-5">
            <q-input
              outlined
              v-model="updatePackage.username"
              label="Username"
            />
          </div>
        </div>
        <div class="row">
          <div class="col-12 col-sm-6 col-md-5">
            <q-input outlined v-model="updatePackage.email" label="Email" />
          </div>
        </div>
        <div class="row">
          <div class="col-12 col-sm-6 col-md-5">
            <q-input outlined v-model="updatePackage.phone" label="Phone" />
          </div>
        </div>
        <div class="row q-py-md">
          <div class="col-12 col-sm-6 col-md-5">
            <div class="text-weight-bold">Nationality:</div>
            <country-select
              v-model="updatePackage.nationality"
              :country="updatePackage.nationality"
              topCountry="US"
            />
          </div>
        </div>
        <div class="row">
          <q-btn
            class="glossy"
            @click="updateInfo"
            color="primary"
            label="Update"
          />
        </div>
        <div
          class="row"
          style="min-height: 10px; border-bottom: 1px solid gray"
        ></div>
        <div class="row q-py-md">
          <div class="text-weight-bold text-h5">Change my password</div>
        </div>
        <div class="row">
          <div class="col-12 col-sm-6 col-md-5">
            <q-input
              outlined
              type="password"
              v-model="currentPass"
              label="Current Pasword"
            />
          </div>
        </div>
        <div class="row">
          <div class="col-12 col-sm-6 col-md-5">
            <q-input
              outlined
              type="password"
              v-model="newPass"
              label="New Pasword"
            />
          </div>
        </div>
        <div class="row">
          <div class="col-12 col-sm-6 col-md-5">
            <q-input
              outlined
              type="password"
              v-model="confirmPass"
              label="Confirm Pasword"
            />
          </div>
        </div>
        <div class="row q-py-md">
          <q-btn
            class="glossy"
            color="primary"
            @click="updatePassword"
            label="Update Password"
          />
        </div>
      </div>
      <div class="col-md-2"></div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance } from "vue";
import { useQuasar } from "quasar";
import myUpload from "vue-image-crop-upload";
import { api } from "boot/axios";
export default {
  name: "UserInfoPage",
  components: {
    "my-upload": myUpload,
  },
  setup() {
    const instance = getCurrentInstance();
    const app = getCurrentInstance().appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const serviceBase = ref("");
    const country = ref("");
    const updatePackage = ref({});
    const upload_icon_params = ref({ expectedType: "thumbnail" });
    const showImageUpload = ref(false);
    const currentPass = ref("");
    const newPass = ref("");
    const confirmPass = ref("");

    function showUpload() {
      showImageUpload.value = true;
    }

    function updateInfo() {
      console.log(updatePackage.value);
      api
        .put("/api/v1/user/basicInfo", updatePackage.value)
        .then((res) => {
          console.log(res.data);
          if (res.data.ok == true) {
            gp.$generalNotify($q, true, "Succeed udpating basic info");
          } else {
            gp.$generalNotify($q, false, "Failed udpating basic info");
          }
        })
        .catch((err) => {
          gp.$generalNotify($q, false, "Fail creating order" + err);
        });
    }

    function updatePassword() {
      if (
        currentPass.value == null ||
        currentPass.value.length < 1 ||
        newPass.value == null ||
        newPass.value.length < 1
      ) {
        gp.$generalNotify($q, false, "Cannot left any password empty");
        return;
      }
      if (newPass.value != confirmPass.value) {
        gp.$generalNotify(
          $q,
          false,
          "Confirm password not the same with new Password"
        );
        return;
      }
      var params = {};
      params.oldPass = currentPass.value;
      params.newPass = newPass.value;
      api
        .put("/api/v1/user/updatePass", params)
        .then((res) => {
          if (res.data.ok == true) {
            gp.$generalNotify($q, true, "Succeed updating password");
          } else {
            gp.$generalNotify($q, false, res.data.message);
          }
        })
        .catch((err) => {
          gp.$generalNotify($q, false, "Fail updating password " + err);
        });
    }

    function getServiceBase() {
      api
        .get("/api/public/service/backend-service")
        .then(function (response) {
          console.log("service base:");
          console.log(response.data);
          serviceBase.value = response.data;
        })
        .catch(function (error) {
          console.log(error);
        });
    }
    function updateMyProfileImage(url) {
      var params = {};
      params.param = url;
      api
        .post("/api/v1/user/update-profile-image", params)
        .then((res) => {
          gp.$generalNotify($q, true, "Succeed updating profile image");
          updatePackage.value.profileImageUrl = url;
        })
        .catch((err) => {
          gp.$generalNotify(
            $q,
            false,
            "Fail updating user profile image" + err
          );
        });
    }
    function cropUploadSuccess(jsonData, field) {
      var url = jsonData.data;
      updateMyProfileImage(url);
      gp.$generalNotify($q, true, "上传成功");
      showImageUpload.value = false;
    }
    function whoami() {
      api
        .get("/api/v1/user/whoami")
        .then(function (response) {
          console.log("current user in user-info: " + response.data);
        })
        .catch(function (error) {
          console.log("currently not logged in user-info: " + error);
          gp.$goPage("/auth/login");
        });
    }
    function getMyPrincipal() {
      api
        .get("/api/v1/user/mysecurityinfo")
        .then(function (response) {
          console.log("my security data:");
          console.log(response.data.data);
          if (response.data.ok == true) {
            country.value = updatePackage.value.nationality;
            updatePackage.value = response.data.data;
            if (
              updatePackage.value.profileImageUrl == null ||
              updatePackage.value.profileImageUrl.length < 1
            ) {
              updatePackage.value.profileImageUrl =
                "https://photoprism.hichinatravel.com/api/v1/t/07dac61cc5dfec34dd9358f37bd70ce32de68488/32mcf2k4/fit_2048";
            }
          } else {
            gp.$generalNotify($q, false, response.data.message);
          }
        })
        .catch(function (error) {
          console.log(error);
        });
    }
    onMounted(() => {
      whoami();
      getServiceBase();
      getMyPrincipal();
    });
    return {
      showImageUpload,
      cropUploadSuccess,
      upload_icon_params,
      serviceBase,
      showUpload,
      updatePassword,
      updatePackage,
      currentPass,
      newPass,
      confirmPass,
      updateInfo,
    };
  },
};
</script>
