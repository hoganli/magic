import Cookies from 'js-cookie'

const LoginKey = 'user'

export function getUser() {
  return Cookies.get(LoginKey);
}

export function setUser(user) {
  return Cookies.set(LoginKey, user)
}

export function removeUser() {
  return Cookies.remove(LoginKey)
}
