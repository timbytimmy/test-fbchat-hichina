<template>
  <q-page>
    <div style="height: 82px"></div>
    <div class="row q-mb-xl justify-center">
      <div
        class="col-10 col-sm-5 col-md-3 rounded-borders login-border shadow-7"
      >
        <div class="text-h5 text-left text-weight-bold text-black q-pa-md">
          {{ $t("register") }}
        </div>
        <div class="col-12 q-pa-md">
          <q-input
            :rules="[(val) => !!val || 'Field is required']"
            color="blue-12"
            v-model="username"
            :label="$t('enter_email')"
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
            :label="$t('enter_password')"
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
            :label="$t('confirm_password')"
            ref="confPasswordInput"
          >
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
          </q-input>
        </div>
        <div class="col-12 q-pa-md">
          <q-btn color="primary" @click="register()" :label="$t('register')" />
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
          email: username.value,
          password: password.value,
        };

        gp.$showLoading($q);
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
            gp.$hideLoading($q);
            gp.$goPage("/regsuccess");
          })
          .catch((e) => {
            gp.$hideLoading($q);
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
      register,
      correctConfPass: computed(() => confPassword.value == password.value),
    };
  },
};
</script>
