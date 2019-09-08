package cn.van.redis.idempotency.expction;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ServiceException
 *
 * @author: Van
 * Date:     2019-09-03 00:15
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public class ServiceException extends RuntimeException{

    private String code;
    private String msg;

    public ServiceException() {
    }

    public ServiceException(String msg) {
        this.msg = msg;
    }

    public ServiceException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

