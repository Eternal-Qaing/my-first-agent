package com.qiang.myfirstagent.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author qiang
 * @since 2026/10/10 23:11
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    //业务错误码
    private int code;
    //用户可读的提示
    private String message;
    //发生时间
    private String timestamp;
    //请求路径
    private String path;
}
