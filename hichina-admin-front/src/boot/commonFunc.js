// src/boot/commonFunc.js
import { boot } from "quasar/wrappers";
import { api } from "boot/axios";

const myMixin = {
  data() {
    return {};
  },
  created: function () {},
  methods: {
    greetings: function () {
      console.log("Howdy my good fellow!");
    },
    goPage(val) {
      this.$router.push(val);
    },
    getRestBase() {
      api
        .get("api/public/servingurl")
        .then((response) => {
          this.rest_base = response.data;
        })
        .catch((e) => {
          console.log(e);
        });
    },
  },
};

export default boot(({ app }) => {});

export { myMixin };
