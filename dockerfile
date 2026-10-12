# ============================================
# 阶段一：构建阶段（使用 JDK + Maven）
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. 先只拷贝 pom.xml，单独下载依赖 —— 利用 Docker 缓存
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. 再拷贝源码并打包
COPY src ./src
RUN mvn clean package -DskipTests -B

# 3. 解压分层 JAR
RUN java -Djarmode=layertools -jar target/*.jar extract

# ============================================
# 阶段二：运行阶段（只保留 JRE）
# ============================================
FROM eclipse-temurin:21-jre-alpine

# 安全：创建非 root 用户
RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

# 按"变化频率从低到高"的顺序拷贝各层，最大化缓存复用
COPY --from=builder --chown=app:app /app/dependencies/ ./
COPY --from=builder --chown=app:app /app/spring-boot-loader/ ./
COPY --from=builder --chown=app:app /app/snapshot-dependencies/ ./
COPY --from=builder --chown=app:app /app/application/ ./

USER app

# JVM 容器化参数
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+ExitOnOutOfMemoryError \
    -XX:+UseG1GC \
    -Djava.security.egd=file:/dev/./urandom"

EXPOSE 10020

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]