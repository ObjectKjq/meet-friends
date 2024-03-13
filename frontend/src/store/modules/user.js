import { login, logout, getInfo } from '@/api/user'

const getDefaultState = () => {
  return {
    name: '',
    avatar: '',
    roles: []
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // 登录接口
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ userAccount: username.trim(), userPassword: password }).then(res => {
        // const {data} = res;
        // commit('SET_NAME', data.username)
        // commit('SET_AVATAR', data.avatarUrl)
        // if(data.userRole === 0){
        //   commit('SET_ROLES', ['user'])
        // }else{
        //   commit('SET_ROLES', ['admin'])
        // }
        // commit('SET_TOKEN', data.token)
        // 不给cookie设置信息
        // setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取用户数据，没有用户名和密码
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(res => {
        const { data } = res
        if (!res || data === '') {
          return reject('Verification failed, please Login again.')
        }
        commit('SET_NAME', data.username)
        commit('SET_AVATAR', data.avatarUrl)
        if(data.userRole === 0){
          commit('SET_ROLES', ['user'])
        }else{
          commit('SET_ROLES', ['admin'])
        }
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 退出登录
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        // removeToken() // must remove  token  first
        // resetRouter()
        // 清空state中的值
        commit('RESET_STATE')
        commit('SET_ROLES', [])
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

