package com.eaosoft.railway.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @Author: ZhouWenTao
 * @Date: 2021/3/24 14:00
 */


public class Result<T> implements Serializable {

    public static final long serialVersionUID = 1L;

    public boolean success = true;

    public String info = "success！";

    public Integer code = 0;

    public T data;

    public long timestamp = System.currentTimeMillis();

    public Result() {

    }

    public Result<T> success(String message) {
        this.info = message;
        this.code = 0;
        this.success = true;
        return this;
    }

    @Deprecated
    public static Result<Object> ok() {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(0);
        r.setInfo("success");
        return r;
    }

    @Deprecated
    public static Result<Object> ok(String msg) {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(0);
        r.setInfo(msg);
        return r;
    }

    @Deprecated
    public static Result<Object> ok(Object data) {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(0);
        r.setData(data);
        return r;
    }

    public static<T> Result<T> OK() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(0);
        r.setInfo("success");
        return r;
    }

    public static<T> Result<T> OK(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(0);
        r.setData(data);
        return r;
    }

    public static<T> Result<T> OK(String msg, T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(0);
        r.setInfo(msg);
        r.setData(data);
        return r;
    }

    public static Result<Object> error(String msg) {
        return error(500, msg);
    }


    public static Result<Object> error(int code, String msg) {
        Result<Object> r = new Result<Object>();
        r.setCode(code);
        r.setInfo(msg);
        r.setSuccess(false);
        return r;
    }

    public Result<T> error500(String message) {
        this.info = message;
        this.code = 500;
        this.success = false;
        return this;
    }
    /**
     * noauth
     */
    public static Result<Object> noauth(String msg) {
        return error(510, msg);
    }

    @JsonIgnore
    private String onlTable;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOnlTable() {
        return onlTable;
    }

    public void setOnlTable(String onlTable) {
        this.onlTable = onlTable;
    }
}