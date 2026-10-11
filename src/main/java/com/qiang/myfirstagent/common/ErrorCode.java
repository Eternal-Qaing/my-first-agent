package com.qiang.myfirstagent.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // ========== AI 服务相关 ==========
    AI_SERVICE_TIMEOUT(10001, "AI 服务响应超时，请稍后重试。"),
    AI_SERVICE_UNAVAILABLE(10002, "AI 服务暂时不可用，请稍后再试。"),
    AI_SERVICE_INSUFFICIENT_BALANCE(10003, "AI 服务额度不足，请联系管理员。"),
    AI_SERVICE_UNAUTHORIZED(10004, "AI 服务认证失败，请联系管理员。"),
    AI_SERVICE_RATE_LIMIT(10005, "请求过于频繁，请稍后再试。"),
    AI_SERVICE_ERROR(10006, "AI 服务调用失败，请稍后重试。"),

    // ========== 工具调用相关 ==========
    TOOL_INVOKE_FAILED(20001, "工具调用失败，请稍后重试。"),
    TOOL_NOT_FOUND(20002, "找不到对应的工具。"),

    // ========== 记忆存储相关 ==========
    MEMORY_STORAGE_ERROR(30001, "会话记忆读写失败，请稍后重试。"),

    // ========== 参数校验相关 ==========
    PARAM_INVALID(40001, "请求参数不合法。"),
    PARAM_MESSAGE_EMPTY(40002, "消息内容不能为空。"),
    PARAM_MESSAGE_TOO_LONG(40003, "消息内容过长，请控制在 2000 字以内。"),

    // ========== 通用 ==========
    INTERNAL_ERROR(50000, "服务暂时不可用，请稍后再试。");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
