<template>
  <q-page>
    <q-dialog v-model="confirmDelete" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm"
            >Are you sure you would like to delete this blog?.</span
          >
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancel" color="primary" v-close-popup />
          <q-btn
            flat
            label="Confirm Delete"
            @click="deleteBLog"
            color="primary"
            v-close-popup
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <div class="row justify-center">
      <div class="col-10 row">
        <div class="col-12 text-h5 q-pa-md text-weight-bold text-center">
          My Blog List
        </div>
        <div
          class="q-mt-xl row col-12"
          v-for="(item, index) in bloglist"
          :key="index"
        >
          Created: {{ item.createdTime }}
          <div class="col-12 row" style="border: 1px solid gray">
            <div class="col-6 text-h6" style="border: 1px solid gray">
              <a :href="'./blog-detail/' + item.blogId">{{ item.title }}</a>
            </div>
            <div class="col-1" style="border: 1px solid gray; min-width: 70px">
              {{ item.draft ? "Draft" : "Published" }}
            </div>
            <div
              class="col-5 row no-wrap justify-center"
              style="border: 1px solid gray"
            >
              <q-btn
                v-if="item.draft"
                rounded
                dense
                color="primary"
                label="Publish"
                icon="publish"
                @click="publishBlog(item.blogId)"
              />
              <q-btn
                rounded
                dense
                color="primary"
                icon="edit"
                @click="goPage('/blog-edit/' + item.blogId)"
              />
              <q-btn
                rounded
                dense
                color="red"
                icon="delete"
                @click="confirmDeleteBlog(item.blogId)"
              />
            </div>
          </div>
        </div>
        <div class="q-pa-lg flex flex-center">
          <q-pagination v-model="currentPage" :max="totalPages" />
        </div>
      </div></div
  ></q-page>
</template>

<script>
import { ref, onMounted, getCurrentInstance } from "vue";
import { useQuasar } from "quasar";
import { api } from "boot/axios";
export default {
  name: "MyBlogsPage",
  setup() {
    const instance = getCurrentInstance();
    const app = getCurrentInstance().appContext.app;
    const gp = app.config.globalProperties;
    const $q = useQuasar();

    const pageSize = ref(100);
    const blogCurrentPage = ref(1);
    const bloglist = ref([]);
    const totalCnt = ref(0);
    const totalPages = ref(1);
    const currentPage = ref(1);
    const confirmDelete = ref(false);
    const todeleteBlogID = ref("");

    function publishBlog(blogId) {
      api
        .put("/api/v1/blog/publish/" + blogId)
        .then((res) => {
          gp.$generalNotify($q, true, "Succeed publishing blog");
          loadMyBlogs();
        })
        .catch((err) => {
          gp.$generalNotify($q, false, "Fail publishing blog" + err);
        });
    }

    function confirmDeleteBlog(blogId) {
      confirmDelete.value = true;
      todeleteBlogID.value = blogId;
    }

    function deleteBLog() {
      api
        .delete("/api/v1/blog/" + todeleteBlogID.value)
        .then((res) => {
          gp.$generalNotify($q, true, "Succeed deleting blog");
          loadMyBlogs();
        })
        .catch((err) => {
          gp.$generalNotify($q, false, "Fail deleting blog" + err);
        });
    }

    function loadMyBlogs() {
      var params = {};
      params.pageSize = pageSize.value;
      params.page = blogCurrentPage.value;
      (params.query = ""),
        api
          .get("/api/v1/blog/mybloglist", {
            params: params,
          })
          .then(function (response) {
            console.log("Got my blogs summary: ");
            console.log(response.data.data);
            bloglist.value = response.data.data.data;
            totalCnt.value = response.data.data.total;
            pageSize.value = response.data.data.pageSize;
            blogCurrentPage.value = response.data.data.currentPage;

            let divFloor = Math.floor(totalCnt.value / pageSize.value);
            if (totalCnt.value % pageSize.value > 0) {
              divFloor += 1;
            }
            totalPages.value = divFloor;
          })
          .catch(function (error) {
            console.log("failed to load my blogs: " + error);
          });
    }

    function whoami() {
      api
        .get("/api/v1/user/whoami")
        .then(function (response) {
          console.log("current user in user page: " + response.data);
        })
        .catch(function (error) {
          console.log("currently not logged in setup: " + error);
          gp.$goPage("/auth/login");
        });
    }

    onMounted(() => {
      whoami();
      loadMyBlogs();
    });

    return {
      pageSize,
      bloglist,
      totalCnt,
      currentPage,
      totalPages,
      confirmDelete,
      todeleteBlogID,
      deleteBLog,
      publishBlog,
      confirmDeleteBlog,
    };
  },
};
</script>
