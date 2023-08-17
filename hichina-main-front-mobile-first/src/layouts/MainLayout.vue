<template>
  <q-layout view="hHh lpR fff">
    <q-header height-hint="76" reveal class="bg-white text-black">
      <q-toolbar class="GPL__toolbar" style="height: 76px">
        <div class="row">
          <div class="col-sm-12 col-xs-12 q-ml-xl q-mr-lg">
            <q-toolbar-title>
              <img
                @click="$router.push('/')"
                class="cursor-pointer float-left"
                src="~/assets/hichinalogo.png"
              />
            </q-toolbar-title>
          </div>
        </div>
        <q-btn
          @click="goPage('/guideintro')"
          flat
          no-caps
          no-wrap
          class="q-ml-xl"
          v-if="$q.screen.gt.xs"
        >
          {{ $t("guidebooks") }}
        </q-btn>
        <q-btn
          @click="goPage('/blog')"
          flat
          no-caps
          no-wrap
          class="q-ml-sm"
          v-if="$q.screen.gt.xs"
        >
          {{ $t("blogs_vlogs") }}
        </q-btn>
        <q-btn
          @click="goPage('/product')"
          flat
          no-caps
          no-wrap
          class="q-ml-sm"
          v-if="$q.screen.gt.xs"
        >
          {{ $t("travel_shop") }}
        </q-btn>
        <q-btn
          @click="goPage('/destination')"
          flat
          no-caps
          no-wrap
          class="q-ml-sm"
          v-if="$q.screen.gt.xs"
        >
          {{ $t("destinations") }}
        </q-btn>

        <!-- this is the trick -->
        <q-space />

        <div class="row no-wrap q-mr-md">
          <q-select
            v-model="locale"
            :options="localeOptions"
            :label="$t('language')"
            dense
            emit-value
            map-options
            options-dense
            style="min-width: 150px"
            @update:model-value="setLanguage"
            ><template v-slot:append> <q-icon name="public" /> </template
          ></q-select>
        </div>

        <div v-if="currentUser === ''" class="row no-wrap">
          <q-btn
            @click="goPage('/auth/register')"
            flat
            dense
            no-wrap
            no-caps
            color="primary"
            :label="$t('register')"
            class="q-mr-md"
            v-if="$q.screen.gt.xs"
          />
          <q-btn
            @click="goPage('/auth/login')"
            flat
            dense
            no-wrap
            no-caps
            color="primary"
            :label="$t('login')"
            class="q-mr-sm"
            v-if="$q.screen.gt.xs"
          />
        </div>
        <div v-if="currentUser != ''" class="row">
          <q-btn
            v-if="$q.screen.gt.xs"
            round
            flat
            @click="goPage('/user-info')"
          >
            <q-avatar size="36px">
              <img :src="currentProfileImage" />
            </q-avatar>
            <q-tooltip>{{ currentUser }}</q-tooltip>
          </q-btn>
        </div>
        <div v-if="currentUser != '' && $q.screen.gt.xs" class="row q-mr-xl">
          <q-btn icon="arrow_drop_down" flat dense>
            <q-menu
              style="width: 100px"
              transition-show="flip-right"
              transition-hide="flip-left"
            >
              <q-list dense class="text-grey-9 text-caption">
                <q-item clickable @click="goPage('/my-blogs')">
                  <q-item-section>{{ $t("my_blogs") }}</q-item-section>
                </q-item>
                <q-item clickable @click="goPage('/my-orders')">
                  <q-item-section>{{ $t("my_orders") }}</q-item-section>
                </q-item>
                <q-item clickable @click="goPage('/user-info')">
                  <q-item-section>{{ $t("edit_profile") }}</q-item-section>
                </q-item>
                <q-separator />
                <q-item clickable @click="logout()">
                  <q-item-section>{{ $t("logout") }}</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
        </div>
        <!-- <div v-if="currentUser != ''" class="row no-wrap q-ml-md">
          <q-btn
            @click="logout()"
            v-if="$q.screen.gt.xs"
            round
            dense
            color="primary"
            class="q-mr-md"
            icon="logout"
            ><q-tooltip>logout</q-tooltip></q-btn
          >
        </div> -->
        <div>
          <q-btn
            v-if="!$q.screen.gt.xs"
            flat
            dense
            round
            @click="toggleLeftDrawer"
            icon="menu"
            aria-label="Menu"
          />
        </div>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" bordered class="bg-grey-2" :width="240">
      <q-scroll-area class="fit">
        <q-list padding>
          <q-item v-ripple clickable @click="goPage('/guideintro')">
            <q-item-section avatar>
              <q-icon color="grey" name="fingerprint" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("guidebooks") }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-ripple clickable @click="goPage('/blog')">
            <q-item-section avatar>
              <q-icon color="grey" name="fingerprint" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("blogs_vlogs") }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-ripple clickable @click="goPage('/product')">
            <q-item-section avatar>
              <q-icon color="grey" name="fingerprint" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("travel_shop") }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-ripple clickable @click="goPage('/destination')">
            <q-item-section avatar>
              <q-icon color="grey" name="fingerprint" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("destinations") }}</q-item-label>
            </q-item-section>
          </q-item>

          <q-separator class="q-my-md" />

          <q-item
            v-if="currentUser === ''"
            v-ripple
            clickable
            @click="goPage('/auth/login')"
          >
            <q-item-section avatar>
              <q-icon color="grey" name="login" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("login") }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item
            v-if="currentUser === ''"
            v-ripple
            clickable
            @click="goPage('/auth/register')"
          >
            <q-item-section avatar>
              <q-icon color="grey" name="account_circle" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("register") }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item
            v-if="currentUser !== ''"
            v-ripple
            clickable
            @click="goPage('/my-blogs')"
          >
            <q-item-section avatar>
              <q-icon color="grey" name="rss_feed" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("my_blogs") }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item
            v-if="currentUser !== ''"
            v-ripple
            clickable
            @click="goPage('/my-orders')"
          >
            <q-item-section avatar>
              <q-icon color="grey" name="list_alt" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("my_orders") }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item
            v-if="currentUser !== ''"
            v-ripple
            clickable
            @click="goPage('/user-info')"
          >
            <q-item-section avatar>
              <q-icon color="grey" name="manage_accounts" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("edit_profile") }}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item
            v-if="currentUser !== ''"
            v-ripple
            clickable
            @click="logout()"
          >
            <q-item-section avatar>
              <q-icon color="grey" name="logout" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ $t("logout") }}</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view />
      <q-footer bordered class="bg-blue-6 text-white">
        <div class="q-pa-md">
          <div class="row justify-center">
            <div class="col-12 col-md-3 text-white q-pl-xl q-pt-md">
              <div class="text-subtitle1 text-weight-bold">
                About HiChinaTravel
              </div>
              <div class="text-body2 hover_underline_white q-mt-md">
                About Us
              </div>
              <div class="text-body2 hover_underline_white q-mt-md">
                Contact us
              </div>
              <div class="text-body2 hover_underline_white q-mt-md">
                Copyright
              </div>
              <div
                class="text-body2 hover_underline_white q-mt-md cursor-pointer"
                @click="goPage('/privacy')"
              >
                Privacy
              </div>
              <div class="text-body2 hover_underline_white q-mt-md">
                Join Us
              </div>
            </div>
            <div class="col-12 col-md-5 q-pt-xl">
              <div
                class="text-subtitle1 text-weight-medium hover_underline_white q-mt-md"
              >
                Facebook: HiChinaTravel
              </div>
              <div
                class="text-subtitle1 text-weight-medium hover_underline_white q-mt-md"
              >
                WeChat Official Account: HCTravel
              </div>
              <div
                class="text-subtitle1 text-weight-medium hover_underline_white q-mt-md"
              >
                Instagram: HiChinaTravel
              </div>
              <div
                class="text-subtitle1 text-weight-medium hover_underline_white q-mt-md"
              >
                Support: customerservice@hichinatrip.com
              </div>
              <div
                class="text-subtitle1 text-weight-medium hover_underline_white q-mt-md"
              >
                Address: 4,Zhixin Rd. Qixia District Nanjing City, Jiangsu
                Province, P.R China
              </div>
            </div>
            <div class="col-12 col-md-4"></div>
          </div>
          <div class="row justify-center q-mt-xl">
            <div class="text-body2 text-weight-medium hover_underline_white">
              COPYRIGHT © 2015-2023 WWW.HICHINATRAVEL.COM, ALL RIGHTS RESERVED
            </div>
          </div>
          <div class="row justify-center">
            <div class="text-body2 text-weight-medium hover_underline_white">
              备案号： 京ICP备16006305号-1
            </div>
          </div>
        </div>
      </q-footer>
    </q-page-container>
  </q-layout>
