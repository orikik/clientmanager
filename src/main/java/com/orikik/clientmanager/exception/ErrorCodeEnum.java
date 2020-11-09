package com.orikik.clientmanager.exception;

public enum ErrorCodeEnum {
    OPERATION_IS_PROHIBITED("OPERATION_IS_PROHIBITED"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    ;

    private String message;

    ErrorCodeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
