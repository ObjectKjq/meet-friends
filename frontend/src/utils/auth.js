import Cookies from 'js-cookie'

const SessionKey = 'JSESSIONID'

export function getSesseion() {
  return Cookies.get(SessionKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
