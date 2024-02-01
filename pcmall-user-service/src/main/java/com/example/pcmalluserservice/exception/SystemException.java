package com.example.pcmalluserservice.exception;

import com.example.pcmallcommon.response.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemException extends RuntimeException {
    private ResponseStatus responseStatus;

    public SystemException(ResponseStatus responseStatus) {
        super(responseStatus.toString());
        this.responseStatus = responseStatus;
    }

}
