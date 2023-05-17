<template>
  <q-page padding>
    <q-btn
      round
      title="增加一张"
      color="primary"
      icon="add"
      @click="addSlide"
    />
    <q-btn
      round
      title="减少一张"
      color="orange"
      style="margin-left: 10px"
      icon="remove"
      @click="removeSlide"
    />
    <q-btn
      color="primary"
      style="margin-left: 40px"
      label="提交"
      @click="submitSliders"
    ></q-btn>
    <label style="margin-left: 40px">当前轮播图数:{{ numberofslides }}</label>
    <div
      class="row justify-left"
      style="margin-top: 50px; border-style: dotted"
      v-for="index in numberofslides"
      :key="index"
    >
      <q-input
        label="滚动图URL"
        v-model="sliders[index - 1]['imageUrl']"
        class="col-12"
        placeholder="例:http://photoprism.hichinatravel.com/api/v1/t/a33b041a545ac346c67627708086dcca7e33c94f/3exfuda9/fit_2048"
      ></q-input>
      <q-input
        class="col-12"
        label="指向链接"
        v-model="sliders[index - 1]['linkTo']"
      ></q-input>
      <label>图片预览:</label>
      <img
        style="height: 220px; width: 780px"
        :src="sliders[index - 1]['imageUrl']"
      />
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
export default {
  name: "GuidebookIntroManagePage",
  setup() {
    const $q = useQuasar();

    return {
      onRowClick: (row) => alert(`${row.name} clicked`),
      showNotifyMessageFail(msg) {
        $q.notify({
          message: msg,
          color: "red",
          position: "top-right",
        });
      },
      showNotifyMessageSucceed(msg) {
        $q.notify({
          message: msg,
          color: "green",
          position: "top-right",
        });
      },
    };
  },
  data() {
    return {
      numberofslides: 1,
      sliders: [{ imageUrl: "", linkTo: "" }],
    };
  },
  mounted() {
    this.loadExistingGuidebookIntroSliders();
  },
  methods: {
    loadExistingGuidebookIntroSliders() {
      api
        .get("/api/v1/pagecontent/guidebookintrosliders")
        .then((response) => {
          if (response.data.ok == true) {
            this.sliders = response.data.data;
            this.numberofslides = response.data.data.length;
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          console.log(e);
          storeThis.showNotifyMessageFail(e.response);
        });
    },
    validateSliders() {
      for (var index in this.sliders) {
        if (
          this.sliders[index].imageUrl == null ||
          this.sliders[index].imageUrl == ""
        ) {
          this.showNotifyMessageFail(
            "第" + (eval(index) + 1) + "个轮播图的图片链接为空"
          );
          return false;
        }
        if (
          this.sliders[index].linkTo == null ||
          this.sliders[index].linkTo == ""
        ) {
          this.showNotifyMessageFail(
            "第" + (index + 1) + "个轮播图的指向博客链接为空"
          );
          return false;
        }
      }
      return true;
    },
    submitSliders() {
      if (!this.validateSliders()) {
        return;
      } else {
        api
          .post("/api/v1/pagecontent/guidebookintrosliders", this.sliders)
          .then((response) => {
            if (response.data.ok === true) {
              this.showNotifyMessageSucceed(response.data.message);
            } else {
              this.showNotifyMessageFail(response.data.message);
            }
          })
          .catch((e) => {
            this.showNotifyMessageFail(e.toString());
          });
      }
    },
    addSlide() {
      this.numberofslides += 1;
      var obj = { imageUrl: "", linkTo: "" };
      this.sliders.push(obj);
    },
    removeSlide() {
      if (this.numberofslides <= 1) {
        this.showNotifyMessageFail("最少1张轮播图");
        return;
      } else {
        this.sliders.pop();
        this.numberofslides -= 1;
      }
    },
  },
};
</script>
