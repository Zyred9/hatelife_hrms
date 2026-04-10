import request from '@/utils/request'

export const listScores = (okrId) => request.get(`/score/okr/${okrId}`)
export const submitScore = (data) => request.post('/score', null, { params: data })
export const listScoreRules = (params) => request.get('/score/rules', { params })
export const updateScoreRule = (id, data) => request.put(`/score/rules/${id}`, data)
export const listRelations = () => request.get('/admin/team/relations')
export const addRelation = (params) => request.post('/admin/team/relation', null, { params })
export const removeRelation = (params) => request.delete('/admin/team/relation', { params })
