package com.qiang.myfirstagent.handler.exception;

import com.qiang.myfirstagent.common.ErrorCode;
import lombok.Getter;

/**
 *
 * @author qiang
 * @since 2026/10/10 23:11
 */
@Getter
public class BizException extends RuntimeException{
    private final ErrorCode errorCode;
    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BizException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
