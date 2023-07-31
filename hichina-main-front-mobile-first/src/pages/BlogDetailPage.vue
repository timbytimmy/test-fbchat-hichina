<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-12 col-md-9" style="min-height: 500px">
        <div class="text-weight-bold text-h3 q-pa-xl">
          {{ removeHtmlTag(title) }}
        </div>
        <div class="row col-12" style="min-height: 50px">
          <div class="q-ml-xl">
            <q-avatar size="86px">
              <img :src="authorProfileImageUrl" />
            </q-avatar>
          </div>
          <div class="col-3 q-ml-md">
            <div class="text-weight-bold text-h6">{{ username }}</div>
            <div><q-btn color="primary" icon="add" label="Follow" /></div>
          </div>
          <div class="col q-mt-md">
            <q-btn
              icon="g_translate"
              @click="translateThisArticle(content)"
              label="Translate"
              dense
              rounded
              stack
              glossy
              color="primary"
            />
          </div>
        </div>
        <div class="row q-px-xl q-py-md text-h5">
          <div class="col-5">{{ createdTime }}</div>
          <div class="col-3">
            <q-icon color="blue" name="visibility" />{{ pageViewCnt }}
          </div>
          <div class="col-4">
            Collect this<q-btn
              class="q-ml-sm"
              round
              color="primary"
              icon="bookmark"
              size="xs"
            />
          </div>
        </div>
        <div
          class="q-px-xl q-pt-md"
          style="border-top: 1px solid black"
          v-html="content"
        ></div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance, reactive } from "vue";
import { api } from "boot/axios";
import { apiRaw } from "boot/axiosraw";
import { useRoute } from "vue-router";
import { useQuasar } from "quasar";
import { useSeoMeta } from "unhead";
import { useI18n } from "vue-i18n";

export default {
  name: "BlogDetail",
  setup() {
    const { locale } = useI18n({ useScope: "global" });

    const route = useRoute();
    const instance = getCurrentInstance();
    const app = getCurrentInstance().appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const pageViewCnt = ref(0);
    const authorProfileImageUrl = ref("");
    const title = ref("");
    const username = ref("");
    const content = ref("");
    const createdTime = ref("");

    const siteData = reactive({
      title: "My Awesome Blog",
      shortContent: "Hichina Travel Blog",
    });

    function logView() {
      api
        .post("/api/public/pagestats/view-blog/" + route.params.blogId)
        .then((res) => {
          console.log("view cnt of this blog:");
          console.log(res.data);
          pageViewCnt.value = res.data.data;
        })
        .catch((err) => {
          console.error("Error:", err);
        });
    }

    function toBaiduLanguage(input) {
      if (input === "ko-KR") {
        return "kor";
      } else if (input === "en-US") {
        return "en";
      } else if (input === "ru-RU") {
        return "ru";
      } else if (input === "th-TH") {
        return "th";
      } else {
        return "auto";
      }
    }

    function translateTitle() {
      const params = {};
      // key format: type_guid_part_tolang example: blog_f4128363-84bb-4169-a30c-a3d6fbc0a3ef_title_th
      params.translationKey =
        "blog_" +
        route.params.blogId +
        "_title_" +
        toBaiduLanguage(locale.value);
      params.query = title.value;
      params.from = "auto";
      params.to = toBaiduLanguage(locale.value);
      gp.$showLoading($q);
      api
        .post("/api/public/baidutranslate", params)
        .then((res) => {
          console.log("baidu translate result title: ");
          console.log(res.data);
          if (res.data.ok == true) {
            title.value = res.data.data;
          } else {
            console.log("translate title failed");
          }

          gp.$hideLoading($q);
        })
        .catch((err) => {
          console.log("err baidu translate title");
          gp.$hideLoading($q);
        });
    }

    function translateContent() {
      const params = {};
      // key format: type_guid_part_tolang example: blog_f4128363-84bb-4169-a30c-a3d6fbc0a3ef_content_th
      params.translationKey =
        "blog_" +
        route.params.blogId +
        "_content_" +
        toBaiduLanguage(locale.value);
      params.query = content.value;
      params.from = "auto";
      params.to = toBaiduLanguage(locale.value);
      gp.$showLoading($q);
      api
        .post("/api/public/baidutranslate", params)
        .then((res) => {
          console.log("baidu translate result: ");
          console.log(res.data);
          if (res.data.ok == true) {
            content.value = res.data.data;
            translateTitle();
          } else {
            console.log("translate failed");
          }

          gp.$hideLoading($q);
        })
        .catch((err) => {
          console.log("err baidu translate");
          gp.$hideLoading($q);
        });
    }
    function translateThisArticle(textToTranslate) {
      translateContent();
    }

    function getAuthorPrincipal() {
      api
        .get(
          "/api/public/blog/blog-author-profile-image/" + route.params.blogId
        )
        .then(function (response) {
          console.log("blog author profile image");
          console.log(response.data);
          if (response.data.ok == true) {
            authorProfileImageUrl.value = response.data.data;
            if (
              authorProfileImageUrl.value == null ||
              authorProfileImageUrl.value.length < 1
            ) {
              authorProfileImageUrl.value =
                "https://photoprism.hichinatravel.com/api/v1/t/8623903789c65a160279faa0b33159413cb18af4/32mcf2k4/fit_2048";
            }
          } else {
            gp.$generalNotify($q, false, response.data.message);
          }
        })
        .catch(function (error) {
          console.log(error);
        });
    }

    function loadBlogDetail() {
      api
        .get("/api/public/blog/" + route.params.blogId)
        .then((response) => {
          title.value = response.data.data.title;
          username.value = response.data.data.authorName;
          content.value = response.data.data.content;
          createdTime.value = response.data.data.createdTime;

          // set head meta for seo
          // Set the title meta tag
          siteData.title = title.value;
          siteData.shortContent = gp
            .$removeHtmlTag(content.value)
            .substring(0, 50);

          useSeoMeta({
            title: siteData.title,
            description: siteData.shortContent,
            ogDescription: siteData.shortContent,
            ogTitle: siteData.title,
            ogImage:
              "https://www.hichinatravel.com/static/png/name-67280b81.png",
            twitterCard: "summary_large_image",
          });
        })
        .catch((e) => {
          console.log("get blog detail err");
          console.log(e);
        });
    }

    onMounted(() => {
      logView();
      getAuthorPrincipal();
      loadBlogDetail();
    });

    return {
      title,
      authorProfileImageUrl,
      username,
      createdTime,
      pageViewCnt,
      content,
      translateThisArticle,
    };
  },
};
</script>
