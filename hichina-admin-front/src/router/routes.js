const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/IndexPage.vue") }],
  },
  {
    path: "/auth",
    component: () => import("layouts/BlankLayout.vue"),
    children: [
      { path: "login", component: () => import("pages/LoginPage.vue") },
    ],
  },
  {
    path: "/create-line",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/CreateLinePage.vue") },
    ],
  },
  {
    path: "/list-line",
    component: () => import("layouts/MainLayout.vue"),
    children: [{ path: "", component: () => import("pages/ListLinePage.vue") }],
  },
  {
    path: "/edit-and-preview-line/:publicid",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/EditAndPreviewPage.vue") },
    ],
  },
  {
    path: "/product-shared-attributes",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/ProductSharedAttributesPage.vue"),
      },
    ],
  },
  {
    path: "/product-type",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/ProductTypePage.vue"),
      },
    ],
  },
  {
    path: "/product-sku",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/ProductSkuPage.vue"),
      },
    ],
  },
  {
    path: "/product-sku-group",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/SkuGroupPage.vue"),
      },
    ],
  },
  {
    path: "/order",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/OrderPage.vue"),
      },
    ],
  },
  {
    path: "/guidebook",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/GuidebookPage.vue"),
      },
    ],
  },
  {
    path: "/guidebook-intro",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/GuidebookIntroManagePage.vue"),
      },
    ],
  },
  {
    path: "/bloghome",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/BloghomeManagePage.vue"),
      },
    ],
  },
  {
    path: "/logintest",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/LoginTestPage.vue"),
      },
    ],
  },
  {
    path: "/destination",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/DestinationPage.vue"),
      },
    ],
  },
  {
    path: "/home-slide-image",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/HomeSlideImagePage.vue"),
      },
    ],
  },
  {
    path: "/adminuser",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/AdminUserManagePage.vue"),
      },
    ],
  },
  {
    path: "/user",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      {
        path: "",
        component: () => import("pages/UserManagePage.vue"),
      },
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
