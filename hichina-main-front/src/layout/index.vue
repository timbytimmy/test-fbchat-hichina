<template>
  <x-nav/>
  <div class="container">
    <router-view/>
  </div>
  <x-footer/>
</template>

<script setup>
  import XNav from './components/XNav'
  import XFooter from './components/XFooter'
</script>

<style scoped lang="scss">
  .container {
    padding-bottom: 125px;
    min-height: calc(100vh - 403px);
  }
</style>