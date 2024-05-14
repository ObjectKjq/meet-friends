import axios from "axios";
import { showFailToast } from "vant";

// 配置前端拿上cookie发送请求。
axios.defaults.withCredentials = true;
// 设置ip地址
let baseURL = import.meta.env.VITE_APP_API_BASEURL;

const service = axios.create({
  baseURL: baseURL,
  timeout: 5000,
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    console.log(error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code !== 20000) {
      showFailToast(res.message);
      return Promise.reject("服务器返回失败结果");
    } else {
      return res;
    }
  },
  (error) => {
    showFailToast("服务异常!");
    console.log("请求异常，异常信息是" + error); // 打印异常信息
    return Promise.reject(error);
  }
);

export default service;
