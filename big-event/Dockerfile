# 第一阶段：构建阶段
# 使用 Maven 和 JDK 的镜像来构建 Spring Boot 应用
FROM maven:3.9.6-eclipse-temurin-21 AS build

# 安装所需依赖
RUN apt-get update \
  && apt-get install -y ca-certificates curl git --no-install-recommends \
  && rm -rf /var/lib/apt/lists/*

# 设置 Maven 环境变量
ENV MAVEN_HOME /usr/share/maven
ARG MAVEN_VERSION=3.9.6
ARG USER_HOME_DIR="/root"
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# 设置工作目录
WORKDIR /app

# 复制应用的 pom.xml 和 源代码到镜像中
COPY pom.xml .
COPY src src

# 使用 Maven 构建应用，跳过测试
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段
# 使用 JDK 的镜像来运行构建好的 Spring Boot 应用
FROM eclipse-temurin:21-jdk

# 设置工作目录
WORKDIR /app

# 从构建阶段复制构建好的 jar 文件到运行阶段的镜像中
COPY --from=build /app/target/*.jar app.jar

# 指定容器启动时运行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]
