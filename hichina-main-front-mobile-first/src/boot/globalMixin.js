import { boot } from "quasar/wrappers";

// "async" is optional;
// more info on params: https://v2.quasar.dev/quasar-cli/boot-files
export default boot(async (/* { app, router, ... } */ { app }) => {
  // something to do
  app.mixin({
    methods: {
      testGlobalMethod() {
        console.log("from global mixin method");
      },
      gotoUrl(url) {
        window.location = url;
      },
      goPage(val) {
        this.$router.push(val);
      },
    },
  });
});
