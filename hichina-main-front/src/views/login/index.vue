<template>
    <div class="form-center" style="width: 500px;">
        <Login />
    </div>
</template>
  
<script setup>
   import Login from "@/components/Login";
</script>
  
<style scoped lang="scss">
    .form-center {
          margin: 0 auto;
          margin-top: 80px;
        //   position: absolute;
        //   top: 40%;
        //   left: 30%;
        //   -ms-transform: translateY(-50%);
        //   transform: translateY(-50%);
    }
</style>