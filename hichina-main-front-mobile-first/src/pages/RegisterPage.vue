<template>
  <q-page>
    <div style="height: 82px"></div>
    <div class="row q-mb-xl justify-center">
      <div
        class="col-10 col-sm-5 col-md-3 rounded-borders login-border shadow-7"
      >
        <div class="text-h5 text-left text-weight-bold text-black q-pa-md">
          Register
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
          <q-input
            color="blue-12"
            v-model="confPassword"
            error-message="Confirm password is not the same with password"
            :error="!correctConfPass"
            type="password"
            label="Confirm your password"
            ref="confPasswordInput"
          >
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
          </q-input>
        </div>
        <div class="col-12 q-pa-md">
          <q-btn color="primary" @click="register()" label="Register" />
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance, computed } from "vue";
import { useQuasar } from "quasar";
import { api } from "boot/axios";

export default {
  name: "RegisterPage",
  setup() {
    const instance = getCurrentInstance();
    const app = getCurrentInstance().appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const username = ref("");
    const password = ref("");
    const confPassword = ref("");

    function register() {
      const isValidPassword = instance.refs.passwordInput.validate();
      const isValidConfPassword = instance.refs.confPasswordInput.validate();
      const isValidUsername = instance.refs.usernameInput.validate();

      if (
        isValidPassword &&
        isValidConfPassword &&
        isValidUsername &&
        confPassword.value == password.value
      ) {
        // do real reg logic
        var data = {
          email: username,
          password: password,
        };

        api
          .post("/api/public/register", data)
          .then((response) => {
            console.log("==after register");
            console.log(response.data);
            if (response.data.ok == true) {
              gp.$generalNotify($q, true, "Succeed sending registering info");
            } else {
              gp.$generalNotify($q, false, response.data.message);
            }
            gp.$goPage("/regsuccess");
          })
          .catch((e) => {
            gp.$generalNotify($q, false, "Error message: " + e);
          });
      } else {
        gp.$generalNotify($q, false, "Input not valid");
      }
    }
    return {
      password,
      confPassword,
      username,
      correctConfPass: computed(() => confPassword.value == password.value),
    };
  },
};
</script>
