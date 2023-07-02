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
    <div class="row q-pa-md">
      <div class="q-gutter-y-md col-6">
        <q-tabs v-model="tab" dense align="justify" class="text-primary">
          <q-tab :ripple="false" name="lg" label="Latest Guidebooks" />
          <q-tab
            :ripple="false"
            name="mdg"
            label="Most Downloaded Guidebooks"
          />
        </q-tabs>
      </div>
    </div>
    <div class="row justify-left">
      <div
        class="col-12 col-sm-4 col-md-2"
        v-for="item in guidebooks"
        :key="item.guideId"
      >
        <div class="q-pa-md">
          <q-card
            @click="gotoUrl(item.downloadUrl)"
            class="guidebook-card cursor-pointer rounded-borders"
            @mouseenter="hoverFlag = true"
            @mouseleave="hoverFlag = false"
          >
            <q-img
              class="rounded-borders"
              style="height: 100%"
              :src="item.coverImageUrl"
            >
              <div class="absolute-bottom">
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
  </q-page>
</template>

<script>
import { ref, onMounted } from "vue";
import { api } from "boot/axios";
export default {
  name: "GuideIntroPage",
  setup() {
    const sliders = ref([]);
    const pageSize = ref(1000);
    const currentPage = ref(1);
    const guidebooks = ref([]);

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

    function loadGuideBooks() {
      console.log("loading latest guidebooks...");
      var params = {};
      params.pageSize = pageSize.value;
      params.page = currentPage.value;
      (params.query = ""),
        api
          .get("/api/public/guidebook", {
            params: params,
          })
          .then(function (response) {
            console.log("all guidebook list:");
            guidebooks.value = response.data.data.data;
            console.log(guidebooks.value);
          })
          .catch(function (error) {
            console.log(error);
          });
    }

    onMounted(() => {
      loadSliders();
      loadGuideBooks();
    });

    return {
      hoverFlag: ref(true),
      slide: ref(1),
      sliders,
      tab: ref("lg"),
      guidebooks,
    };
  },
};
</script>
<style lang="sass" scoped>
.guidebook-card
  width: 100%
  max-width: 350px
  height: 300px
</style>
