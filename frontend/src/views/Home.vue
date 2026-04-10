<template>
  <div class="home-page">
    <!-- AI 助手 -->
    <div class="assistant-card">
      <div class="assistant-avatar" @click="switchAssistant">{{ currentAssistant.emoji }}</div>
      <div class="assistant-content">
        <div class="assistant-name">{{ currentAssistant.name }}</div>
        <van-search v-model="searchQuery" placeholder="搜索问题或功能" shape="round" />
      </div>
    </div>

    <!-- 书架 -->
    <div class="section">
      <div class="section-title">书架</div>
      <div class="bookshelf">
        <div class="book-item" v-for="book in books" :key="book.id">
          {{ book.title }}
        </div>
        <div class="book-item empty" @click="showAddBook = true">+ 新增解读</div>
      </div>
    </div>

    <!-- 日历 -->
    <div class="section">
      <div class="section-title">流程日历</div>
      <div class="calendar-placeholder">
        <van-calendar :show-confirm="false" @select="onDateSelect" />
      </div>
    </div>

    <!-- 流程待办 -->
    <div class="section">
      <div class="section-title">流程待办</div>
      <van-tabs v-model:active="todoTab">
        <van-tab title="当前待办" name="current">
          <van-empty v-if="todos.length === 0" description="暂无待办" />
          <van-cell v-for="todo in todos" :key="todo.id" :title="todo.title" is-link />
        </van-tab>
        <van-tab title="历史流程" name="history">
          <van-empty v-if="historyTodos.length === 0" description="暂无历史流程" />
        </van-tab>
      </van-tabs>
    </div>

    <!-- 最近访问 -->
    <div class="section">
      <div class="section-title">最近访问</div>
      <van-grid :column-num="5" :border="false">
        <van-grid-item to="/my-okr" icon="records-o" text="我的OKR" />
        <van-grid-item to="/team-okr" icon="friends-o" text="团队OKR" />
        <van-grid-item to="/account" icon="user-o" text="账号管理" />
        <van-grid-item to="/team" icon="cluster-o" text="团队管理" />
        <van-grid-item to="/settings" icon="setting-o" text="系统设置" />
      </van-grid>
    </div>

    <!-- 新增书籍弹窗 -->
    <van-dialog v-model:show="showAddBook" title="新增书籍解读" show-cancel-button @confirm="addBook">
      <van-field v-model="newBookTitle" placeholder="输入完整书名" />
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const searchQuery = ref('')
const todoTab = ref('current')
const todos = ref([])
const historyTodos = ref([])
const books = ref([])
const showAddBook = ref(false)
const newBookTitle = ref('')

const assistants = [
  { name: '根宝', emoji: '🐱' },
  { name: '多多', emoji: '🐱' },
  { name: '蛋挞', emoji: '🐱' },
]
const currentAssistantIndex = ref(0)
const currentAssistant = computed(() => assistants[currentAssistantIndex.value])

const switchAssistant = () => {
  currentAssistantIndex.value = (currentAssistantIndex.value + 1) % assistants.length
}

const onDateSelect = () => {}
const addBook = () => {}
</script>

<style scoped>
.home-page {
  padding-bottom: 60px;
  background: #f5f5f5;
  min-height: 100vh;
}
.assistant-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}
.assistant-avatar {
  font-size: 36px;
  cursor: pointer;
}
.assistant-content {
  flex: 1;
}
.assistant-name {
  color: white;
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 8px;
}
.section {
  background: white;
  margin: 12px;
  border-radius: 12px;
  padding: 12px;
}
.section-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;
}
.bookshelf {
  display: flex;
  gap: 8px;
  overflow-x: auto;
}
.book-item {
  min-width: 80px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f0f0;
  border-radius: 8px;
  font-size: 12px;
}
.book-item.empty {
  color: #999;
  cursor: pointer;
}
.calendar-placeholder {
  min-height: 200px;
}
</style>
