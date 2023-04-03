package com.eaosoft.exception;

/**
 * @Author ZhouWenTao
 * @Date 2022/7/14 15:05
 * @Version 1.0
 */
public class OneException extends RuntimeException{
    private Integer code;
    private String errorMessage;

    public OneException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
