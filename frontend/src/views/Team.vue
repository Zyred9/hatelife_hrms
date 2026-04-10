<template>
  <div class="team-page">
    <van-nav-bar title="团队管理" fixed placeholder />

    <van-empty v-if="relations.length === 0" description="暂无团队关系" />

    <van-list v-else v-model:loading="loading" :finished="finished" @load="loadData">
      <van-cell-group inset>
        <van-cell v-for="rel in relations" :key="rel.id" :title="`上级ID: ${rel.superiorId} → 下级ID: ${rel.subordinateId}`">
          <template #right-icon>
            <van-tag :type="rel.relationType === 1 ? 'primary' : 'default'">{{ rel.relationType === 1 ? '直属' : '间接' }}</van-tag>
            <van-button size="mini" type="danger" @click="removeRel(rel)">删除</van-button>
          </template>
        </van-cell>
      </van-cell-group>
    </van-list>

    <van-button type="primary" block class="add-btn" @click="showAdd = true">新增关系</van-button>

    <van-dialog v-model:show="showAdd" title="新增上下级关系" show-cancel-button @confirm="addRel">
      <van-cell-group inset>
        <van-field v-model="form.superiorId" label="上级ID" type="number" placeholder="上级用户ID" />
        <van-field v-model="form.subordinateId" label="下级ID" type="number" placeholder="下级用户ID" />
        <van-field name="type" label="关系类型">
          <template #input>
            <van-radio-group v-model="form.relationType" direction="horizontal">
              <van-radio :name="1">直属</van-radio>
              <van-radio :name="2">间接</van-radio>
            </van-radio-group>
          </template>
        </van-field>
      </van-cell-group>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { listRelations, addRelation, removeRelation } from '@/api/team'
import { showToast } from 'vant'

const relations = ref([])
const loading = ref(false)
const finished = ref(false)
const showAdd = ref(false)
const form = reactive({ superiorId: '', subordinateId: '', relationType: 1 })

const loadData = async () => {
  try {
    const res = await listRelations()
    relations.value = res
    finished.value = true
  } catch (e) {}
  loading.value = false
}

const addRel = async () => {
  try {
    await addRelation({ ...form })
    showToast('关系已添加')
    loadData()
  } catch (e) {}
}

const removeRel = async (rel) => {
  try {
    await removeRelation({ superiorId: rel.superiorId, subordinateId: rel.subordinateId })
    showToast('关系已删除')
    loadData()
  } catch (e) {}
}

loadData()
</script>

<style scoped>
.team-page {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 60px;
}
.add-btn {
  margin: 16px;
}
</style>
