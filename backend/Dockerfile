# 自动打包maven3.5和java8
FROM maven:3.5-jdk-8alpine as builder

# 指定项目的工作目录
WORKDIR /app
# 把pom.xml复制到app目录中
COPY pom.xml .
# 把源码复制到app的src下
COPY src ./src

# 执行maven的打包命令
RUN mvn package -DskipTests

CMD ["java","-jar","/app/target/backend-1.0-SNAPSHOT.jar","--spring.profiles.active=prod"]