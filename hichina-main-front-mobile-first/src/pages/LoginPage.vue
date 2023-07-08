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
            label="Enter your username"
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
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance } from "vue";
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

    onMounted(() => {
      whoami();
    });

    return {
      login,
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
