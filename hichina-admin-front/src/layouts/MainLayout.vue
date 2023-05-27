<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="toggleLeftDrawer"
        />

        <q-toolbar-title>
          <q-btn flat label="Hichina管理后台" @click="goPage('/')" />
        </q-toolbar-title>

        <q-btn-dropdown flat :label="user">
          <q-list link>
            <q-item clickable v-if="loggedIn" @click="logout">
              <!-- <q-item-side icon="assignment" inverted color="primary" /> -->
              <q-item-main>
                <q-item-tile>登出</q-item-tile>
                <!-- <q-item-tile sublabel>February 22, 2016</q-item-tile> -->
              </q-item-main>
              <q-item-side right icon="info" color="amber" />
            </q-item>
            <q-item clickable v-if="!loggedIn" @click="goPage('/auth/login')">
              <!-- <q-item-side icon="assignment" inverted color="primary" /> -->
              <q-item-main>
                <q-item-tile label>登录</q-item-tile>
                <!-- <q-item-tile sublabel>February 22, 2016</q-item-tile> -->
              </q-item-main>
              <q-item-side right icon="info" color="amber" />
            </q-item>
          </q-list>
        </q-btn-dropdown>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <q-list>
        <q-item-label header> 功能菜单 </q-item-label>
        <q-expansion-item
          default-opened
          icon="inventory_2"
          label="产品管理"
          :content-inset-level="1"
        >
          <q-item
            clickable
            target="_blank"
            v-if="getMyRole() == 'SUPERADMIN'"
            @click="goPage('/product-shared-attributes')"
          >
            <q-item-section avatar>
              <q-icon :name="'warehouse'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>产品属性库管理</q-item-label>
            </q-item-section>
          </q-item>
          <q-item
            clickable
            target="_blank"
            @click="goPage('/product-type')"
            v-if="getMyRole() == 'SUPERADMIN'"
          >
            <q-item-section avatar>
              <q-icon :name="'category'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>产品类别模板</q-item-label>
            </q-item-section>
          </q-item>
          <q-item clickable target="_blank" @click="goPage('/product-sku')">
            <q-item-section avatar>
              <q-icon :name="'production_quantity_limits'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>产品细项</q-item-label>
            </q-item-section>
          </q-item>
          <q-item
            clickable
            target="_blank"
            @click="goPage('/product-sku-group')"
          >
            <q-item-section avatar>
              <q-icon :name="'group'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>产品管理</q-item-label>
            </q-item-section>
          </q-item>
          <q-item
            clickable
            target="_blank"
            @click="goPage('/order')"
            v-if="getMyRole() == 'SUPERADMIN' || getMyRole() == 'EMPLOYEE'"
          >
            <q-item-section avatar>
              <q-icon :name="'list_alt'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>订单管理</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-expansion-item
          default-opened
          icon="chalet"
          label="目的地管理"
          v-if="getMyRole() == 'SUPERADMIN' || getMyRole() == 'EMPLOYEE'"
          :content-inset-level="1"
        >
          <q-item clickable target="_blank" @click="goPage('/destination')">
            <q-item-section avatar>
              <q-icon :name="'add_location'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>目的地</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-expansion-item
          default-opened
          icon="book"
          label="博文管理"
          v-if="getMyRole() == 'SUPERADMIN' || getMyRole() == 'EMPLOYEE'"
          :content-inset-level="1"
        >
          <q-item clickable target="_blank" @click="goPage('/blogmanage')">
            <q-item-section avatar>
              <q-icon :name="'book'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>博客</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-expansion-item
          default-opened
          icon="auto_stories"
          label="攻略书管理"
          v-if="getMyRole() == 'SUPERADMIN' || getMyRole() == 'EMPLOYEE'"
          :content-inset-level="1"
        >
          <q-item clickable target="_blank" @click="goPage('/guidebook')">
            <q-item-section avatar>
              <q-icon :name="'note'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>攻略书</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-expansion-item
          default-opened
          icon="web"
          label="主站页面数据管理"
          v-if="getMyRole() == 'SUPERADMIN' || getMyRole() == 'EMPLOYEE'"
          :content-inset-level="1"
        >
          <q-item
            clickable
            target="_blank"
            @click="goPage('/home-slide-image')"
          >
            <q-item-section avatar>
              <q-icon :name="'image'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>首页轮播图</q-item-label>
            </q-item-section>
          </q-item>
          <q-item clickable target="_blank" @click="goPage('/guidebook-intro')">
            <q-item-section avatar>
              <q-icon :name="'image'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>攻略页轮播图</q-item-label>
            </q-item-section>
          </q-item>
          <q-item clickable target="_blank" @click="goPage('/home-post')">
            <q-item-section avatar>
              <q-icon :name="'image'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>首页海报位</q-item-label>
            </q-item-section>
          </q-item>
          <q-item clickable target="_blank" @click="goPage('/bloghome')">
            <q-item-section avatar>
              <q-icon :name="'image'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>博客页轮播图</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-expansion-item
          default-opened
          icon="account_circle"
          v-if="getMyRole() == 'SUPERADMIN'"
          label="用户管理"
          :content-inset-level="1"
        >
          <q-item clickable target="_blank" @click="goPage('/adminuser')">
            <q-item-section avatar>
              <q-icon :name="'badge'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>管理后台用户</q-item-label>
            </q-item-section>
          </q-item>
          <q-item clickable target="_blank" @click="goPage('/user')">
            <q-item-section avatar>
              <q-icon :name="'person'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>网站用户</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
        <q-expansion-item
          v-if="debugging"
          default-opened
          icon="accessibility"
          label="旧用户兼容性测试"
          :content-inset-level="1"
        >
          <q-item clickable target="_blank" @click="goPage('/logintest')">
            <q-item-section avatar>
              <q-icon :name="'quiz'" />
            </q-item-section>
            <q-item-section>
              <q-item-label>email登录测试</q-item-label>
            </q-item-section>
          </q-item>
        </q-expansion-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import { defineComponent, ref } from "vue";
import { api } from "boot/axios";
import { myMixin } from "boot/commonFunc";
import EssentialLink from "components/EssentialLink.vue";

const linksList = [
  {
    title: "线路管理",
    caption: "quasar.dev",
    icon: "school",
    link: "https://quasar.dev",
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
  mixins: [myMixin],
  data() {
    return {
      user: "",
      debugging: false,
      loggedIn: false,
    };
  },

  components: {
    // EssentialLink
  },
  methods: {
    getMyRole() {
      if (this.user.startsWith("SP_")) {
        return "SUPPLIER";
      } else if (this.user.startsWith("EMP_")) {
        return "EMPLOYEE";
      } else if (this.user == "hichinaadmin") {
        return "SUPERADMIN";
      } else {
        return "UNKNOWN";
      }
    },
    logout() {
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
    },
    whoami() {
      var storeThis = this;
      api
        .get("/api/v1/user/info")
        .then(function (response) {
          storeThis.user = response.data;
          storeThis.loggedIn = true;
        })
        .catch(function (error) {
          storeThis.user = "";
          storeThis.loggedIn = false;
          console.log("hide in main layout:");
          storeThis.$router.push("/auth/login");
        });
    },
    regularCheckLogin() {
      var storeThis = this;
      var count = 0;
      setInterval(function () {
        count = count + 1;
        storeThis.whoami();
      }, 15000);
    },
  },
  created: function () {
    this.whoami();
  },
  mounted() {
    this.regularCheckLogin();
  },
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
