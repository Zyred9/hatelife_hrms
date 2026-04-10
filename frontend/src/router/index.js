import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue') },
  { path: '/', name: 'Home', component: () => import('@/views/Home.vue'), meta: { auth: true } },
  { path: '/my-okr', name: 'MyOkr', component: () => import('@/views/MyOkr.vue'), meta: { auth: true } },
  { path: '/okr-edit/:id?', name: 'OkrEdit', component: () => import('@/views/OkrEdit.vue'), meta: { auth: true } },
  { path: '/team-okr', name: 'TeamOkr', component: () => import('@/views/TeamOkr.vue'), meta: { auth: true } },
  { path: '/account', name: 'Account', component: () => import('@/views/Account.vue'), meta: { auth: true } },
  { path: '/team', name: 'Team', component: () => import('@/views/Team.vue'), meta: { auth: true } },
  { path: '/settings', name: 'Settings', component: () => import('@/views/Settings.vue'), meta: { auth: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.auth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
