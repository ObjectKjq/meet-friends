import request from './index'
import qs from 'qs'

// 发起搜索请求获取后端用户列表
export function searchUsersByTags(tagList) {
    return request({
      url: '/user/search/tags',
      method: 'get',
      params:{
        tagList,
      },
      paramsSerializer: params => {
        return qs.stringify(params, {indices: false})
      }
    })
}

// 发起获取当前登录用户
export function getLoginUser() {
    return request({
      url: '/user/info',
      method: 'get'
    })
}
  

// 发起跟新用户的请求
export function updateUser(user) {
    return request({
      url: '/user/update',
      method: 'post',
      data:user
    })
}

// 主页列表请求的数据
export function recommendUsers(userPages) {
    return request({
      url: '/user/recommend',
      method: 'get',
      params:userPages
    })
}

// 主页列表请求的数据
export function matchUsers() {
  return request({
    url: '/user/match',
    method: 'get',
    params:{
      num: 10,
    }
  })
}

// 发起请求搜索队伍
export function listTeams(searchQuery) {
  return request({
    url: '/team/list',
    method: 'get',
    params:searchQuery
  })
}

// 发起请求添加队伍队伍
export function addTeam(postData) {
  return request({
    url: '/team/add',
    method: 'post',
    data:postData
  })
}

// 发起请求添加队伍队伍
export function joinTeam(joinQuery) {
  return request({
    url: '/team/join',
    method: 'post',
    data:joinQuery
  })
}

// 根据id获取队伍信息
export function getTeamById(id) {
  return request({
    url: '/team/get',
    method: 'get',
    params:{
      id,
    }
  })
}

// 更新队伍信息
export function updateTeam(postData) {
  return request({
    url: '/team/update',
    method: 'post',
    data:postData
  })
}

// 退出队伍
export function quitTeam(id) {
  return request({
    url: '/team/quit',
    method: 'post',
    data:{
      teamId:id
    }
  })
}

// 解散队伍
export function deleteTeam(id) {
  return request({
    url: '/team/delete',
    method: 'post',
    data:{
      id
    }
  })
}

// 获取我创建的队伍
export function listMyCreateTeams(query) {
  return request({
    url: '/team/list/my/create',
    method: 'get',
    params:query
  })
}

// 获取我加入的队伍
export function listMyJoinTeams(query) {
  return request({
    url: '/team/list/my/join',
    method: 'get',
    params:query
  })
}

// 获取所有的标签数据
export function listTag() {
  return request({
    url: '/tag/list',
    method: 'get'
  })
}

// 获取所有的标签数据
export function updateUserTag(query) {
  return request({
    url: '/user/update',
    method: 'post',
    data:query
  })
}

// 登录
export function login(query) {
  return request({
    url: '/user/login',
    method: 'post',
    data:query
  })
}
// 退出登录
export function logOut() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}