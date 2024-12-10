package com.group.chatSystem.exception.handler;

import com.group.chatSystem.exception.NotFoundRefreshTokenException;
import com.group.chatSystem.web.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.group.chatSystem.exception.LoggingUtil.warningLogging;

@Slf4j
@Order(GlobalRestExceptionHandler.ORDER)
@RestControllerAdvice(annotations = RestController.class)
public class GlobalRestExceptionHandler {

    public static final int ORDER = 0;

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundRefreshTokenException.class)
    public CommonResponse handleNotFoundRefreshTokenException(HttpServletRequest req, NotFoundRefreshTokenException e) {
        warningLogging(req, e, false);
        return CommonResponse.createErrorResponse(e);
    }

}