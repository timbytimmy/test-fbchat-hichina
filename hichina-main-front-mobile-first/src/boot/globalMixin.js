import { boot } from "quasar/wrappers";
import vueCountryRegionSelect from "vue3-country-region-select";
// "async" is optional;
// more info on params: https://v2.quasar.dev/quasar-cli/boot-files
export default boot(async (/* { app, router, ... } */ { app, router }) => {
  // something to do
  const testGlobal2 = () => {
    console.log("testGlobal2");
  };

  app.use(vueCountryRegionSelect);

  const normalizeMultiImageUrl = (input) => {
    if (input.indexOf(",") > -1) {
      return input.split(",").shift();
    } else if (input.indexOf(";") > -1) {
      return input.split(";").shift();
    }
    return input;
  };

  const removeHtmlTag = (input) => {
    var div = document.createElement("div");
    div.innerHTML = input;
    var text = div.textContent || div.innerText || "";
    return text;
  };

  const generalNotify = (q, isPositive, message) => {
    q.notify({
      position: "top-right",
      timeout: 2500,
      color: isPositive ? "positive" : "negative",
      textColor: "white",
      message: message,
      actions: [{ icon: "close", color: "white" }],
    });
  };

  const goPage = (val) => {
    router.push(val);
  };

  app.config.globalProperties.$testGlobal2 = testGlobal2;
  app.config.globalProperties.$normalizeMultiImageUrl = normalizeMultiImageUrl;
  app.config.globalProperties.$removeHtmlTag = removeHtmlTag;
  app.config.globalProperties.$generalNotify = generalNotify;
  app.config.globalProperties.$goPage = goPage;

  app.mixin({
    methods: {
      testGlobal2,
      generalNotify,
      normalizeMultiImageUrl,
      removeHtmlTag,
      gotoUrl(url) {
        window.location = url;
      },
      goPage,
    },
  });
});
