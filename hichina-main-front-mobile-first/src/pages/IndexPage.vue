<template>
  <q-page>
    <div class="q-pa-md">
      <q-carousel
        animated
        v-model="slide"
        navigation
        infinite
        :autoplay="2000"
        arrows
        transition-prev="slide-right"
        transition-next="slide-left"
        @mouseenter="autoplay = false"
        @mouseleave="autoplay = true"
      >
        <q-carousel-slide
          :name="1"
          img-src="https://cdn.quasar.dev/img/mountains.jpg"
        />
        <q-carousel-slide
          :name="2"
          img-src="https://cdn.quasar.dev/img/parallax1.jpg"
        />
        <q-carousel-slide
          :name="3"
          img-src="https://cdn.quasar.dev/img/parallax2.jpg"
        />
        <q-carousel-slide
          :name="4"
          img-src="https://cdn.quasar.dev/img/quasar.jpg"
        />
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
    };
  },
});
</script>
