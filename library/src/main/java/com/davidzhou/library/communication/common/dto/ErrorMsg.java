package com.davidzhou.library.communication.common.dto;

/**
 * Created by David on 2018/2/27 0027.
 */

public class ErrorMsg {
    private String errorMsg;
    private int errorCode;
    private Exception exception;
    public ErrorMsg() {
        errorCode = -1;
        errorMsg = null;
        exception = null;
    }
    public boolean hasErrorCode() {
        return errorCode != -1;
    }
    public boolean hasErrorMsg() {
        return errorMsg != null;
    }
    public boolean hasException() {
        return exception != null;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
