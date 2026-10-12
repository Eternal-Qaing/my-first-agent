# My First Agent

> 基于 Spring Boot 4 + Spring AI 2 构建的生产级 Java AI Agent，支持真实工具调用、多轮记忆持久化与流式输出。

[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-green)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-2.0.1-blue)](https://spring.io/projects/spring-ai)
[![License](https://img.shields.io/badge/license-MIT-lightgrey)](./LICENSE)

---

## ✨ 功能特性

| 特性 | 说明 |
|---|---|
| 🤖 **大模型对话** | 对接阿里云百炼（通义千问），兼容 OpenAI 协议 |
| 🛠️ **真实工具调用** | 集成高德地图天气 API，Agent 自主决策调用 |
| 🧠 **多轮记忆** | 基于 `conversationId` 的会话隔离与上下文记忆 |
| 💾 **记忆持久化** | 记忆写入 Redis Stack，服务重启不丢失 |
| ⚡ **SSE 流式输出** | 打字机效果，显著改善长回答的体验 |
| 🛡️ **全局异常处理** | 统一错误码 + 友好提示，不暴露内部堆栈 |
| ✅ **参数校验** | 消息长度、会话 ID 长度校验 |
| 📝 **配置外置** | System Prompt 与 API Key 均通过配置注入 |
| 🐳 **Docker 化部署** | 一条命令启动，跨环境一致 |
| 📊 **结构化日志** | SLF4J + 占位符，生产可用 |

---

## 🏗️ 架构

```mermaid
graph LR
    A[浏览器/客户端] -->|HTTP| B[Spring Boot 应用<br/>:10020]
    B -->|ChatClient| C[阿里云百炼<br/>通义千问]
    B -->|@Tool 调用| D[高德天气 API]
    B -->|ChatMemory| E[(Redis Stack)]