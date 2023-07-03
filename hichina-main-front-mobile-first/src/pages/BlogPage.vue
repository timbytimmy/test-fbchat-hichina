<template>
  <q-page>
    <div
      class="row justify-center text-h4 text-weight-bold text-blue-6 q-mt-md"
    >
      Blogs of The Week
    </div>
    <div class="q-pa-md">
      <q-carousel
        animated
        v-model="slide"
        navigation
        infinite
        height="540px"
        :autoplay="autoplay"
        arrows
        transition-prev="slide-right"
        transition-next="slide-left"
        @mouseenter="autoplay = false"
        @mouseleave="autoplay = 2000"
      >
        <q-carousel-slide
          v-for="(item, index) in sliders"
          :key="index"
          :name="index"
          :img-src="item.imageUrl"
          class="cursor-pointer"
          @click="gotoUrl(item.linkToBlog)"
        >
          <div class="q-mt-xl q-ml-xl absolute-left custom-caption">
            <div style="height: 100px"></div>
            <div class="text-h1 text-white text-weight-bolder">
              {{ item.title }}
            </div>
            <div class="text-h5 text-white text-weight-medium">
              {{ item.subTitle }}
            </div>
          </div>
        </q-carousel-slide>
      </q-carousel>
    </div>
    <div class="row justify-center q-pa-md">
      <q-btn rounded color="primary" size="xl" label="Write your blog" />
    </div>
    <div class="row q-pa-md">
      <div class="q-gutter-y-md col-12 col-md-8">
        <q-tabs v-model="tab" dense align="justify" class="text-primary">
          <q-tab :ripple="false" name="lb" label="Latest Blogs" />
          <q-tab :ripple="false" name="mvm" label="Most Viewed in a Month" />
          <q-tab :ripple="false" name="bfl" label="Bloggers I'm following" />
        </q-tabs>
      </div>
    </div>
    <div class="row justify-left">
      <div
        class="col-12 col-sm-4 col-md-2"
        v-for="(item, index) in globalUnifiedItemList"
        v-bind:key="index"
      >
        <div class="q-pa-md">
          <q-card
            v-if="item.type === 'blog'"
            class="cursor-pointer"
            flat
            bordered
          >
            <q-img
              :src="item.value.headImageUrl"
              placeholder-src="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/tile_224"
            />

            <q-card-section>
              <div class="text-overline text-orange-9">
                {{ item.value.createdTime }}
              </div>
              <div class="text-h5 q-mt-sm q-mb-xs">{{ item.value.title }}</div>
              <div class="text-caption text-grey">
                {{ item.value.content }}
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-12">
        <p class="text-center" style="background-color: #b4b4b4">
          Scroll to load more
        </p>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted } from "vue";
import { api } from "boot/axios";
import { debounce } from "lodash";

export default {
  name: "BlogPage",
  setup() {
    const unifiedItemList = ref([]);
    const blogPageSizePage = ref(20);
    const currentPage = ref(1);
    const bloglist = ref([]);
    const sliders = ref([]);
    const totalBlogCount = ref(0);
    const globalUnifiedItemList = ref([]);

    function loadBlogList() {
      unifiedItemList.value = [];
      var params = {};
      params.pageSize = blogPageSizePage.value;
      params.query = "";
      params.page = currentPage.value;
      api
        .get("/api/public/blog/list", { params: params })
        .then(function (response) {
          bloglist.value = response.data.data.data;
          totalBlogCount.value = response.data.data.total;
          for (var index in bloglist.value) {
            var obj = {};
            // obj.type=parseInt(Math.random() * 2)==0?'blog':'scaleblog';
            obj.type = "blog";
            obj.value = bloglist.value[index];
            unifiedItemList.value.push(obj);
          }
          globalUnifiedItemList.value = globalUnifiedItemList.value.concat(
            unifiedItemList.value
          );
        })
        .catch(function (error) {
          console.log(error);
        });
    }

    function loadSliders() {
      api
        .get("/api/public/pagecontent/bloghomesliders")
        .then(function (response) {
          console.log("blog sliders:");
          sliders.value = response.data.data;
          console.log(sliders.value);
          loadBlogList();
        })
        .catch(function (error) {
          console.log(error);
        });
    }

    function loadMore() {
      currentPage.value += 1;
      var maxPage = totalBlogCount.value / blogPageSizePage.value;
      if (currentPage.value <= maxPage + 1) {
        loadBlogList();
      }
    }

    function getNextBatch() {
      window.onscroll = debounce(function () {
        let bottomOfWindow =
          document.documentElement.scrollTop + window.innerHeight + 100 >
          document.documentElement.scrollHeight;

        if (bottomOfWindow) {
          loadMore();
        }
      }, 500);
    }

    onMounted(() => {
      loadSliders();
      getNextBatch();
    });

    return {
      slide: ref(1),
      autoplay: ref(true),
      sliders,
      tab: ref("lb"),
      globalUnifiedItemList,
    };
  },
};
</script>
<style lang="sass" scoped>
.custom-caption
  text-align: left
  padding: 12px
  color: white
</style>

