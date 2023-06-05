<template>
  <div class="main">
    <p class="title">{{ title }}</p>
    <div class="flex-start mt-36">
      <v-lazy-image
        class="logo"
        :src="authorProfileImageUrl"
        src-placeholder="https://photoprism.hichinatravel.com/api/v1/t/07dac61cc5dfec34dd9358f37bd70ce32de68488/32mcf2k4/fit_720"
        alt=""
      />
      <div class="name flex-item ml-24">
        <p>{{ username }}</p>
        <el-button class="follow mt-10">Follow</el-button>
      </div>
    </div>
    <div class="info flex-between mt-30">
      <p>{{ createdTime }}</p>
      <p class="flex-between">
        <el-icon size="24">
          <svg-icon icon-class="eye-open" color="#2a82e4" />
        </el-icon>
        <span>{{pageViewCnt}}</span>
      </p>
      <p class="flex-between">
        <span>Collect this</span>
        <el-icon size="30">
          <svg-icon icon-class="book" color="#2a82e4" />
        </el-icon>
      </p>
    </div>
  </div>

  <div class="content">
    <div class="content-main">
      <div class="rich-text" v-html="content"></div>
    </div>
  </div>

  <!-- <div class="list" :class="{show: scrollY > 200}">
    <div class="list-item">
      <p>Relavant Destinations</p>
      <div class="img mt-4">
        <v-lazy-image src="@/assets/images/banner-1.jpg" alt="" />
        <span class="tag">Dali</span>
      </div>
    </div>
    <div class="list-item">
      <p>Relavant Tours</p>
      <div class="img mt-4">
        <v-lazy-image src="@/assets/images/banner-1.jpg" alt="" />
        <span class="tag">Dali</span>
      </div>
    </div>
  </div> -->
</template>

<script setup>
import VLazyImage from "v-lazy-image";
import { useRouter } from "vue-router";
import { useWindowScroll } from "@vueuse/core";
import { AXIOS } from "@/common/http-commons";
import { useSeoMeta } from 'unhead'
const { y: scrollY } = useWindowScroll();
console.log(scrollY.value);


const title = ref("");
const username = ref("");
const content = ref("");
const createdTime = ref("");
const authorInfo = ref({});
const authorProfileImageUrl = ref("");
const pageViewCnt = ref(0)

const siteData = reactive({
  title: 'My Awesome Blog',
  shortContent: 'Hichina Travel Blog'
})

// useHead({
//   title: computed(()=>'fuck'),
//   meta: [
//     { hid: 'Description', name: 'Description', content: removeHtmlTag(content.value).substring(0,50)},
//     { hid: 'Keywords', name: 'Keywords', content: title.value}
//   ]
// })


const route = useRoute();

function getAuthorPrincipal() {
  AXIOS.get("/api/public/blog/blog-author-profile-image/" + route.params.blogId)
    .then(function (response) {
      console.log("blog author profile image");
      console.log(response.data);
      if (response.data.ok == true) {
        authorProfileImageUrl.value = response.data.data;
      } else {
        ElNotification({
          title: "Error",
          message: response.data.message,
          type: "error",
        });
      }
    })
    .catch(function (error) {
      console.log(error);
    });
}

function loadBlogDetail() {
  AXIOS.get("/api/public/blog/" + route.params.blogId)
    .then((response) => {
      title.value = response.data.data.title;
      username.value = response.data.data.authorName;
      content.value = response.data.data.content;
      createdTime.value = response.data.data.createdTime;

      // set head meta for seo
      // Set the title meta tag
      siteData.title = title.value;
      siteData.shortContent = removeHtmlTag(content.value).substring(0,50)


      useSeoMeta({
        title: siteData.title,
        description: siteData.shortContent,
        ogDescription: siteData.shortContent,
        ogTitle: siteData.title,
        ogImage: 'https://www.hichinatravel.com/static/png/name-67280b81.png',
        twitterCard: 'summary_large_image',
      })
    })
    .catch((e) => {
      console.log("get blog detail err");
      console.log(e);
    });

}

function removeHtmlTag(input){
  var div = document.createElement("div");
  div.innerHTML = input;
  var text = div.textContent || div.innerText || "";
  return text;
}

function logView(){
  AXIOS.post("/api/public/pagestats/view-blog/"+route.params.blogId)
  .then((res) => {
    console.log("view cnt of this blog:")
    console.log(res.data);
    pageViewCnt.value = res.data.data
  })
  .catch((err) => {
    console.error("Error:", err);
  });
}

onMounted(() => {
  logView()
  getAuthorPrincipal();
  loadBlogDetail();
});
</script>

<style scoped lang="scss">
.content-main,
.main {
  min-width: 560px;
  width: 70%;
  padding-left: 100px;
  margin: 0 auto;
}

.main {
  padding-top: 105px;
}

.title {
  color: rgba(80, 80, 80, 1);
  font-size: 3em;
  font-weight: bold;
}

.logo {
  width: 104px;
  height: 104px;
  border-radius: 50%;
}

.name {
  color: rgba(80, 80, 80, 1);
  font-size: 26px;
}

.follow {
  width: 157px;
  height: 30px;
  color: rgba(255, 255, 255, 1);
  background-color: rgba(42, 130, 228, 1);
  border-radius: 5px;
  font-size: 19px;
  line-height: 30px;
  text-align: center;
}

.info {
  color: rgba(80, 80, 80, 1);
  font-size: 27px;
}

.content {
  margin: 40px 17px 0;
  border-top: 1px solid #e5e5e5;
}

.rich-text {
  color: rgba(80, 80, 80, 1);
  font-size: 22px;
}

.content-main {
  padding-top: 28px;

  img {
    width: 100%;
  }
}

.list {
  position: fixed;
  left: -251px;
  bottom: 300px;
  transition: 0.3s;

  &.show {
    left: 20px;
  }

  &-item {
    margin-bottom: 33px;
    color: rgba(42, 130, 228, 1);
    font-size: 17px;
    text-align: center;
  }

  .img {
    position: relative;
    width: 201px;
    height: 155px;

    img {
      width: 100%;
      height: 100%;
    }

    .tag {
      position: absolute;
      left: 0;
      top: 25px;
      width: 56px;
      height: 30px;
      color: rgba(255, 255, 255, 1);
      background-color: rgba(42, 130, 228, 1);
      border-radius: 4px;
      font-size: 12px;
      line-height: 30px;
      text-align: center;
    }
  }
}
</style>