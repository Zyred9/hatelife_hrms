import axios from 'axios'
import { showToast } from 'vant'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

request.interceptors.response.use(
  (response) => {
    const { code, message, data } = response.data
    if (code === 200) {
      return data
    }
    showToast(message || '请求失败')
    return Promise.reject(new Error(message))
  },
  (error) => {
    showToast(error.message || '网络异常')
    return Promise.reject(error)
  },
)

export default request
