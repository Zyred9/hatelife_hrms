<template>
  <div class="login-page">
    <div class="logo">折耳根绩效管理平台</div>
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
          v-model="form.username"
          name="username"
          placeholder="用户名"
          :rules="[{ required: true, message: '请输入用户名' }]"
        />
        <van-field
          v-model="form.password"
          type="password"
          name="password"
          placeholder="密码"
          :rules="[{ required: true, message: '请输入密码' }]"
        />
      </van-cell-group>
      <div class="submit-btn">
        <van-button round block type="primary" native-type="submit" :loading="loading">
          登录
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { login } from '@/api/auth'

const router = useRouter()
const loading = ref(false)
const form = reactive({ username: '', password: '' })

const onSubmit = async () => {
  loading.value = true
  try {
    const res = await login(form)
    localStorage.setItem('token', res.token)
    localStorage.setItem('userId', res.userId)
    localStorage.setItem('isAdmin', res.isAdmin)
    showToast('登录成功')
    router.push('/')
  } catch (e) {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.logo {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: white;
  margin-bottom: 40px;
}
.submit-btn {
  margin-top: 24px;
}
</style>
