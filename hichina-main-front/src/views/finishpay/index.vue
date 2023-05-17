<template>
    
    Redirecting...  <vue-json-pretty :data="route.query" />
     
</template>
  <script setup>
    import router from "../../router";
    import {AXIOS} from '@/common/http-commons'
    import { ElNotification } from 'element-plus'  
    const route = useRoute();
    onMounted(() => {
        if(route.query.out_trade_no!=null && route.query.out_trade_no.length>0){
            var params = {}
            params.tradeNo = route.query.trade_no
            params.method = route.query.method
            AXIOS.put('/api/v1/order/succeed/'+route.query.out_trade_no, params).then(res => {
                ElNotification({
                  title: 'Suceed',
                  message: 'Succeed finishing pay',
                  type: 'success',
                })
                var params2pass = {}
                params2pass.active = 2;
                router.push({name: 'user', params: params2pass})
              })
              .catch(err => {
                ElNotification({
                title: 'Fail',
                message: 'Fail creating order'+err,
                type: 'error',
              })
            })
        }
    })
  
  
  </script>
  
  
  <style scoped lang="scss">
  
  </style>
  