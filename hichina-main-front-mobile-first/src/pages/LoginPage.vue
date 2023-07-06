<template>
  <q-page>
    <div style="height: 82px"></div>
    <div class="row q-mb-xl justify-center">
      <div
        class="col-10 col-sm-5 col-md-3 rounded-borders login-border shadow-7"
      >
        <div class="text-h5 text-left text-weight-bold text-black q-pa-md">
          Login
        </div>
        <div class="col-12 q-pa-md">
          <q-input color="blue-12" v-model="text" label="Enter your username">
            <template v-slot:prepend>
              <q-icon name="account_circle" />
            </template>
          </q-input>
        </div>
        <div class="col-12 q-pa-md">
          <q-input color="blue-12" v-model="text" label="Enter your password">
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
          </q-input>
        </div>
        <div class="col-12 q-pa-md">
          <q-btn color="primary" label="Login" />
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "LoginPage",
};
</script>
<style lang="sass" scoped>
.login-border
  border-radius: 10px
</style>
