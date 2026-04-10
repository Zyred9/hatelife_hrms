import request from '@/utils/request'

export const listNotifications = () => request.get('/admin/settings/notifications')
export const updateNotification = (id, data) => request.put(`/admin/settings/notifications/${id}`, data)
export const listAssistantQa = (params) => request.get('/admin/settings/assistant-qa', { params })
export const addQa = (data) => request.post('/admin/settings/assistant-qa', data)
export const updateQa = (id, data) => request.put(`/admin/settings/assistant-qa/${id}`, data)
export const deleteQa = (id) => request.delete(`/admin/settings/assistant-qa/${id}`)
