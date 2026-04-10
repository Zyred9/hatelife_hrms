<template>
  <div class="my-okr-page">
    <van-nav-bar title="我的OKR" fixed placeholder />

    <div class="status-tabs">
      <van-tabs v-model:active="statusFilter" @change="loadData">
        <van-tab title="全部" :name="null" />
        <van-tab title="正常" :name="1" />
        <van-tab title="审核中" :name="2" />
        <van-tab title="驳回待修改" :name="3" />
        <van-tab title="作废" :name="4" />
      </van-tabs>
    </div>

    <van-pull-refresh v-model="refreshing" @refresh="loadData">
      <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="loadData">
        <van-cell-group v-for="okr in list" :key="okr.id" class="okr-card" inset>
          <van-cell :title="okr.title" :label="okr.content">
            <template #right-icon>
              <van-tag :type="statusTagType(okr.status)">{{ statusText(okr.status) }}</van-tag>
            </template>
          </van-cell>
          <van-cell>
            <template #title>
              <span class="time-text">{{ formatTime(okr.createdAt) }}</span>
            </template>
            <template #right-icon>
              <van-button v-if="canEdit(okr)" size="mini" type="primary" @click="$router.push(`/okr-edit/${okr.id}`)">编辑</van-button>
              <van-button v-else-if="canCreate(okr)" size="mini" type="success" @click="$router.push('/okr-edit')">新增</van-button>
            </template>
          </van-cell>
        </van-cell-group>
        <van-empty v-if="list.length === 0 && !loading" description="暂无OKR记录" />
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { listMyOkr } from '@/api/okr'

const statusFilter = ref(null)
const list = ref([])
const loading = ref(false)
const finished = ref(false)
const refreshing = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    const res = await listMyOkr({ current: 1, size: 20, status: statusFilter.value })
    list.value = res.records
    finished.value = true
  } catch (e) {}
  loading.value = false
  refreshing.value = false
}

const statusText = (status) => ({ 1: '正常', 2: '审核中', 3: '驳回待修改', 4: '作废' }[status] || '-')
const statusTagType = (status) => ({ 1: 'success', 2: 'primary', 3: 'warning', 4: 'default' }[status] || 'default')
const canEdit = (okr) => okr.status === 1 || okr.status === 3
const canCreate = (okr) => okr.status === 4
const formatTime = (t) => t ? t.split('T')[0] : ''

loadData()
</script>

<style scoped>
.my-okr-page {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 60px;
}
.okr-card {
  margin: 8px 0;
  border-radius: 8px;
}
.time-text {
  font-size: 12px;
  color: #999;
}
</style>
