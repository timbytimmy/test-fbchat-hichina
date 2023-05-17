<template>
    <div>
      <p class="time"> Created on: {{ blogSummary.createdTime }}</p>
      <div class="content flex-between mt-6">
        <p class="title" @click="goPage('/blog-detail/'+blogSummary.blogId)"><a href="">{{ blogSummary.title }}</a></p>
        <p class="status">{{ blogSummary.draft?'DRAFTED':'PUBLISHED' }}</p>
        <div class="detail">
          <el-button @click="goBlogDetail(blogSummary.blogId)">Blog Details</el-button>
        </div>
        <div class="detail">
          <el-button v-if="blogSummary.draft" @click="publishBlog(blogSummary.blogId)">Publish</el-button>
        </div>
        <div>
          <el-button type="danger" :icon="Delete" @click="deleteBLog(blogSummary.blogId)" circle />
        </div>
        <div>
          <el-button type="warning" :icon="Edit" @click="editBLog(blogSummary.blogId)" circle />
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
    import {
      Check,
      Delete,
      Edit,
      Message,
      Search,
      Star,
    } from '@element-plus/icons-vue'
    import {AXIOS} from '@/common/http-commons'
    import router from "../../../router";
    const props = defineProps({
      blogSummary: {
        type: Object
      }
    })

    function goPage(page){
      router.push({path: page})
    }

    const emit = defineEmits(['refreshBlogList'])
  
    function editBLog(blogId){
      router.push({path: '/blog-edit/'+blogId})
    }
    function deleteBLog(blogId){
      AXIOS.delete("/api/v1/blog/"+blogId).then(res => {
          ElNotification({
            title: 'Suceed',
            message: 'Succeed deleting blog',
            type: 'success',
          })
          emit("refreshBlogList");
        })
        .catch(err => {
          ElNotification({
          title: 'Fail',
          message: 'Fail deleting blog'+err,
          type: 'error',
        })
      })
    }

    function publishBlog(blogId){
      AXIOS.put('/api/v1/blog/publish/'+blogId).then(res => {
          ElNotification({
            title: 'Suceed',
            message: 'Succeed publishing blog',
            type: 'success',
          })
          emit("refreshBlogList");
        })
        .catch(err => {
          ElNotification({
          title: 'Fail',
          message: 'Fail publishing blog'+err,
          type: 'error',
        })
      })
    }

    function goBlogDetail(blogId){
      console.log("blog detail coming...")
      console.log("calling detail of : "+ blogId)
      router.push({path: '/blog-detail/'+blogId})
    }
  </script>
  
  <style scoped lang="scss">
    .item {
      margin-bottom: 12px;
    }
  
    .time {
      height: 30px;
      // padding-left: 12px;
      // background-color: rgba(214, 214, 214, 1);
      font-size: 14px;
      line-height: 30px;
      text-align: left;
    }
  
    .content {
      padding: 18px 46px 18px 15px;
      border: rgba(184, 181, 181, 1) solid 1px;
      // color: rgba(80, 80, 80, 1);
      font-size: 14px;
    }
  
    .title {
      cursor: pointer;
      min-width: 225px;
      max-width: 225px;
      font-size: 20px !important;;
    }
  
    .detail {
      flex: 0 0 120px;
      height: 31px;
  
      button {
        width: 100%;
        // color: rgba(255, 253, 253, 1);
        background-color: rgba(255, 141, 26, 1);
        border-radius: 7px;
        font-size: 14px;
        text-align: center;
      }
    }
  </style>