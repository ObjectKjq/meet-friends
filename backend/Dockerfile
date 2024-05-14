# 自动打包maven3.6和java8
FROM maven:3.6-jdk-8-alpine as builder

# 指定项目的工作目录
WORKDIR /app
# 配置maven镜像快速下载包
COPY ./docker/settings.xml /usr/share/maven/conf/
# 把pom.xml复制到app目录中
COPY pom.xml .
# 把源码复制到/app/src下
COPY src ./src

# 执行maven的打包命令
RUN mvn package -DskipTests

CMD ["java","-jar","/app/target/backend-1.0-SNAPSHOT.jar","--spring.profiles.active=prod"]
