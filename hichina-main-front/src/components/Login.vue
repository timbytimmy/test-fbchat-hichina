<template>
  <el-form
    class="login-form"
    :model="model"
    ref="form"
    label-position="top"
    :rules="rules"
    label-width="80px"
    @submit.native.prevent="login"
  >
    <h2>Login</h2>
    <br />
    <el-form-item prop="username">
      <el-input
        v-model="model.username"
        prefix-icon="user"
        placeholder="Enter your username"
      ></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input
        v-model="model.password"
        prefix-icon="lock"
        type="password"
        placeholder="Enter your password"
      ></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" native-type="submit">Login</el-button>
    </el-form-item>
    <div class="separate-line"></div>
    <el-form-item>
      <el-button
        color="#626aef"
        @click="goToFacebookLogin"
        class="custom-button"
      >
        <span class="facebook-icon"></span>
        Login with Facebook
      </el-button>
    </el-form-item>
  </el-form>

  <!-- <div class="logincontainer"> -->
  <!-- <div class="login" v-loading="loading">
      <el-card>
        <h2>Login</h2>
        <el-form
          class="login-form"
          :model="model"
          :rules="rules"
          ref="form"
          @submit.native.prevent="login"
        >
          <el-form-item prop="username">
            <el-input prefix-icon="user" v-model="model.username" placeholder="Username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="model.password"
              placeholder="Password"
              type="password"
              prefix-icon="lock"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              :loading="loading"
              class="login-button"
              type="primary"
              native-type="submit"
              block
            >Login</el-button>
          </el-form-item>
          <a class="forgot-password" href="https://oxfordinformatics.com/">Forgot password ?</a>
        </el-form>
      </el-card>
    </div> -->
  <!-- </div> -->
</template>
  
  <script>
import { AXIOS } from "@/common/http-commons";
import { ElNotification } from "element-plus";
import { useRouter } from "vue-router";
import Qs from "qs";
export default {
  name: "login",
  setup() {
    const router = useRouter();

    function whoami() {
      AXIOS.get("/api/v1/user/whoami")
        .then(function (response) {
          console.log("current user in setup: " + response.data);
          router.push("/home");
        })
        .catch(function (error) {
          console.log("currently not logged in setup: " + error);
        });
    }

    function initFacebookSDK() {
      // Load the Facebook SDK asynchronously
      const loadFacebookSDK = new Promise((resolve) => {
        window.fbAsyncInit = function () {
          FB.init({
            appId: "994181467335802",
            autoLogAppEvents: true,
            xfbml: true,
            version: "v13.0",
          });
          resolve();
        };
      });

      // Load the SDK script
      (function (d, s, id) {
        var js,
          fjs = d.getElementsByTagName(s)[0];
        console.log("js");
        console.log(js);
        console.log("fjs");
        console.log(fjs);
        if (d.getElementById(id)) {
          return;
        }
        js = d.createElement(s);
        js.id = id;
        js.src = "https://connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
      })(document, "script", "facebook-jssdk");

      // Wait for the SDK to be loaded
      loadFacebookSDK.then(() => {
        console.log("facebook sdk loaded");
        // You can now use the Facebook SDK methods here
        // For example, authenticate the user, fetch user data, etc.
      });
    }

    onMounted(() => {
      whoami();
    });

    onBeforeMount(() => {
      initFacebookSDK();
    });

    return {};
  },
  data() {
    return {
      validCredentials: {
        username: "lightscope",
        password: "lightscope",
      },
      model: {
        username: "",
        password: "",
      },
      loading: false,
      rules: {
        username: [
          {
            required: true,
            message: "username should not be empty",
            trigger: "blur",
          },
          {
            min: 4,
            message: "at least 5 characters",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "Password is required", trigger: "blur" },
          {
            min: 5,
            message: "Password length should be at least 5 characters",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    goToFacebookLogin() {
      FB.login(function (response) {
        // handle the response
        if (response.authResponse) {
          var accessToken = response.authResponse.accessToken;

          // get my info and set
          FB.api(
            "/me",
            "GET",
            { fields: "id,name,email,picture" },
            function (response) {
              // try login using the oauth way
              const params = {};
              params.accessToken = accessToken;
              params.id = response.id;
              params.email = response.email;
              params.name = response.name;
              params.profileImageUrl = response.picture.data.url;

              this.loading = true;
              var data = {
                username: response.id+","+response.name+","+response.email+","+response.picture.data.url,
                password: accessToken,
                timeout: 1000,
              };

              AXIOS.post("/login", Qs.stringify(data), {
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
              })
                .then((response) => {
                  this.loading = false;
                  location.reload();
                })
                .catch((e) => {
                  ElNotification({
                    title: "Error",
                    message: "Error message: " + e,
                    type: "error",
                  });
                });
              // AXIOS.post("/auth/social/facebook", params)
              //   .then((response) => {
              //     console.log("after try login facebook from facebook got:");
              //     console.log(response);
              //     location.reload();

              //   })
              //   .catch((e) => {
              //     ElNotification({
              //       title: "Error",
              //       message: "Error message: " + e,
              //       type: "error",
              //     });
              //   });
            }
          );
        } else {
          console.log("User cancelled login or did not fully authorize.");
          ElNotification({
            title: "Error",
            message: "User cancelled login or did not fully authorize.",
            type: "error",
          });
        }
      });
    },
    async login() {
      this.loading = true;
      var data = {
        username: this.model.username,
        password: this.model.password,
        timeout: 1000,
      };

      AXIOS.post("/login", Qs.stringify(data), {
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      })
        .then((response) => {
          this.loading = false;
          location.reload();
        })
        .catch((e) => {
          ElNotification({
            title: "Error",
            message: "Error message: " + e,
            type: "error",
          });
        });
      // let valid = await this.$refs.form.validate();
      // if (!valid) {
      //   return;
      // }
      // this.loading = true;
      // await this.simulateLogin();
      // this.loading = false;
      // if (
      //   this.model.username === this.validCredentials.username &&
      //   this.model.password === this.validCredentials.password
      // ) {
      //   this.$message.success("Login successfull");
      // } else {
      //   this.$message.error("Username or password is invalid");
      // }
    },
  },
};
</script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
  <style scoped>
.login-form {
  max-width: 300px;
  margin: 0 auto;
  border: 1px solid #000000;
  padding: 10px;
}
.separate-line {
  border-bottom: 1px solid #000;
}
.custom-button {
  margin-top: 5px;
}
.custom-button .facebook-icon {
  background-image: url("@/assets/svg/facebook-round.svg");
  background-size: cover;
  display: inline-block;
  width: 24px;
  height: 24px;
  margin-right: 10px;
}
</style>