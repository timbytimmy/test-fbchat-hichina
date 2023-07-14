<template>
  <q-page>
    <div class="row flex flex-center">
      <div v-if="!validated" class="text-h5 text-red">Validating...</div>
      <div v-if="validated" class="text-h5 text-red">
        Your register has finished, please go to login page
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance } from "vue";
import { api } from "boot/axios";
import { useRoute } from "vue-router";
import { useQuasar } from "quasar";
export default {
  name: "RegvalidationPage",
  setup() {
    const instance = getCurrentInstance();
    const app = instance.appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const validated = ref(false);

    const route = useRoute();

    const waitAndGo = async () => {
      setTimeout(() => {
        gp.$goPage("/auth/login");
      }, 2500);
    };

    onMounted(() => {
      console.log("on mounted");
      api
        .post("/api/public/register/pushintodb/" + route.params.regKey)
        .then((response) => {
          console.log("Validating register result: ");
          console.log(response.data);
          if (response.data.ok == true) {
            validated.value = true;
            waitAndGo();
          } else {
            gp.$generalNotify(
              $q,
              false,
              "Validating registering failed: " + response.data.message
            );
          }
        })
        .catch((e) => {
          console.log("Validate registering fail detail err");
          gp.$generalNotify($q, false, "Validating registering failed: " + e);
          console.log(e);
        });
    });
    return {
      validated,
    };
  },
};
</script>
