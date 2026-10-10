# 我的第一个 Agent

基于 Spring Boot 和 Spring AI 的简单 Agent 应用示例。

## 项目简介

这是一个入门级的 AI Agent 应用，使用 Spring AI 框架构建，演示了如何创建带有工具调用能力的智能对话 Agent。

## 技术栈

- Java 17+
- Spring Boot 3.x
- Spring AI
- Maven

## 项目结构

```
src/main/java/com/qiang/myfirstagent/
├── MyFirstAgentApplication.java    # Spring Boot 启动类
├── config/
│   └── ChatClientConfig.java       # ChatClient 配置
├── controller/
│   └── ChatController.java         # REST 控制器
└── tools/
    └── WeatherTools.java           # 天气查询工具
```

## 核心功能

### 1. 智能对话
通过 `/chat` 接口与 AI 进行对话交互。

### 2. 工具调用
内置天气查询工具，AI 可以自动调用该工具获取实时天气信息。

## 快速开始

### 1. 克隆项目

```bash
git clone https://gitee.com/yang-deqiang/my-first-agent.git
cd my-first-agent
```

### 2. 配置

在 `src/main/resources/application.yml` 中配置你的 AI API 密钥和模型参数。

### 3. 运行项目

```bash
./mvnw spring-boot:run
```

或使用 Maven：

```bash
./mvnw clean package
java -jar target/my-first-agent-0.0.1-SNAPSHOT.jar
```

### 4. 访问服务

启动后访问聊天接口：

```
http://localhost:8080/chat?message=你好
```

查询天气：

```
http://localhost:8080/chat?message=杭州天气怎么样
```

## API 接口

### 聊天接口

- **URL**: `/chat`
- **方法**: GET
- **参数**:
  - `message`: 对话内容（必填）
  - `conversationId`: 会话 ID（可选，默认值为 `default_user`）
- **响应**: JSON 格式的对话回复

## 扩展工具

可以在 `tools` 包中添加更多自定义工具类，只需使用 `@Tool` 和 `@ToolParam` 注解标注方法即可。

## 许可证

本项目仅供学习参考使用。