package com.example.pcmallcommon.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseResult<T> {
    private ResponseStatus status;
    private T data;

    public static <T> ResponseResult<T> ok(T data) {
        return new ResponseResult<>(ResponseStatus.OK, data);
    }

    public static ResponseResult<String> error(ResponseStatus status) {
        return new ResponseResult<>(status, status.getMessage());
    }

    public static ResponseResult<String> error(ResponseStatus status, String msg) {
        return new ResponseResult<>(status, msg);
    }
}
