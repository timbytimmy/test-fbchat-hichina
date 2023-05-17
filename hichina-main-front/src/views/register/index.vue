<template>
    <div class="form-center" style="width: 500px;">
        <Register />
    </div>
</template>
  
<script setup>
   import Register from "@/components/Register";
 
</script>
  
<style scoped lang="scss">
    .custom-container {
        display: flex;
        justify-content:center;
        align-items: left;
        height: 50%;
    }
    .form-center {
          margin: auto;
          position: absolute;
          top: 40%;
          left: 30%;
          -ms-transform: translateY(-50%);
          transform: translateY(-50%);
    }
</style>