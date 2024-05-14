# 前端构建镜像文件
FROM nginx:1.21

# 指定工作目录
WORKDIR /usr/share/nginx/html

# 把docker文件下的配置文件复制到容器中去
COPY ./docker/nginx.conf /etc/nginx/conf.d/default.conf

# 把dist目录下的文件，赋值到指定目录
COPY ./frontend/dist /usr/share/nginx/html/back/
COPY ./buddy-vue3/dist /usr/share/nginx/html/front/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]