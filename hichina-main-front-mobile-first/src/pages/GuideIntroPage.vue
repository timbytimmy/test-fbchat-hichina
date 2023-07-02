<template>
  <q-page>
    <div class="q-pa-md">
      <q-carousel
        animated
        v-model="slide"
        navigation
        infinite
        height="380px"
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
          @click="goToBlog(item.linkTo)"
        >
        </q-carousel-slide>
      </q-carousel>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted } from "vue";
import { api } from "boot/axios";
export default {
  name: "GuideIntroPage",
  setup() {
    const sliders = ref([]);

    function loadSliders() {
      api
        .get("/api/public/pagecontent/guidebookintrosliders")
        .then(function (response) {
          console.log("guidebookintrosliders:");
          console.log(response.data.data);
          sliders.value = response.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });
    }

    onMounted(() => {
      loadSliders();
    });

    return {
      slide: ref(1),
      sliders,
    };
  },
};
</script>
