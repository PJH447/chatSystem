package com.group.chatSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "중복될 수 없습니다.")
public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super(message);
    }

}
