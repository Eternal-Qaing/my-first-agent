# My First Agent

A simple Agent application example based on Spring Boot and Spring AI.

## Project Introduction

This is an entry-level AI Agent application built using the Spring AI framework, demonstrating how to create a smart conversational Agent with tool calling capabilities.

## Technology Stack

- Java 17+
- Spring Boot 3.x
- Spring AI
- Maven

## Project Structure

```
src/main/java/com/qiang/myfirstagent/
├── MyFirstAgentApplication.java    # Spring Boot Main Class
├── config/
│   └── ChatClientConfig.java       # ChatClient Configuration
├── controller/
│   └── ChatController.java         # REST Controller
└── tools/
    └── WeatherTools.java           # Weather Query Tool
```

## Core Features

### 1. Intelligent Conversation
Interact with the AI via the `/chat` endpoint.

### 2. Tool Calling
Built-in weather query tool; the AI can automatically call this tool to retrieve real-time weather information.

## Quick Start

### 1. Clone the Project

```bash
git clone https://gitee.com/yang-deqiang/my-first-agent.git
cd my-first-agent
```

### 2. Configuration

Configure your AI API key and model parameters in `src/main/resources/application.yml`.

### 3. Run the Project

```bash
./mvnw spring-boot:run
```

Or use Maven:

```bash
./mvnw clean package
java -jar target/my-first-agent-0.0.1-SNAPSHOT.jar
```

### 4. Access the Service

After starting, access the chat endpoint:

```
http://localhost:8080/chat?message=Hello
```

Query weather:

```
http://localhost:8080/chat?message=How is the weather in Hangzhou
```

## API Interfaces

### Chat Endpoint

- **URL**: `/chat`
- **Method**: GET
- **Parameters**:
  - `message`: Conversation content (Required)
  - `conversationId`: Session ID (Optional, default value is `default_user`)
- **Response**: Conversation reply in JSON format

## Extending Tools

You can add more custom tool classes in the `tools` package; simply annotate methods with `@Tool` and `@ToolParam`.

## License

This project is for learning and reference purposes only.