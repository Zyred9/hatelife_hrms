<template>
  <router-view />
  <van-tabbar v-if="showTabbar" route>
    <van-tabbar-item to="/" icon="wap-home-o">首页</van-tabbar-item>
    <van-tabbar-item to="/my-okr" icon="records-o">我的OKR</van-tabbar-item>
    <van-tabbar-item to="/team-okr" icon="friends-o">团队OKR</van-tabbar-item>
    <van-tabbar-item to="/settings" icon="setting-o">设置</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const showTabbar = computed(() => route.name !== 'Login' && route.name !== 'OkrEdit')
</script>

<style>
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #f5f5f5;
}
</style>
