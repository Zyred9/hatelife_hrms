<template>
  <div class="account-page">
    <van-nav-bar title="账号管理" fixed placeholder right-text="新增" @click-right="showAdd = true" />

    <van-search v-model="keyword" placeholder="搜索姓名或手机号" @search="loadData" />

    <van-list v-model:loading="loading" :finished="finished" @load="loadData">
      <van-cell-group v-for="user in list" :key="user.id" class="user-card" inset>
        <van-cell :title="user.name" :label="`手机: ${user.phone} | 部门: ${user.department || '未分配'}`">
          <template #right-icon>
            <van-tag :type="user.isAdmin ? 'danger' : 'primary'">{{ user.isAdmin ? '管理员' : '普通' }}</van-tag>
          </template>
        </van-cell>
        <van-cell>
          <template #right-icon>
            <van-button size="mini" @click="editUser(user)">编辑</van-button>
            <van-button size="mini" type="danger" v-if="!user.isAdmin" @click="deleteUser(user.id)">删除</van-button>
          </template>
        </van-cell>
      </van-cell-group>
      <van-empty v-if="list.length === 0 && !loading" description="暂无用户" />
    </van-list>

    <van-dialog v-model:show="showAdd" :title="editingUser ? '编辑用户' : '新增用户'" show-cancel-button @confirm="saveUser">
      <van-cell-group inset>
        <van-field v-model="form.name" label="姓名" placeholder="姓名" :rules="[{ required: true }]" />
        <van-field v-model="form.phone" label="手机号" placeholder="手机号" :rules="[{ required: true }]" />
        <van-field v-model="form.username" label="用户名" placeholder="用户名" :rules="[{ required: true }]" :disabled="!!editingUser" />
        <van-field v-model="form.password" label="密码" placeholder="密码" :rules="[{ required: true }]" type="password" />
        <van-field v-model="form.department" label="部门" placeholder="部门" />
      </van-cell-group>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { listUsers, createUser, updateUser, deleteUser as apiDeleteUser } from '@/api/user'
import { showToast, showConfirmDialog } from 'vant'

const keyword = ref('')
const list = ref([])
const loading = ref(false)
const finished = ref(false)
const showAdd = ref(false)
const editingUser = ref(null)
const form = reactive({ name: '', phone: '', username: '', password: '', department: '' })

const loadData = async () => {
  try {
    const res = await listUsers({ current: 1, size: 50, keyword: keyword.value })
    list.value = res.records
    finished.value = true
  } catch (e) {}
  loading.value = false
}

const editUser = (user) => {
  editingUser.value = user
  form.name = user.name
  form.phone = user.phone
  form.username = user.username
  form.password = user.password
  form.department = user.department || ''
  showAdd.value = true
}

const saveUser = async () => {
  try {
    if (editingUser.value) {
      await updateUser(editingUser.value.id, { ...form })
    } else {
      await createUser(form)
    }
    showToast('保存成功')
    loadData()
  } catch (e) {}
}

const deleteUser = async (id) => {
  try {
    await showConfirmDialog({ title: '确认删除', message: '确定要删除该用户吗？' })
    await apiDeleteUser(id)
    showToast('已删除')
    loadData()
  } catch (e) {}
}

loadData()
</script>

<style scoped>
.account-page {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 60px;
}
.user-card {
  margin: 8px 0;
  border-radius: 8px;
}
</style>
