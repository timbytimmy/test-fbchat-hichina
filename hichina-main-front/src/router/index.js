import { createWebHistory, createRouter } from 'vue-router'

const constantRoutes = [
  {
    path: '/',
    component: () => import('@/layout/index'),
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: 'home',
        component: () => import('@/views/home')
      },
      {
        path: '/guideintro',
        name: 'guideintro',
        component: () => import('@/views/guideintro')
      },
      {
        path: '/guide-intro-detail',
        name: 'guide-intro-detail',
        component: () => import('@/views/guideintro/detail')
      },
      {
        path: '/blog',
        name: 'blog',
        component: () => import('@/views/blog')
      },
      {
        path: '/blog-detail/:blogId',
        name: 'blog-detail',
        component: () => import('@/views/blog/detail')
      },
      {
        path: '/blog-create',
        name: 'blog-create',
        component: () => import('@/views/blog/create')
      },
      {
        path: '/blog-edit/:blogId',
        name: 'blog-edit',
        component: () => import('@/views/blog/edit')
      },
      {
        path: '/product',
        name: 'product',
        component: () => import('@/views/product')
      },
      {
        path: '/contact',
        name: 'contact',
        component: () => import('@/views/contact')
      },
      {
        path: '/finishpay',
        name: 'finishpay',
        component: () => import('@/views/finishpay')
      },
      {
        path: '/alipay',
        name: 'alipay',
        component: () => import('@/views/alipay')
      },
      {
        path: '/wechatpay',
        name: 'wechatpay',
        component: () => import('@/views/wechatpay')
      },
      {
        path: '/product-detail/:skuGroupId',
        name: 'product-detail',
        component: () => import('@/views/product/detail')
      },
      {
        path: '/book',
        name: 'book',
        component: () => import('@/views/book')
      },
      {
        path: '/destination',
        name: 'destination',
        component: () => import('@/views/destination')
      },
      {
        path: '/destination-detail/:destinationId',
        name: 'destination-detail',
        component: () => import('@/views/destination/detail')
      },
      {
        path: '/order-detail/:orderId',
        name: 'order-detail',
        component: () => import('@/views/order/detail')
      },
      {
        path: '/user',
        name: 'user',
        component: () => import('@/views/user')
      },
      {
        path: '/user-info',
        name: 'user-info',
        component: () => import('@/views/userinfo')
      },
      {
        path: '/auth/register',
        name: 'register',
        component: () => import('@/views/register')
      },
      {
        name: 'regsuccess',
        path: '/regsuccess',
        component: () => import('@/views/regsuccess')
      },
      {
        path: '/regvalidation/:regKey',
        name: 'regvalidation',
        component: () => import('@/views/regvalidation')
      },
      {
        path: '/auth/login',
        name: 'login',
        component: () => import('@/views/login')
      },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
});

export default router;
