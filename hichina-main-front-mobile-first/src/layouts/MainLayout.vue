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
          GuideBooks
        </q-btn>
        <q-btn
          @click="goPage('/blog')"
          flat
          no-caps
          no-wrap
          class="q-ml-sm"
          v-if="$q.screen.gt.xs"
        >
          Blogs/Vlogs
        </q-btn>
        <q-btn
          @click="goPage('/product')"
          flat
          no-caps
          no-wrap
          class="q-ml-sm"
          v-if="$q.screen.gt.xs"
        >
          Travel Shop
        </q-btn>
        <q-btn
          @click="goPage('/destination')"
          flat
          no-caps
          no-wrap
          class="q-ml-sm"
          v-if="$q.screen.gt.xs"
        >
          Destinations
        </q-btn>

        <!-- this is the trick -->
        <q-space />

        <div class="row no-wrap">
          <q-btn
            flat
            dense
            no-wrap
            no-caps
            color="primary"
            label="Register"
            class="q-mr-lg"
            v-if="$q.screen.gt.xs"
          />
          <q-btn
            @click="goPage('/auth/login')"
            flat
            dense
            no-wrap
            no-caps
            color="primary"
            label="Login"
            class="q-mr-sm"
            v-if="$q.screen.gt.xs"
          />
        </div>
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
          <q-item v-ripple clickable>
            <q-item-section avatar>
              <q-icon color="grey" name="fingerprint" />
            </q-item-section>
            <q-item-section>
              <q-item-label>GuideBooks</q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-ripple clickable>
            <q-item-section avatar>
              <q-icon color="grey" name="fingerprint" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Blogs/Vlogs</q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-ripple clickable>
            <q-item-section avatar>
              <q-icon color="grey" name="fingerprint" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Travel Shop</q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-ripple clickable>
            <q-item-section avatar>
              <q-icon color="grey" name="fingerprint" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Destinations</q-item-label>
            </q-item-section>
          </q-item>

          <q-separator class="q-my-md" />

          <q-item v-ripple clickable>
            <q-item-section avatar>
              <q-icon color="grey" name="login" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Login</q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-ripple clickable>
            <q-item-section avatar>
              <q-icon color="grey" name="account_circle" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Register</q-item-label>
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
              <div class="text-body2 hover_underline_white q-mt-md">
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
import { defineComponent, ref } from "vue";

const linksList = [
  {
    title: "Docs",
    caption: "quasar.dev",
    icon: "school",
    link: "https://quasar.dev",
  },
  {
    title: "Github",
    caption: "github.com/quasarframework",
    icon: "code",
    link: "https://github.com/quasarframework",
  },
  {
    title: "Discord Chat Channel",
    caption: "chat.quasar.dev",
    icon: "chat",
    link: "https://chat.quasar.dev",
  },
  {
    title: "Forum",
    caption: "forum.quasar.dev",
    icon: "record_voice_over",
    link: "https://forum.quasar.dev",
  },
  {
    title: "Twitter",
    caption: "@quasarframework",
    icon: "rss_feed",
    link: "https://twitter.quasar.dev",
  },
  {
    title: "Facebook",
    caption: "@QuasarFramework",
    icon: "public",
    link: "https://facebook.quasar.dev",
  },
  {
    title: "Quasar Awesome",
    caption: "Community Quasar projects",
    icon: "favorite",
    link: "https://awesome.quasar.dev",
  },
];

export default defineComponent({
  name: "MainLayout",

  components: {},

  setup() {
    const leftDrawerOpen = ref(false);

    return {
      essentialLinks: linksList,
      leftDrawerOpen,
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
