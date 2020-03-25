package cn.van.parameter.validator.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @公众号： 风尘博客
 * @Classname HttpResult
 * @Description 统一结果集
 * @Date 2019/9/09 9:32 下午
 * @Author by Van
 */
@Data
public class HttpResult<T> implements Serializable {

    /**
     * 成功标示
     */
    private boolean success;
    /**
     * 返回状态码
     */
    private int code;
    /**
     * 返回对象
     */
    private T data;
    /**
     * 返回错误信息
     */
    private String message;

    // 构造器开始
    /**
     * 无参构造器
     */
    public HttpResult() {
        this.code = 200;
        this.success = true;
    }
    /**
     * 返回成功的实体
     * @param obj
     */
    public HttpResult(T obj) {
        this.code = 200;
        this.data = obj;
        this.success = true;
    }

    /**
     * 返回错误信息
     * @param code
     * @param message
     */
    public HttpResult(int code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }
    // 构造器结束

    /**
     * 成功直接返回数据和状态
     * @param data
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> success(T data){
        return new HttpResult(data);
    }

    /**
     *
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> success(){
        return new HttpResult();
    }
    /**
     * 失败的时候调用
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> failure(int code, String msg){
        return  new HttpResult(code,msg);
    }

}
