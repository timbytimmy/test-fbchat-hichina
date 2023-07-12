const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/IndexPage.vue") }],
  },
  {
    path: "/guideintro",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/GuideIntroPage.vue") },
    ],
  },
  {
    path: "/blog",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/BlogPage.vue") }],
  },
  {
    path: "/product",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/TravelShopPage.vue") },
    ],
  },
  {
    path: "/destination",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/DestinationPage.vue") },
    ],
  },
  {
    path: "/auth/login",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/LoginPage.vue") }],
  },
  {
    path: "/auth/register",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/RegisterPage.vue") }],
  },
  {
    path: "/product-detail/:skuGroupId",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/ProductDetailPage.vue") },
    ],
  },
  {
    path: "/destination-detail/:destinationId",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/DestinationDetailPage.vue") },
    ],
  },
  {
    path: "/blog-detail/:blogId",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/BlogDetailPage.vue") },
    ],
  },
  {
    path: "/contact",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/ContactPage.vue") }],
  },
  {
    path: "/my-blogs",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/MyBlogsPage.vue") }],
  },
  {
    path: "/my-orders",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/MyOrdersPage.vue") }],
  },
  {
    path: "/user-info",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/UserInfoPage.vue") }],
  },
  {
    path: "/blog-create",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/BlogCreatePage.vue") },
    ],
  },
  {
    path: "/blog-edit/:blogId",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/BlogEditPage.vue") }],
  },
  {
    name: "book",
    path: "/book",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/BookPage.vue") }],
  },
  {
    path: "/regsuccess",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/RegSuccessPage.vue") },
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: "/:catchAll(.*)*",
    component: () => import("pages/ErrorNotFound.vue"),
  },
];

export default routes;
