<template>
  <q-page padding>
    <div class="row justify-center">
      <q-input class="col-5" v-model="username" label="username"></q-input>
      <q-input
        class="col-5"
        v-model="password"
        type="password"
        label="password"
      ></q-input>
      <q-btn color="primary" label="测试登录" @click="testlogin"></q-btn>
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
export default {
  name: "LoginTestPage",
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
    testlogin() {
      var params = {};
      params.email = this.username;
      params.password = this.password;
      var storeThis = this;
      api
        .post("/api/v1/testlogin", params)
        .then((response) => {
          console.log(response.data);
          if (response.data === false) {
            storeThis.showNotifyMessageFail("测试登录失败");
          } else {
            storeThis.showNotifyMessageSucceed("测试登录成功");
          }
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
  },
  data() {
    return {
      username: "",
      password: "",
    };
  },
};
</script>
