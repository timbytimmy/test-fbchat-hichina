<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-12 col-sm-7 q-pa-md">
        <div class="row col-12" style="border: 1px solid gray">
          <div class="col-2">Product Name</div>
          <div class="col-2">Package Category</div>
          <div class="col-2">Start Date</div>
          <div class="col-2">Total Price</div>
          <div class="col-2">Payment Status</div>
          <div class="col-2"></div>
        </div>
        <div v-for="(item, index) in orderSummaries" v-bind:key="index">
          <div>
            <div class="text-body1" style="background-color: #d6d6d6">
              Order No. {{ item.orderId }} | Created On: {{ item.createdTime }}
            </div>
            <div
              class="row col-12 text-subtitle1"
              style="border: 1px solid gray"
            >
              <div class="col-2">{{ item.productName }}</div>
              <div class="col-2">{{ item.packageCategory }}</div>
              <div class="col-2">{{ item.selectedDate }}</div>
              <div class="col-2">CNY: {{ item.totalPrice }}</div>
              <div class="col-2">{{ item.orderStatus }}</div>
              <div class="col-2">
                <q-btn
                  @click="goPage('/order-detail/' + item.orderId)"
                  color="grey-4"
                  text-color="primary"
                  glossy
                  dense
                  unelevated
                  icon="info"
                  label="Order Details"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance, reactive } from "vue";
import { api } from "boot/axios";
import { useQuasar } from "quasar";

export default {
  name: "MyOrdersPage",
  setup() {
    const instance = getCurrentInstance();
    const app = instance.appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const pageSize = ref(100);
    const currentPage = ref(1);
    const orderSummaries = ref([]);
    const totalCnt = ref(0);

    function whoami() {
      api
        .get("/api/v1/user/whoami")
        .then(function (response) {
          console.log("current user in user page: " + response.data);
        })
        .catch(function (error) {
          console.log("currently not logged in setup: " + error);
          gp.goPage("/");
        });
    }

    function loadMyOrderByPage() {
      var params = {};
      params.pageSize = pageSize.value;
      params.page = currentPage.value;
      params.query = "";
      api
        .get("/api/v1/order/myorders", {
          params: params,
        })
        .then(function (response) {
          console.log("Got my orders summary: ");
          console.log(response.data.data);
          orderSummaries.value = response.data.data.data;
          totalCnt.value = response.data.data.total;
          pageSize.value = response.data.data.pageSize;
          currentPage.value = response.data.data.currentPage;
        })
        .catch(function (error) {
          console.log("currently not logged in my order page: " + error);
          gp.$goPage("/auth/login");
        });
    }

    onMounted(() => {
      whoami();
      loadMyOrderByPage();
    });

    return {
      orderSummaries,
    };
  },
};
</script>
