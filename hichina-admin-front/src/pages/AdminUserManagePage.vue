<template>
  <q-page padding>
    <q-dialog v-model="confirmDelete" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="signal_wifi_off" color="primary" text-color="white" />
          <span class="q-ml-sm">确定要删除所选吗？</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn
            flat
            label="确认删除"
            color="primary"
            @click="executeDelete"
            v-close-popup
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog
      v-model="popUpCreateAdminUser"
      transition-show="rotate"
      transition-hide="rotate"
    >
      <q-card>
        <q-card-section>
          <div class="text-h6">创建后台管理用户</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-input outlined v-model="form.username" label="Username" />
          <q-input
            outlined
            v-model="form.password"
            type="password"
            label="Password"
          />
          <q-select
            outlined
            v-model="form.role"
            :options="roleOptions"
            label="Role"
          />
        </q-card-section>

        <q-card-actions align="right">
          <q-btn
            flat
            label="Submit"
            @click="doCreateNewAdmin"
            color="primary"
            v-close-popup
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-table
      ref="adminUserTableRef"
      style="max-width: 98%; min-width: 90%"
      title="管理员用户管理"
      :rows="adminUserlist"
      :columns="columns"
      :loading="loading"
      :filter="filter"
      row-key="username"
      :separator="'cell'"
      @request="onRequest"
      selection="multiple"
      v-model:selected="selected"
    >
      <template v-slot:top-right>
        <q-btn
          color="deep-orange"
          glossy
          @click="newAdminUser"
          label="新建用户"
          style="margin-right: 50px"
        />
        <q-btn
          style="margin-right: 50px"
          round
          color="primary"
          @click="goDelete"
          icon="delete"
        />
      </template>
      <template v-slot:body-cell-role="props">
        <q-td :props="props">
          {{
            props.row.username == "hichinaadmin"
              ? "超级管理员"
              : props.row.username.startsWith("SP_")
              ? "供应商"
              : props.row.username.startsWith("EMP_")
              ? "员工"
              : "未知"
          }}
        </q-td>
      </template>
    </q-table>
  </q-page>
</template>

<script>
import { api } from "boot/axios";
import { useQuasar } from "quasar";
export default {
  name: "AdminUserManagePage",
  setup() {
    const $q = useQuasar();

    return {
      showNotifyMessageFail(msg) {
        $q.notify({
          message: msg,
          color: "red",
          position: "top-right",
        });
      },
      showNotifyMessageSucceed(msg) {
        $q.notify({
          message: msg,
          color: "green",
          position: "top-right",
        });
      },
      showFullPageLoading() {
        this.disableAction = true;
        $q.loading.show();
      },
      hideFullPageLoading() {
        $q.loading.hide();
        this.disableAction = false;
      },
    };
  },
  data() {
    return {
      confirmDelete: false,
      roleOptions: [
        { label: "员工", value: 1 },
        { label: "供应商", value: 2 },
      ],
      form: {
        username: "",
        password: "",
        role: "",
      },
      popUpCreateAdminUser: false,
      selected: [],
      filter: "",
      loading: false,
      adminUserlist: [],
      columns: [
        {
          name: "username",
          required: true,
          label: "用户名",
          align: "left",
          field: "username",
          sortable: false,
          style:
            "max-width: 170px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "password",
          required: true,
          label: "密码",
          align: "left",
          field: "password",
          sortable: false,
          style:
            "max-width: 170px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        {
          name: "createdTime",
          required: true,
          label: "创建时间",
          align: "left",
          field: "createdTime",
          sortable: false,
          style:
            "max-width: 170px;text-overflow: ellipsis !important;white-space: nowrap !important;overflow: hidden !important;",
        },
        { name: "role", label: "角色", field: "role" },
      ],
    };
  },
  methods: {
    executeDelete() {
      console.log(this.selected);
      var listOfId2Del = [];
      for (var i in this.selected) {
        listOfId2Del.push(this.selected[i].username);
      }
      if (listOfId2Del.indexOf("hichinaadmin") != -1) {
        alert("不能删除超级管理员");
        return;
      }
      var params = {};
      params.toDelete = listOfId2Del;
      api
        .delete("/api/v1/adminuser/batch", { data: params })
        .then((response) => {
          if (response.data.ok == true) {
            this.showNotifyMessageSucceed(response.data.message);
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
          this.$refs.adminUserTableRef.requestServerInteraction();
          this.selected = [];
        })
        .catch((e) => {
          storeThis.showNotifyMessageFail(e.toString());
        });
    },
    goDelete() {
      this.confirmDelete = true;
    },
    checkNull(val) {
      console.log("checking ...");
      console.log(val);
      if (val == null || val.length < 1) {
        return true;
      } else {
        return false;
      }
    },
    doCreateNewAdmin() {
      if (this.checkNull(this.form.username)) {
        alert("Usernmae cannot be empty");
        return;
      }
      if (this.checkNull(this.form.password)) {
        alert("Password cannot be empty");
        return;
      }
      if (this.form.role == "" || this.checkNull(this.form.role.value)) {
        alert("Role cannot be empty");
        return;
      }
      var params = {};
      params.username = this.form.username;
      params.password = this.form.password;
      params.role = this.form.role.value;
      api
        .post("/api/v1/adminuser", params)
        .then((response) => {
          console.log(response.data);
          if (response.data.ok == true) {
            this.showNotifyMessageSucceed(response.data.message);
            this.popUpCreateAdminUser = false;
            this.$refs.adminUserTableRef.requestServerInteraction();
          } else {
            this.showNotifyMessageFail(response.data.message);
          }
        })
        .catch((e) => {
          this.showNotifyMessageFail(e.toString());
        });
    },
    newAdminUser() {
      this.popUpCreateAdminUser = true;
    },
    onRequest() {
      this.loadAllAdminUsers();
    },
    loadAllAdminUsers() {
      api
        .get("/api/v1/adminuser/listalladminuser")
        .then((response) => {
          console.log(response.data);
          this.adminUserlist = response.data.data;
        })
        .catch((e) => {
          console.log(e);
          this.showNotifyMessageFail(e.response);
        });
    },
  },
  mounted() {
    this.$refs.adminUserTableRef.requestServerInteraction();
    // this.loadAllAdminUsers();
  },
};
</script>
