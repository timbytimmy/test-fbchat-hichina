import { defineStore } from "pinia";

export const orderPaymentParamStore = defineStore("orderPaymentParam", {
  state: () => ({
    paymentDetail: {},
  }),

  getters: {
    getPaymentDetail: (state) => state.paymentDetail,
  },

  actions: {
    setPaymentDetail(val) {
      this.paymentDetail = val;
    },
  },
});
