<template>
  <div class="notification">
    <p class="notification-text">Please go to your mailbox to activate your account</p>
  </div>
</template>
  
  <script setup>
import router from "../../router";

const waitAndGo = async () => {
  setTimeout(() => {
    router.push({ name: "home" });
  }, 8000);
};

onMounted(() => {
  waitAndGo();
});
</script>
  
  <style scoped lang="scss">
.header {
  height: 374px;
  padding-top: 116px;
  color: rgba(80, 80, 80, 1);
  background-color: rgba(42, 130, 228, 1);
}

.intro {
  position: relative;
  margin: 0 auto;
  width: 747px;
  height: 172px;
  padding: 45px 25px 24px;
  color: rgba(80, 80, 80, 1);
  background-color: rgba(255, 255, 255, 1);
  border-radius: 10px;
  font-size: 14px;
  border: rgba(169, 169, 169, 1) solid 1px;
  box-shadow: 0px 3px 3px 0px rgba(0, 0, 0, 0.5);

  &.transparent {
    background: transparent;
    box-shadow: none;
    border: none;
  }
}

.part {
  position: absolute;
  width: 656px;
  height: 92px;
  padding: 0 22px;
  left: 45px;
  top: -50px;
  background-color: rgba(255, 255, 255, 1);
  border-radius: 16px;
  box-shadow: 0 5px 5px 0 rgba(0, 0, 0, 0.5);
  text-align: center;
  cursor: pointer;
  transition: 0.3s;

  &.down {
    top: 50px;
  }

  li {
    width: 20%;
    height: 100%;
    padding: 22px 0 15px;
    color: rgba(42, 130, 228, 1);
    font-size: 14px;

    &.active {
      color: white;
      background: rgba(42, 130, 228, 1);
    }
  }
}

.info {
  text-align: center;
  align-items: stretch;
  li {
    width: calc(100% / 3);
  }

  .title {
    margin-top: 21px;
    color: rgba(80, 80, 80, 1);
    font-size: 23px;
    font-weight: bold;
  }

  .sub {
    color: rgba(79, 79, 79, 1);
    font-size: 14px;
  }

  .el-icon {
    margin-left: 8px;
  }
}

.search {
  margin: 22px auto 0;
  width: 257px;
  height: 48px;
  color: rgba(42, 130, 228, 1);
  background-color: rgba(250, 250, 250, 1);
  box-shadow: rgba(42, 130, 228, 1) solid 1px;
  border-radius: 8px;
  font-size: 19px;
  line-height: 48px;
  border: rgba(250, 250, 250, 1) solid 1px;
  text-align: center;
}

.wrapper {
  padding-top: 102px;
}

.title {
  position: relative;
  color: rgba(42, 130, 228, 1);
  font-size: 32px;
  text-align: center;

  a {
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
    font-size: 20px;
  }
}

.list {
  flex-wrap: wrap;

  &-item {
    margin-right: 47px;
    margin-bottom: 20px;
  }
}

.notification {
    background-color: #f8f8f8;
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 10px;
    text-align: center;
  }

  .notification-text {
    font-weight: bold;
    font-size: 3em;
    color: rgb(241, 78, 78);
  }
</style>