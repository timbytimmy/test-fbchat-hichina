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

        <div v-if="currentUser === ''" class="row no-wrap">
          <q-btn
            @click="goPage('/auth/register')"
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
        <div v-if="currentUser != ''" class="row">
          <q-btn v-if="$q.screen.gt.xs" round flat class="q-mr-md">
            <q-avatar size="36px">
              <img :src="currentProfileImage" />
            </q-avatar>
            <q-tooltip>{{ currentUser }}</q-tooltip>
          </q-btn>
        </div>
        <div v-if="currentUser != ''" class="row no-wrap">
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

          <q-item v-if="currentUser === ''" v-ripple clickable>
            <q-item-section avatar>
              <q-icon color="grey" name="login" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Login</q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-if="currentUser === ''" v-ripple clickable>
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
import { defineComponent, onMounted, ref } from "vue";
import { api } from "boot/axios";

export default defineComponent({
  name: "MainLayout",

  components: {},

  setup() {
    const leftDrawerOpen = ref(false);
    const currentUser = ref("");
    const currentProfileImage = ref("");

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
      leftDrawerOpen,
      currentUser,
      currentProfileImage,
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
