<template>
  <div class="settings-page">
    <van-nav-bar title="系统设置" fixed placeholder />

    <van-tabs v-model:active="activeTab">
      <van-tab title="消息提醒" name="notification">
        <van-cell-group inset>
          <van-switch-cell v-for="item in notifications" :key="item.id" v-model="item.enabled" :title="item.configKey" @change="toggleNotification(item)" />
        </van-cell-group>
        <van-empty v-if="notifications.length === 0" description="暂无配置" />
      </van-tab>

      <van-tab title="评分规则" name="score">
        <van-cell-group inset>
          <van-cell v-for="rule in scoreRules" :key="rule.id" :title="rule.dimensionName" :label="`权重: ${rule.weight}% | ${rule.description || ''}`" />
        </van-cell-group>
        <van-empty v-if="scoreRules.length === 0" description="暂无规则" />
      </van-tab>

      <van-tab title="助手问答" name="qa">
        <van-cell-group inset>
          <van-collapse v-model="expandedQa">
            <van-collapse-item v-for="qa in qaList" :key="qa.id" :name="qa.id" :title="qa.question">
              <div class="qa-answer">{{ qa.answer }}</div>
            </van-collapse-item>
          </van-collapse>
        </van-cell-group>
        <van-empty v-if="qaList.length === 0" description="暂无问答" />
      </van-tab>

      <van-tab title="关于" name="about">
        <van-cell-group inset>
          <van-cell title="版本" value="1.0.0" />
          <van-cell title="技术栈" value="Vue3 + Spring Boot" />
        </van-cell-group>
      </van-tab>
    </van-tabs>

    <div class="logout-btn">
      <van-button block type="danger" @click="handleLogout">退出登录</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { listNotifications, updateNotification } from '@/api/settings'
import { listScoreRules } from '@/api/team'
import { listAssistantQa } from '@/api/settings'

const activeTab = ref('notification')
const notifications = ref([])
const scoreRules = ref([])
const qaList = ref([])
const expandedQa = ref([])
const router = useRouter()

const loadNotifications = async () => {
  try { notifications.value = await listNotifications() } catch (e) {}
}

const loadScoreRules = async () => {
  try { scoreRules.value = await listScoreRules() } catch (e) {}
}

const loadQa = async () => {
  try { qaList.value = await listAssistantQa() } catch (e) {}
}

const toggleNotification = async (item) => {
  try { await updateNotification(item.id, item) } catch (e) {}
}

const handleLogout = () => {
  localStorage.clear()
  router.push('/login')
}

onMounted(() => {
  loadNotifications()
  loadScoreRules()
  loadQa()
})
</script>

<style scoped>
.settings-page {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 80px;
}
.qa-answer {
  padding: 8px 0;
  color: #666;
  line-height: 1.6;
}
.logout-btn {
  position: fixed;
  bottom: 60px;
  left: 0;
  right: 0;
  padding: 16px;
  background: white;
}
</style>
