<template>
  <q-page class="flex flex-center" padding>
    <!-- content -->
    <div class="full-width" style="max-width: 330px">
      <h5 class="q-my-md text-center">登录</h5>
      <q-card>
        <q-card-section>
          <q-input
            v-model="username"
            filled
            label="用户名"
            class="q-mb-md"
          ></q-input>
          <q-input
            v-model="password"
            filled
            label="密码"
            class="q-md-sm"
            type="password"
          ></q-input>
          <!-- <q-checkbox :model-value="true" label="Remember me"></q-checkbox> -->
        </q-card-section>

        <q-btn
          label="login"
          @click="login"
          class="full-width"
          color="primary"
        ></q-btn>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import Qs from "qs";
import { useQuasar } from "quasar";
export default {
  // name: 'PageName',
  data() {
    return {
      username: "",
      password: "",
      loggedIn: false,
    };
  },
  setup() {
    const $q = useQuasar();

    return {
      showNotifyMessageFail(msg) {
        $q.notify({
          message: msg,
          color: "red",
          position: "top-right",
        });
      },
      showNotifyMessageSucceed(msg) {
        $q.notify({
          message: msg,
          color: "green",
          position: "top-right",
        });
      },
      showFullPageLoading() {
        this.disableAction = true;
        $q.loading.show();
      },
      hideFullPageLoading() {
        $q.loading.hide();
        this.disableAction = false;
      },
    };
  },
  methods: {
    login() {
      console.log(this.username);
      console.log(this.password);
      var data = {
        username: this.username,
        password: this.password,
        timeout: 1000,
      };
      api
        .post("/login", Qs.stringify(data), {
          headers: { "Access-Control-Allow-Origin": true },
        })
        .then((response) => {
          // location.reload();
          console.log("login succeed");
          window.location.href = "/";
        })
        .catch((e) => {
          console.log("login failed");
          console.log(e);
          showNotifyMessageFail(e);
        });
    },
    logout() {
      this.$axios
        .post(
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
    },
    whoami() {
      var storeThis = this;
      api
        .get("/api/v1/user/info")
        .then(function (response) {
          console.log("got login info: " + response.data);
          storeThis.user = response.data;
          storeThis.loggedIn = true;
        })
        .catch(function (error) {
          storeThis.loggedIn = false;
          console.log("hide in LoginPage:");
          console.log(error);
          storeThis.$router.push("/login");
        });
    },
  },
};
</script>
