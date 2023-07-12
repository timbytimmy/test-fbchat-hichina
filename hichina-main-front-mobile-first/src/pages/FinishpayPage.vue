<template>
  <q-page class="flex flex-center">
    Redirecting... <vue-json-pretty :data="route.query" />
  </q-page>
</template>

<script>
import { useQuasar } from "quasar";
import { onMounted, getCurrentInstance } from "vue";
import { useRoute } from "vue-router";
import { api } from "boot/axios";
import VueJsonPretty from "vue-json-pretty";
import "vue-json-pretty/lib/styles.css";
export default {
  name: "FinishpayPage",
  components: {
    VueJsonPretty: VueJsonPretty,
  },
  setup() {
    const instance = getCurrentInstance();
    const app = instance.appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const route = useRoute();
    onMounted(() => {
      if (
        route.query.out_trade_no != null &&
        route.query.out_trade_no.length > 0
      ) {
        var params = {};
        params.tradeNo = route.query.trade_no;
        params.method = route.query.method;
        api
          .put("/api/v1/order/succeed/" + route.query.out_trade_no, params)
          .then((res) => {
            gp.$generalNotify($q, true, "Succeed finishing pay");
            var params2pass = {};
            params2pass.active = 2;
            gp.$goPage("/my-orders");
          })
          .catch((err) => {
            gp.$generalNotify($q, false, "Fail creating order" + err);
          });
      }
    });
    return {
      route,
    };
  },
};
</script>