</template>

<script>
import { defineComponent, onMounted, ref, getCurrentInstance } from "vue";
import { api } from "boot/axios";
import { useI18n } from "vue-i18n";
import { useQuasar } from "quasar";

export default defineComponent({
  name: "MainLayout",

  components: {},

  setup() {
    const { locale } = useI18n({ useScope: "global" });

    const leftDrawerOpen = ref(false);
    const currentUser = ref("");
    const currentProfileImage = ref("");

    const instance = getCurrentInstance();
    const app = instance.appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    function setLanguage() {
      // $q.lang.set(locale);
      console.log(locale);
    }

    function whoami() {
      api
        .get("/api/v1/user/whoamiv2")
        .then(function (response) {
          console.log("current user:" + response.data.data.username);
          console.log(
            "current user profile image:" + response.data.data.profileImageUrl
          );
          currentUser.value = response.data.data.username;
          currentProfileImage.value = response.data.data.profileImageUrl;
          if (
            currentProfileImage.value == null ||
            currentProfileImage.value.length < 1
          ) {
            currentProfileImage.value =
              "https://photoprism.hichinatravel.com/api/v1/t/8623903789c65a160279faa0b33159413cb18af4/32mcf2k4/fit_2048";
          }
          console.log("currentProfileImage.value");
          console.log(currentProfileImage.value);
        })
        .catch(function (error) {
          console.log("not logged in err");
          // router.push({name: 'home'})
        });
    }
    function logout() {
      console.log("logging out...");
      api
        .post(
          "/logout",
          {},
          { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
        )
        .then((response) => {
          location.reload();
        })
        .catch((e) => {
          location.reload();
        });
    }
    onMounted(() => {
      whoami();
    });
    return {
      setLanguage,
      locale,
      localeOptions: [
        { value: "en-US", label: "English" },
        { value: "th-TH", label: "ภาษาไทย" },
        { value: "ko-KR", label: "한국인" },
        { value: "ru-RU", label: "Русский" },
      ],
      leftDrawerOpen,
      currentUser,
      currentProfileImage,
      menu_profile: false,
      logout,
      toggleLeftDrawer() {
        leftDrawerOpen.value = !leftDrawerOpen.value;
      },
    };
  },
});
</script>
<style lang="sass">
.GPL

  &__toolbar
    height: 64px

  &__toolbar-input
    width: 35%

  &__drawer-item
    line-height: 24px
    border-radius: 0 24px 24px 0
    margin-right: 12px

    .q-item__section--avatar
      padding-left: 12px
      .q-icon
        color: #5f6368

    .q-item__label:not(.q-item__label--caption)
      color: #3c4043
      letter-spacing: .01785714em
      font-size: .875rem
      font-weight: 500
      line-height: 1.25rem

    &--storage
      border-radius: 0
      margin-right: 0
      padding-top: 24px
      padding-bottom: 24px

  &__side-btn
    &__label
      font-size: 12px
      line-height: 24px
      letter-spacing: .01785714em
      font-weight: 500

  @media (min-width: 1024px)
    &__page-container
      padding-left: 94px
</style>
