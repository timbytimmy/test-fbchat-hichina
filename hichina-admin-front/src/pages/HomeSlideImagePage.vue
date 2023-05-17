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
        label="标题"
        v-model="sliders[index - 1]['title']"
      ></q-input>
      <q-input
        class="col-12"
        label="副标题"
        v-model="sliders[index - 1]['subTitle']"
      ></q-input>
      <q-input
        class="col-12"
        label="指向博客链接"
        v-model="sliders[index - 1]['linkToBlog']"
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
  name: "HomeSlideImagePage",
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
      numberofslides: 3,
      sliders: [
        { imageUrl: "", title: "", subTitle: "", linkToBlog: "" },
        { imageUrl: "", title: "", subTitle: "", linkToBlog: "" },
        { imageUrl: "", title: "", subTitle: "", linkToBlog: "" },
      ],
    };
  },
  mounted() {
    this.loadExistingHomeSliders();
  },
  methods: {
    loadExistingHomeSliders() {
      api
        .get("/api/v1/pagecontent/homesliders")
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
          this.sliders[index].title == null ||
          this.sliders[index].title == ""
        ) {
          this.showNotifyMessageFail("第" + (index + 1) + "个轮播图的标题为空");
          return false;
        }
        if (
          this.sliders[index].subTitle == null ||
          this.sliders[index].subTitle == ""
        ) {
          this.showNotifyMessageFail(
            "第" + (index + 1) + "个轮播图的副标题为空"
          );
          return false;
        }
        if (
          this.sliders[index].linkToBlog == null ||
          this.sliders[index].linkToBlog == ""
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
          .post("/api/v1/pagecontent/homesliders", this.sliders)
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
      var obj = { imageUrl: "", title: "", subTitle: "", linkToBlog: "" };
      this.sliders.push(obj);
    },
    removeSlide() {
      if (this.numberofslides <= 3) {
        this.showNotifyMessageFail("最少3张轮播图");
        return;
      } else {
        this.sliders.pop();
        this.numberofslides -= 1;
      }
    },
    testMongo() {
      api
        .get("/api/v1/demo")
        .then((response) => {
          console.log(response.data);
          this.showNotifyMessageSucceed("成功插入");
        })
        .catch((e) => {
          console.log(e);
          this.showNotifyMessageFail("失败");
        });
    },
  },
};
</script>
