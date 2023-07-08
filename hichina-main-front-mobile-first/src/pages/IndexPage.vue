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
          @click="gotoUrl(item.linkToBlog)"
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
        :key="item.blogId"
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
          class="cursor-pointer col-12 q-mt-md q-pt-md rounded-borders text-white text-weight-bold text-subtitle1 text-no-wrap text-center"
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
    <div class="q-pa-md">
      <div class="row">
        <div class="col-12 cursor-pointer" style="height: 160px">
          <q-img :src="homePostImageUrl" fit="fill" style="height: 100%">
          </q-img>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="text-h5 text-weight-bold text-blue-6 q-pl-md q-mt-md">
        People are traveling!
      </div>
    </div>
    <div class="row">
      <div class="text-h3 text-weight-medium q-pl-md q-mt-md">
        What's in China!
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
          <q-card
            v-if="item.type === 'product'"
            class="cursor-pointer"
            flat
            bordered
          >
            <q-img
              :src="item.value.imageUrl"
              placeholder-src="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/tile_224"
            />

            <q-card-section>
              <div class="text-subtitle1 text-pink">
                Start from ￥{{ item.value.minPrice }}
              </div>
              <div class="text-h5 q-mt-sm q-mb-xs">
                {{ item.value.skuGroupName }}
              </div>
            </q-card-section>

            <q-slide-transition>
              <div v-show="expanded">
                <q-separator />
                <q-card-section class="text-subitle2">
                  {{ lorem }}
                </q-card-section>
              </div>
            </q-slide-transition>
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
import { defineComponent } from "vue";
import { debounce } from "lodash";
import { api } from "boot/axios";

export default defineComponent({
  name: "IndexPage",
  setup() {
    const sliders = ref([]);
    const randDestinations = ref([]);
    const hoverFlag = ref(false);
    const homePostLink = ref("");
    const homePostImageUrl = ref("");

    const currentPage = ref(1);
    const totalBlogCount = ref(-1);
    const blogPageSize = ref(30);
    const productPageSizePage = ref(5);
    const loading = ref(false);

    const bloglist = ref([]);
    const productlist = ref([]);

    const globalUnifiedItemList = ref([]);
    const unifiedItemList = ref([]);

    // function goToBlog(url) {
    //   window.location.href = url;
    // }

    // function normalizeMultiImageUrl(input) {
    //   if (input.indexOf(",") > -1) {
    //     return input.split(",").shift();
    //   } else if (input.indexOf(";") > -1) {
    //     return input.split(";").shift();
    //   }
    //   return input;
    // }

    function loadRand6Destinations() {
      api
        .get("/api/public/destination/rand6")
        .then((response) => {
          randDestinations.value = response.data.data;
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
          loadBlogList();
        })
        .catch((e) => {
          console.log(e);
        });
    }

    function loadHomePost() {
      api
        .get("/api/public/pagecontent/homepost")
        .then((response) => {
          homePostLink.value = response.data.data.postLink;
          homePostImageUrl.value = response.data.data.postImageUrl;
        })
        .catch((e) => {
          console.log(e);
        });
    }

    function loadProducts() {
      var params = {};
      params.pageSize = productPageSizePage.value;
      params.query = "";
      params.page = currentPage.value;
      params.productTypeId = "";
      api
        .get("/api/public/productsku/productskugrouplist", { params: params })
        .then(function (response) {
          productlist.value = response.data.data.data;

          for (var index in productlist.value) {
            var obj = {};
            obj.type = "product";
            obj.value = productlist.value[index];
            unifiedItemList.value.push(obj);
          }

          globalUnifiedItemList.value = globalUnifiedItemList.value.concat(
            unifiedItemList.value
          );

          loading.value = false;
        })
        .catch(function (error) {
          console.log(error);
        });
    }

    function loadBlogList() {
      loading.value = true;
      unifiedItemList.value = [];
      var params = {};
      params.pageSize = blogPageSize.value;
      params.query = "";
      console.log("loading page: " + currentPage.value);
      params.page = currentPage.value;
      api
        .get("/api/public/blog/list", { params: params })
        .then(function (response) {
          if (totalBlogCount.value == -1) {
            totalBlogCount.value = response.data.data.total;
          }
          bloglist.value = response.data.data.data;
          for (var index in bloglist.value) {
            var obj = {};
            // obj.type=parseInt(Math.random() * 2)==0?'blog':'scaleblog';
            obj.type = "blog";
            obj.value = bloglist.value[index];
            unifiedItemList.value.push(obj);
          }
          loadProducts();
        })
        .catch(function (error) {
          console.log(error);
        });
    }

    function loadMore() {
      currentPage.value += 1;
      var maxPage = totalBlogCount.value / blogPageSize.value;
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
      // we call "next()" method of our component
      loadHomeSliders();
      loadRand6Destinations();
      loadHomePost();
      getNextBatch();
    });

    return {
      slide: ref(1),
      autoplay: ref(true),
      sliders,
      randDestinations,
      hoverFlag,
      homePostLink,
      homePostImageUrl,
      globalUnifiedItemList,
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
  max-width: 550px
  height: 300px
</style>
