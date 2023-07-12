<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-7 q-pa-md">
        <div>
          <q-btn
            color="primary"
            @click="goPage('/my-orders')"
            glossy
            label="Return to order list"
          />
        </div>
        <div class="q-my-md col-7 text-h5 text-weight-bold">Product Info</div>

        <div class="row col-12 q-pa-md" style="background-color: #e7e7e7">
          <div class="col-3">Product Name</div>
          <div class="col-3">Package Category</div>
          <div class="col-2">Total Price</div>
          <div class="col-2">Payment Status</div>
          <div class="col-2">Start Time</div>
        </div>
        <div class="row col-12 q-pa-md" style="border: 1px solid gray">
          <div class="col-3">
            <a :href="frontendBase + '/product-detail/' + skuGroupId">{{
              productName
            }}</a>
          </div>
          <div class="col-3">{{ packageCategory }}</div>
          <div class="col-2">CNY:{{ totalPrice }}</div>
          <div class="col-2">{{ orderStatus }}</div>
          <div class="col-2">{{ selectedDate }}</div>
        </div>
      </div>
      <div class="row col-7 q-pa-md">
        <div class="text-h5 text-weight-bold">Passenger Info</div>
        <div v-if="passengerInfo != null && passengerInfo.length > 0">
          <q-table
            ref="passengerInfoTableRef"
            :rows="passengerInfo"
            :columns="columns"
            :separator="'cell'"
          >
            <template v-slot:body-cell-gender="props">
              <q-td :props="props">
                {{ props.row.gender == 1 ? "Male" : "Female" }}
              </q-td>
            </template>
          </q-table>
        </div>
      </div>
      <div class="row col-7 q-pa-md">
        <div class="text-h5 text-weight-bold">Contact Info</div>
        <div class="row col-12 q-pa-md" style="background-color: #e7e7e7">
          <div class="col-3">Contact Person</div>
          <div class="col-3">Email</div>
          <div class="col-3">Phone</div>
          <div class="col-3">Address</div>
        </div>
        <div class="row col-12" style="border: 1px solid gray">
          <div class="col-3">{{ contactInfo.name }}</div>
          <div class="col-3">{{ contactInfo.email }}</div>
          <div class="col-3">{{ contactInfo.phone }}</div>
          <div class="col-3">{{ contactInfo.address }}</div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance, reactive } from "vue";
import { api } from "boot/axios";
import { useQuasar } from "quasar";
import { useRoute } from "vue-router";
export default {
  name: "OrderDetailPage",
  setup() {
    const instance = getCurrentInstance();
    const app = instance.appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();
    const route = useRoute();

    const frontendBase = ref("");
    const orderDetail = ref({});
    const productName = ref("");
    const packageCategory = ref("");
    const selectedDate = ref("");
    const contactInfo = ref({ address: "", email: "", name: "", phone: "" });
    const passengerInfo = ref([]);
    const totalPrice = ref(0);
    const orderStatus = ref("");
    const skuGroupId = ref("");

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

    function loadOrderDetail(orderId) {
      api
        .get("/api/v1/order/orderDetail/" + orderId)
        .then((response) => {
          console.log("order detail: ");
          console.log(response.data.data);
          orderDetail.value = response.data.data;

          productName.value = orderDetail.value.meta.productInfo.productName;
          packageCategory.value =
            orderDetail.value.meta.productInfo.packageCategory;
          selectedDate.value = orderDetail.value.meta.productInfo.selectedDate;

          contactInfo.value = orderDetail.value.meta.contactInfo;
          totalPrice.value = orderDetail.value.price;

          orderStatus.value = orderDetail.value.status;

          passengerInfo.value = orderDetail.value.meta.passengerInfo;

          console.log("passengerInfo.value");
          console.log(passengerInfo.value);

          skuGroupId.value =
            orderDetail.value.meta.productInfo.productSkuGroupId;
        })
        .catch((e) => {
          console.log("get order detail err");
          console.log(e);
        });
    }

    function loadFrontendBase() {
      api
        .get("/api/public/service")
        .then(function (response) {
          console.log("base front url: " + response.data);
          frontendBase.value = response.data;
        })
        .catch(function (error) {
          console.log("currently not logged in setup: " + error);
          gp.$goPage("/");
        });
    }

    onMounted(() => {
      whoami();
      loadOrderDetail(route.params.orderId);
      loadFrontendBase();
    });

    return {
      frontendBase,
      skuGroupId,
      productName,
      packageCategory,
      totalPrice,
      orderStatus,
      selectedDate,
      passengerInfo,
      contactInfo,
      columns: [
        {
          name: "surName",
          required: true,
          label: "SurName",
          align: "left",
          field: "surName",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "givenName",
          required: true,
          label: "Given Name",
          align: "left",
          field: "givenName",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "nationality",
          required: true,
          label: "Nationality",
          align: "left",
          field: "nationality",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "birthday",
          required: true,
          label: "Birthday",
          align: "left",
          field: "birthday",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "gender",
          required: true,
          label: "Gender",
          align: "left",
          field: "gender",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "passportNo",
          required: true,
          label: "Passport No.",
          align: "left",
          field: "passportNo",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "passportExpireDate",
          required: true,
          label: "Passport Expiry Date",
          align: "left",
          field: "passportExpireDate",
          sortable: false,
          style:
            "max-width: 100px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
      ],
    };
  },
};
</script>
