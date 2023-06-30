<template>
  <q-page>
    <div class="q-pa-md">
      <q-carousel
        animated
        v-model="slide"
        navigation
        infinite
        height="540px"
        :autoplay="2000"
        arrows
        transition-prev="slide-right"
        transition-next="slide-left"
        @mouseenter="autoplay = false"
        @mouseleave="autoplay = true"
      >
        <q-carousel-slide
          v-for="(item, index) in sliders"
          :key="index"
          :name="index"
          :img-src="item.imageUrl"
          class="cursor-pointer"
          @click="goToBlog(item.linkToBlog)"
        >
          <div class="q-mt-xl q-ml-xl absolute-left custom-caption">
            <div style="height: 100px"></div>
            <div class="text-h1 text-weight-bolder">{{ item.title }}</div>
            <div class="text-h5 text-weight-medium">{{ item.subTitle }}</div>
          </div>
        </q-carousel-slide>
      </q-carousel>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted } from "vue";
import { defineComponent } from "vue";
import { api } from "boot/axios";

export default defineComponent({
  name: "IndexPage",
  setup() {
    const sliders = ref([]);

    function goToBlog(url) {
      window.location.href = url;
    }

    function loadHomeSliders() {
      api
        .get("/api/public/pagecontent/homesliders")
        .then((response) => {
          sliders.value = response.data.data;
          console.log("sliders.value");
          console.log(sliders.value);
          // loadBlogList();
        })
        .catch((e) => {
          console.log(e);
        });
    }

    onMounted(() => {
      // we call "next()" method of our component
      loadHomeSliders();
    });

    return {
      slide: ref(1),
      autoplay: ref(true),
      sliders,
      goToBlog,
    };
  },
});
</script>
<style lang="sass" scoped>
.custom-caption
  text-align: left
  padding: 12px
  color: white
</style>
