<template>
  <q-page padding>
    <q-btn
      color="primary"
      style="margin-left: 40px"
      label="提交"
      @click="submitPost"
    ></q-btn>
    <div
      class="row justify-left"
      style="margin-top: 50px; border-style: dotted"
    >
      <q-input
        label="海报图url"
        v-model="postImageUrl"
        class="col-12"
        placeholder="例:http://photoprism.hichinatravel.com/api/v1/t/a33b041a545ac346c67627708086dcca7e33c94f/3exfuda9/fit_2048"
      ></q-input>

      <q-input class="col-12" label="指向链接" v-model="postLink"></q-input>
      <label>图片预览:</label>
      <img style="height: 220px; width: 780px" :src="postImageUrl" />
    </div>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
export default {
  name: "HomePostPage",
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
  methods: {
    submitPost() {
      var params = {};
      params.postImageUrl = this.postImageUrl;
      params.postLink = this.postLink;
      api
        .post("/api/v1/pagecontent/homepostimage", params)
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
    },
  },
  data() {
    return {
      postImageUrl: "",
      postLink: "",
    };
  },
};
</script>
