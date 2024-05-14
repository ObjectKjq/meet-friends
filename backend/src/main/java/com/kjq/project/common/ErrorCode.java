package com.kjq.project.common;

/**
 * 全局错误码
 */
public enum ErrorCode {

    SUCCESS(20000, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求参数为空", ""),
    NOT_LOGIN(40100, "没有登陆", ""),
    NOT_AUTH(40101, "没有权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    FILE_TYPE_ERROR(40002, "不支持的文件", "");

    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
