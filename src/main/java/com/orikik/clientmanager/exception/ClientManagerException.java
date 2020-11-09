package com.orikik.clientmanager.exception;

public class ClientManagerException extends RuntimeException {
    private ErrorCodeEnum errorCode;
    private String errorMessage;

    public ClientManagerException(ErrorCodeEnum errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ClientManagerException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
    }

    public String getErrorCode() {
        return errorCode.name();
    }

    public void setErrorCode(ErrorCodeEnum errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
