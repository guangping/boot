package io.lance.common.core.bean;

import java.io.Serializable;

/**
 * Author Lance.
 * Date: 2017-09-05 17:08
 * Desc: 返回值规范
 */
public class JsonResult<T> implements Serializable {

    private Integer code = 0;

    private String message;

    private T data;

    private String success = "1";


    public JsonResult() {
    }

    public JsonResult(String message) {
        this.message = message;
    }

    public JsonResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResult(Integer code, String message, T data, String success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
