package com.orikik.clientmanager.config;

import com.orikik.clientmanager.dto.ErrorDto;
import com.orikik.clientmanager.exception.ClientManagerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleConflict(ClientManagerException exp) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(exp.getErrorCode());
        errorDto.setMessage(exp.getMessage());
        return errorDto;
    }
}
