# 阶段 1: 构建
# 使用带有 Node.js 的官方 Node 基础镜像
FROM node:18.18.0 as build-stage

# 安装 pnpm
RUN npm install -g pnpm

# 设置工作目录
WORKDIR /app

# 复制项目文件
COPY . .

# 安装项目依赖
RUN pnpm install

# 构建应用
RUN pnpm build

# 阶段 2: 以 Nginx 作为服务器运行构建结果
# 使用官方 Nginx 基础镜像
FROM nginx:stable-alpine as production-stage

# 从构建阶段复制构建结果到 Nginx 用于托管的目录
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 暴露 80 端口
EXPOSE 80

# 使用默认的 Nginx 配置启动 Nginx
CMD ["nginx", "-g", "daemon off;"]
