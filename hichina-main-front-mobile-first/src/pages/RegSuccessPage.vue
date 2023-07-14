<template>
  <q-page>
    <div class="row flex flex-center">
      <div class="col-12 col-md-8 text-h3 q-pa-xl text-red">
        Please go to your mailbox to confirm registration...
      </div>
    </div>
  </q-page>
</template>

<script>
import { onMounted, getCurrentInstance } from "vue";
import { useQuasar } from "quasar";
export default {
  name: "RegSuccessPage",
  setup() {
    const instance = getCurrentInstance();
    const app = instance.appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const waitAndGo = async () => {
      setTimeout(() => {
        gp.$goPage("/");
      }, 8000);
    };
    onMounted(() => {
      waitAndGo();
    });
    return {};
  },
};
</script>
