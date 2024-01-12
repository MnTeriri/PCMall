package com.example.pcmallcommon.response;

public enum ResponseStatus {
    OK(200, "操作成功"),
    INTERNAL_ERROR(500000, "系统错误"),
    BUSINESS_ERROR(500001, "业务错误"),
    LOGIN_ERROR(500002, "账号或密码错误"),
    NO_DATA_ERROR(500003, "没有找到数据"),
    PARAM_ERROR(500004, "参数格式错误"),
    AUTH_ERROR(401, "没有权限,需要登录");

    private Integer code;
    private String message;

    ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
