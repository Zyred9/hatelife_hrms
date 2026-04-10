<template>
  <div class="okr-edit-page">
    <van-nav-bar :title="isEdit ? '编辑OKR' : '新增OKR'" fixed placeholder left-arrow @click-left="$router.back()" />

    <van-tabs v-model:active="inputType" class="input-tabs">
      <van-tab title="手动填写" name="manual">
        <van-form @submit="onSubmit">
          <van-cell-group inset>
            <van-field v-model="form.title" name="title" label="标题" placeholder="OKR标题" :rules="[{ required: true }]" />
            <van-field v-model="form.content" name="content" label="内容" type="textarea" placeholder="Objective + Key Results" rows="8" :rules="[{ required: true }]" />
          </van-cell-group>
          <div class="submit-btn">
            <van-button round block type="primary" native-type="submit" :loading="loading">提交审核</van-button>
          </div>
        </van-form>
      </van-tab>
      <van-tab title="图片上传" name="image">
        <van-uploader v-model="fileList" :max-count="5" @after-read="onImageRead">
          <div class="upload-area">
            <van-icon name="photograph" size="24" />
            <div>上传图片</div>
          </div>
        </van-uploader>
        <van-cell-group inset style="margin-top: 12px">
          <van-field v-model="form.content" name="content" label="识别结果" type="textarea" placeholder="图片识别后的内容" rows="8" :rules="[{ required: true }]" />
        </van-cell-group>
        <div class="submit-btn">
          <van-button round block type="primary" @click="onSubmit">提交审核</van-button>
        </div>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { showToast } from 'vant'
import { createOkr, updateOkr } from '@/api/okr'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => !!route.params.id)
const inputType = ref('manual')
const loading = ref(false)
const fileList = ref([])
const form = ref({ title: '', content: '' })

const onSubmit = async () => {
  if (!form.value.title || !form.value.content) {
    showToast('请填写完整信息')
    return
  }
  loading.value = true
  try {
    if (isEdit.value) {
      await updateOkr(route.params.id, form.value)
    } else {
      await createOkr(form.value)
    }
    showToast('提交成功')
    router.back()
  } catch (e) {}
  loading.value = false
}

const onImageRead = (file) => {
  showToast('图片已上传，OCR识别功能待接入')
}
</script>

<style scoped>
.okr-edit-page {
  background: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 20px;
}
.input-tabs {
  margin-top: 12px;
}
.upload-area {
  width: 80px;
  height: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f7f8fa;
  border-radius: 8px;
  color: #999;
  font-size: 12px;
}
.submit-btn {
  margin: 16px;
}
</style>
