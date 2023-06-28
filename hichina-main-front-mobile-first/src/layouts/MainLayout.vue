<template>
  <q-layout view="hHh lpR fFf">
    <q-header height-hint="76" reveal class="bg-white text-black">
      <q-toolbar class="GPL__toolbar" style="height: 76px">
        <div class="row">
          <div class="col-sm-12 col-xs-12 q-ml-xl q-mr-lg">
            <q-toolbar-title>
              <img
                @click="$router.push('/home')"
                class="cursor-pointer float-left"
                src="~/assets/hichinalogo.png"
              />
            </q-toolbar-title>
          </div>
        </div>
        <q-btn flat no-caps no-wrap class="q-ml-xl" v-if="$q.screen.gt.xs">
          GuideBooks
        </q-btn>
        <q-btn flat no-caps no-wrap class="q-ml-sm" v-if="$q.screen.gt.xs">
          Blogs/Vlogs
        </q-btn>
        <q-btn flat no-caps no-wrap class="q-ml-sm" v-if="$q.screen.gt.xs">
          Travel Shop
        </q-btn>
        <q-btn flat no-caps no-wrap class="q-ml-sm" v-if="$q.screen.gt.xs">
          Destinations
        </q-btn>

        <!-- this is the trick -->
        <q-space />

        <div class="row">
          <q-btn flat dense color="primary" label="Register" class="q-mr-lg" />
          <q-btn flat dense color="primary" label="Login" class="q-mr-sm" />
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view />
    </q-page-container>

    <q-footer reveal bordered class="bg-grey-8 text-white">
      <q-toolbar>
        <q-toolbar-title>
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg" />
          </q-avatar>
          <div>Title</div>
        </q-toolbar-title>
      </q-toolbar>
    </q-footer>
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
