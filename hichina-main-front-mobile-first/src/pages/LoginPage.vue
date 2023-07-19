<template>
  <q-page>
    <div style="height: 82px"></div>
    <div class="row q-mb-xl justify-center">
      <div
        class="col-10 col-sm-5 col-md-3 rounded-borders login-border shadow-7"
      >
        <div class="text-h5 text-left text-weight-bold text-black q-pa-md">
          Login
        </div>
        <div class="col-12 q-pa-md">
          <q-input
            :rules="[(val) => !!val || 'Field is required']"
            color="blue-12"
            v-model="username"
            label="Enter your email/username"
            ref="usernameInput"
          >
            <template v-slot:prepend>
              <q-icon name="account_circle" />
            </template>
          </q-input>
        </div>
        <div class="col-12 q-pa-md">
          <q-input
            :rules="[(val) => !!val || 'Field is required']"
            color="blue-12"
            v-model="password"
            type="password"
            label="Enter your password"
            ref="passwordInput"
          >
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
          </q-input>
        </div>
        <div class="col-12 q-pa-md">
          <q-btn color="primary" @click="login()" label="Login" />
          <q-btn
            icon="lab la-facebook"
            color="blue-6"
            dense
            rounded
            class="q-ma-sm"
            @click="goToFacebookLogin"
          />
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, onBeforeMount, getCurrentInstance } from "vue";
import { useQuasar } from "quasar";
import { api } from "boot/axios";
import Qs from "qs";
export default {
  name: "LoginPage",
  setup() {
    const instance = getCurrentInstance();
    const app = getCurrentInstance().appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const username = ref("");
    const password = ref("");

    function login() {
      const isValidPassword = instance.refs.passwordInput.validate();
      const isValidUsername = instance.refs.usernameInput.validate();
      if (isValidUsername && isValidPassword) {
        // do real login logic
        var data = {
          username: username.value,
          password: password.value,
        };

        api
          .post("/login", Qs.stringify(data), {
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
          })
          .then((response) => {
            gp.$generalNotify($q, true, "Login succeed!");
            location.reload();
          })
          .catch((e) => {
            gp.$generalNotify($q, false, "Fail login error message: " + e);
          });
      } else {
        gp.$generalNotify($q, false, "error");
      }
    }

    function whoami() {
      api
        .get("/api/v1/user/whoami")
        .then(function (response) {
          console.log("current user in setup: " + response.data);
          gp.$goPage("/");
        })
        .catch(function (error) {
          console.log("currently not logged in setup: " + error);
        });
    }

    function goToFacebookLogin() {
      FB.login(
        function (response) {
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
                  username:
                    response.id +
                    "," +
                    response.name +
                    "," +
                    response.email +
                    "," +
                    response.picture.data.url,
                  password: accessToken,
                };

                api
                  .post("/login", Qs.stringify(data), {
                    headers: {
                      "Content-Type": "application/x-www-form-urlencoded",
                    },
                  })
                  .then((response) => {
                    location.reload();
                  })
                  .catch((e) => {
                    gp.$generalNotify($q, false, "Error message: " + e);
                  });
              }
            );
          } else {
            console.log("User cancelled login or did not fully authorize.");
            gp.$generalNotify(
              $q,
              false,
              "User cancelled login or did not fully authorize."
            );
          }
        },
        { scope: "email" }
      );
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

    return {
      login,
      goToFacebookLogin,
      username,
      password,
    };
  },
};
</script>
<style lang="sass" scoped>
.login-border
  border-radius: 10px
</style>
