package io.lance.common.core.exception;

/**
 * Author Lance.
 * Date: 2017-09-04 17:40
 * Desc: 框架异常
 */
public class EbsException extends RuntimeException {

    //异常编码
    private Integer code;

    //异常信息
    private String msg;

    public EbsException() {
    }

    public EbsException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public EbsException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public EbsException(String message, Integer code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }

    public EbsException(Throwable cause, Integer code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public EbsException(Throwable cause){
        super(cause);
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
