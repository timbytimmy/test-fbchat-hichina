<template>
  <div class="login" v-loading="loading">
    <el-card>
      <h2>Register</h2>
      <el-form
        class="login-form"
        :model="model"
        :rules="state.rules"
        ref="form"
        @submit.native.prevent="register"
      >
        <el-form-item prop="email">
          <el-input
            prefix-icon="user"
            v-model="model.email"
            placeholder="Email"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="model.password"
            placeholder="Password"
            type="password"
            prefix-icon="lock"
          ></el-input>
        </el-form-item>
        <el-form-item prop="repassword">
          <el-input
            v-model="model.repassword"
            placeholder="Confirm password"
            type="password"
            prefix-icon="lock"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            class="login-button"
            type="primary"
            native-type="submit"
            block
            >Register</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
  
  <script setup>
import { AXIOS } from "@/common/http-commons";
import { ElNotification } from "element-plus";
import { reactive, ref } from "vue";
import router from "../router";

const model = ref({
  email: "",
  password: "",
  repassword: "",
});

const loading=ref(false)

const confirmPass = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("Please input the password again"));
  } else if (value !== model.value.password) {
    callback(new Error("Two inputs don't match!"));
  } else {
    callback();
  }
};

function validateEmail(email) {
  var re = /\S+@\S+\.\S+/;
  return re.test(email);
}

const isEmail =  (rule, value, callback) => {
  if (value === "") {
    callback(new Error("Please input the email"));
  } else if (!validateEmail(value)) {
    callback(new Error("Not a valid email address!"));
  } else {
    callback();
  }
};

function validateForm() {
  if (
    model.value.email == null ||
    model.value.email.length < 2 ||
    model.value.email.length > 50
  ) {
    ElNotification({
      title: "Error",
      message: "Email not valid, length should between 2 and 50",
      type: "error",
    });
    return false;
  }
  if (
    model.value.password == null ||
    model.value.password.length < 6 ||
    model.value.password.length > 50
  ) {
    ElNotification({
      title: "Error",
      message: "password not valid, length should between 6 and 50",
      type: "error",
    });
    return false;
  }
  if (model.value.password != model.value.repassword) {
    ElNotification({
      title: "Error",
      message: "Repeat password is not same with origin password",
      type: "error",
    });
    return false;
  }
  return true;
}

const state = reactive({
  rules: {
    email: [
      {
        required: true,
        message: "email should not be empty",
        trigger: "blur",
      },
      {
        min: 4,
        message: "at least 5 characters",
        trigger: "blur",
      },
      { validator: isEmail, trigger: "blur" }
    ],
    password: [
      {
        required: true,
        message: "Password is required",
        trigger: "blur",
      },
      {
        min: 5,
        message: "Password length should be at least 5 characters",
        trigger: "blur",
      },
    ],
    repassword: [{ validator: confirmPass, trigger: "blur" }],
  },
});

function register() {
  loading.value = true;
  if (!validateForm()) {
    return;
  }
  var data = {
    email: model.value.email,
    password: model.value.password,
  };

  AXIOS.post("/api/public/register", data).then((response) => {
      console.log("==after register");
      console.log(response.data);
      if(response.data.ok==true){
        ElNotification({
          title: "Success",
          message: "Succeed sending registering info",
          type: "Success",
        });
      }else{
        ElNotification({
          title: "Error",
          message: response.data.message,
          type: "Error",
        });
      }
      loading.value = false;
      
      router.push({ name: "regsuccess" });
    })
    .catch((e) => {
      ElNotification({
        title: "Error",
        message: "Error message: " + e,
        type: "error",
      });
    });
}
</script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.login {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-button {
  width: 100%;
  margin-top: 40px;
}
.login-form {
  width: 290px;
}
.forgot-password {
  margin-top: 10px;
}
</style>
  <style lang="scss">
$teal: rgb(109, 86, 228);
.el-button--primary {
  background: $teal;
  border-color: $teal;

  &:hover,
  &.active,
  &:focus {
    background: lighten($teal, 7);
    border-color: lighten($teal, 7);
  }
}
.login .el-input__inner:hover {
  border-color: $teal;
}
.login .el-input__prefix {
  background: rgb(238, 237, 234);
  left: 0;
  height: calc(100% - 2px);
  left: 1px;
  top: 1px;
  border-radius: 3px;
  .el-input__icon {
    width: 30px;
  }
}
.login .el-input input {
  padding-left: 35px;
}
.login .el-card {
  padding-top: 0;
  padding-bottom: 30px;
}
h2 {
  font-family: "Open Sans";
  letter-spacing: 1px;
  font-family: Roboto, sans-serif;
  padding-bottom: 20px;
}
a {
  color: $teal;
  text-decoration: none;
  &:hover,
  &:active,
  &:focus {
    color: lighten($teal, 7);
  }
}
.login .el-card {
  width: 340px;
  display: flex;
  justify-content: center;
}
</style>
  