<template>
  <ul class="x-tab flex-start">
    <li v-for="(item, index) in list" :key="item.id" :class="{active: active === index}" @click="activate(index)">{{item.title}}</li>
  </ul>
</template>

<script setup>
  const active = ref(0)
  const props = defineProps({
    list: []
  })
  const emit = defineEmits(['updateActiveTab'])

  function activate(index){
    active.value = index
    emit("updateActiveTab", index)
  }

  onMounted(() => {
      console.log("what I got from book form:")
      // console.log(list.value)
  })
</script>

<style scoped lang="scss">
  .x-tab {
    border-bottom: 1px solid #e0e0e0;

    li {
      width: 369px;
      height: 54px;
      color: rgba(80, 80, 80, 1);
      font-size: 19px;
      text-align: center;
      line-height: 54px;
      cursor: pointer;

      &.active {
        color: rgba(255, 255, 255, 1);
        background-color: rgba(42, 130, 228, 1);
        border-radius: 6px;
        font-size: 21px;
      }
    }
  }
</style>