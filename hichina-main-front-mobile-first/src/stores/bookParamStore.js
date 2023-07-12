import { defineStore } from "pinia";

export const bookParamStore = defineStore("bookParam", {
  state: () => ({
    orderDetail: {},
  }),

  getters: {
    getOrderDetail: (state) => state.orderDetail,
  },

  actions: {
    setOrderDetail(val) {
      this.orderDetail = val;
    },
  },
});
