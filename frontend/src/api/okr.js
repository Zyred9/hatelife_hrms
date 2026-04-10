import request from '@/utils/request'

export const listMyOkr = (params) => request.get('/okr/my', { params })
export const createOkr = (data) => request.post('/okr', data)
export const updateOkr = (id, data) => request.put(`/okr/${id}`, data)
export const reviewOkr = (id, params) => request.post(`/okr/${id}/review`, null, { params })
export const listTeamOkr = (params) => request.get('/okr/team', { params })
