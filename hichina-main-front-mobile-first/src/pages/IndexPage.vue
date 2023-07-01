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
    <div class="q-pa-md" style="height: 40px"></div>
    <div class="row">
      <div class="col-6 col-md-2 text-blue-6 text-h4 q-pl-md">Destinations</div>
      <div class="col-md-8"></div>
      <div class="col-6 col-md-2 text-blue-6 cursor-pointer">
        More destinations >
      </div>
    </div>
    <div class="row">
      <div class="text-h3 q-pl-md q-mt-md">Places to go in China!</div>
    </div>
    <div class="row justify-center">
      <div
        class="col-12 col-sm-4 col-md-2"
        v-for="item in randDestinations"
        :key="item.destinationId"
      >
        <div class="q-pa-md">
          <q-card
            class="rand-destination-card cursor-pointer rounded-borders"
            @mouseenter="hoverFlag = true"
            @mouseleave="hoverFlag = false"
          >
            <q-img
              class="rounded-borders"
              style="height: 100%"
              :src="normalizeMultiImageUrl(item.destinationProfileImage)"
            >
              <div class="absolute-bottom" v-if="hoverFlag">
                <div class="text-h6">{{ item.destinationName }}</div>
                <div class="text-subtitle2">
                  {{ item.parentDestinationName }}
                </div>
              </div>
            </q-img>
          </q-card>
        </div>
      </div>
    </div>
    <div class="q-pa-md">
      <div class="row">
        <div
          v-if="$q.screen.gt.xs"
          class="cursor-pointer col-12 q-mt-md q-pt-md rounded-borders text-white text-weight-bold text-h5 text-no-wrap text-center"
          style="
            background-color: #2a82e4;
            height: 80px;
            border-radius: 20px;
            border: 1px solid black;
          "
        >
          Want a Tailor Made Trip to China? Click Here for Help
        </div>
        <div
          v-if="!$q.screen.gt.xs"
          class="cursor-pointer col-12 q-mt-md q-pt-md rounded-borders text-white text-weight-bold text-h6 text-no-wrap text-center"
          style="
            background-color: #2a82e4;
            height: 80px;
            border-radius: 20px;
            border: 1px solid black;
          "
        >
          Want a Tailor Made Trip to China? Click Here for Help
        </div>
      </div>
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
    const randDestinations = ref([]);
    const hoverFlag = ref(false);
    function goToBlog(url) {
      window.location.href = url;
    }

    function normalizeMultiImageUrl(input) {
      if (input.indexOf(",") > -1) {
        return input.split(",").shift();
      } else if (input.indexOf(";") > -1) {
        return input.split(";").shift();
      }
      return input;
    }

    function loadRand6Destinations() {
      api
        .get("/api/public/destination/rand6")
        .then((response) => {
          randDestinations.value = response.data.data;
          console.log("randDestinations.value");
          console.log(randDestinations.value);
        })
        .catch((e) => {
          alert(e);
        });
    }

    function loadHomeSliders() {
      api
        .get("/api/public/pagecontent/homesliders")
        .then((response) => {
          sliders.value = response.data.data;
          // loadBlogList();
        })
        .catch((e) => {
          console.log(e);
        });
    }

    onMounted(() => {
      // we call "next()" method of our component
      loadHomeSliders();
      loadRand6Destinations();
    });

    return {
      slide: ref(1),
      autoplay: ref(true),
      sliders,
      randDestinations,
      hoverFlag,
      goToBlog,
      normalizeMultiImageUrl,
    };
  },
});
</script>
<style lang="sass" scoped>
.custom-caption
  text-align: left
  padding: 12px
  color: white

.rand-destination-card
  width: 100%
  max-width: 350px
  height: 300px
</style>
