<template>
  <q-layout view="hHh Lpr fFf"> <!-- Be sure to play with the Layout demo on docs -->
    <q-header elevated>
        <q-toolbar>
          <q-btn
            flat
            dense
            round
            icon="menu"
            aria-label="Menu"
          />
          <q-toolbar-title>
            <q-btn flat label="Hichina管理后台" @click="goPage('/')" />
          </q-toolbar-title>
        </q-toolbar>
    </q-header>

    <q-page-container>
      <!-- This is where pages get injected -->
      <router-view />
    </q-page-container>

  </q-layout>
</template>
<script>
import { myMixin } from 'boot/commonFunc'
export default {
    name: "BlankLayout",
    mixins: [myMixin]
};
</script>