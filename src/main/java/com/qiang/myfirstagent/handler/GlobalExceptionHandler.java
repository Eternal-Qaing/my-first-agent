package com.qiang.myfirstagent.handler;

import com.qiang.myfirstagent.common.ErrorCode;
import com.qiang.myfirstagent.common.ErrorResponse;
import com.qiang.myfirstagent.handler.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author qiang
 * @since 2026/10/10 23:10
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 1.处理自定义业务异常（优先级最高）
     */
    public ResponseEntity<ErrorResponse> handleBizException(BizException e, HttpServletRequest request) {
        log.warn("业务异常 | code={} | msg={} | path={}",e.getErrorCode().getCode(), e.getErrorCode().getMessage(), request.getRequestURI());
        return buildResponse(e.getErrorCode(), request);
    }
    /**
     * 2.处理参数校验异常（@Valid 触发）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String detail = fieldError != null ? fieldError.getDefaultMessage() : ErrorCode.PARAM_INVALID.getMessage();
        log.warn("参数校验失败 | detail={} | path={}", detail, request.getRequestURI());
        ErrorResponse body = new ErrorResponse(
                ErrorCode.PARAM_INVALID.getCode(),
                detail,
                LocalDate.now().toString(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    /**
     * 3.处理缺少请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(
            MissingServletRequestParameterException e, HttpServletRequest request
    ){
        log.warn("缺少请求参数 | param={} | path={}", e.getParameterName(), request.getRequestURI());
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.PARAM_INVALID.getCode(),
                "缺少必要参数：" + e.getParameterName(),
                LocalDateTime.now().toString(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    /**
     * 4. 处理底层 AI 服务抛出的异常（Spring AI 包装的异常）
     *    这是兜底逻辑：解析底层异常信息，映射到我们自己的错误码
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e, HttpServletRequest request) {
        // 打印完整堆栈，方便排查（生产环境通常用 log.error，不用 printStackTrace）
        log.error("未捕获异常 | path={} | msg={}", request.getRequestURI(), e.getMessage(), e);

        ErrorCode errorCode = mapToErrorCode(e);
        return buildResponse(errorCode, request);
    }

    /**
     * 底层异常 -> 业务错误码 的映射
     * Spring AI 最终会把底层 HTTP 错误包装成 RuntimeException，这里按关键字识别
     */
    private ErrorCode mapToErrorCode(Exception e) {
        String msg = e.getMessage();
        if (msg == null) {
            return ErrorCode.INTERNAL_ERROR;
        }
        String lower = msg.toLowerCase();
        if (lower.contains("insufficient balance") || lower.contains("402")) {
            return ErrorCode.AI_SERVICE_INSUFFICIENT_BALANCE;
        }
        if (lower.contains("401") || lower.contains("unauthorized") || lower.contains("invalid api key")) {
            return ErrorCode.AI_SERVICE_UNAUTHORIZED;
        }
        if (lower.contains("rate limit") || lower.contains("429")) {
            return ErrorCode.AI_SERVICE_RATE_LIMIT;
        }
        if (lower.contains("timeout") || lower.contains("timed out")) {
            return ErrorCode.AI_SERVICE_TIMEOUT;
        }
        if (lower.contains("redis") || lower.contains("connection refused")) {
            return ErrorCode.MEMORY_STORAGE_ERROR;
        }
        return ErrorCode.INTERNAL_ERROR;
    }

    private ResponseEntity<ErrorResponse> buildResponse(ErrorCode errorCode, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                LocalDateTime.now().toString(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
