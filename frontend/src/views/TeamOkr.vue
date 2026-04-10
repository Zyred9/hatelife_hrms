<template>
  <div class="team-okr-page">
    <van-nav-bar title="团队OKR" fixed placeholder />

    <van-tabs v-model:active="activeTab">
      <van-tab title="审核中" name="reviewing">
        <van-list v-model:loading="loading" :finished="finished" @load="loadData">
          <van-cell-group v-for="okr in list" :key="okr.id" class="okr-card" inset>
            <van-cell :title="okr.title" :label="`用户ID: ${okr.userId}`">
              <template #right-icon>
                <van-tag type="primary">审核中</van-tag>
              </template>
            </van-cell>
            <van-cell>
              <template #right-icon>
                <van-button size="mini" type="success" @click="reviewOkr(okr.id, 1)">通过</van-button>
                <van-button size="mini" type="warning" @click="showReject(okr.id)">驳回</van-button>
                <van-button size="mini" type="danger" @click="reviewOkr(okr.id, 3)">作废</van-button>
              </template>
            </van-cell>
          </van-cell-group>
          <van-empty v-if="list.length === 0 && !loading" description="暂无待审核OKR" />
        </van-list>
      </van-tab>
      <van-tab title="全部" name="all">
        <van-list v-model:loading="allLoading" :finished="allFinished" @load="loadAllData">
          <van-cell-group v-for="okr in allList" :key="okr.id" class="okr-card" inset>
            <van-cell :title="okr.title" :label="`用户ID: ${okr.userId}`">
              <template #right-icon>
                <van-tag :type="statusTagType(okr.status)">{{ statusText(okr.status) }}</van-tag>
              </template>
            </van-cell>
          </van-cell-group>
          <van-empty v-if="allList.length === 0 && !allLoading" description="暂无OKR记录" />
        </van-list>
      </van-tab>
    </van-tabs>

    <van-dialog v-model:show="showRejectDialog" title="驳回原因" show-cancel-button @confirm="confirmReject">
      <van-field v-model="rejectReason" placeholder="请填写驳回原因" />
    </van-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { listTeamOkr, reviewOkr } from '@/api/okr'
import { showToast } from 'vant'

const activeTab = ref('reviewing')
const list = ref([])
const loading = ref(false)
const finished = ref(false)
const allList = ref([])
const allLoading = ref(false)
const allFinished = ref(false)

const showRejectDialog = ref(false)
const rejectReason = ref('')
const rejectOkrId = ref(null)

const loadData = async () => {
  try {
    const res = await listTeamOkr({ current: 1, size: 20, status: 2 })
    list.value = res.records
    finished.value = true
  } catch (e) {}
  loading.value = false
}

const loadAllData = async () => {
  try {
    const res = await listTeamOkr({ current: 1, size: 20 })
    allList.value = res.records
    allFinished.value = true
  } catch (e) {}
  allLoading.value = false
}

const showReject = (id) => {
  rejectOkrId.value = id
  showRejectDialog.value = true
}

const confirmReject = async () => {
  if (!rejectReason.value) {
    showToast('请填写驳回原因')
    return
  }
  try {
    await reviewOkr(rejectOkrId.value, { action: 2, reason: rejectReason.value })
    showToast('已驳回')
    loadData()
  } catch (e) {}
}

const reviewOkrFn = async (id, action) => {
  try {
    await reviewOkr(id, { action })
    showToast(action === 1 ? '已通过' : '已作废')
    loadData()
  } catch (e) {}
}

const statusText = (status) => ({ 1: '正常', 2: '审核中', 3: '驳回', 4: '作废' }[status] || '-')
const statusTagType = (status) => ({ 1: 'success', 2: 'primary', 3: 'warning', 4: 'default' }[status] || 'default')

loadData()
</script>

<style scoped>
.team-okr-page {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 60px;
}
.okr-card {
  margin: 8px 0;
  border-radius: 8px;
}
</style>
