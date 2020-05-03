package cn.van.order.result;

import java.io.Serializable;

/**
 * @公众号： 风尘博客
 * @Classname ResultCodeEnum
 * @Description 封装 RPC 响应结果集
 * @Date 2019/3/19 7:55 下午
 * @Author by Van
 */
public class RpcResult <T> implements Serializable {

    /**
     * 是否响应成功
     */
    private Boolean success;
    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应数据
     */
    private T data;
    /**
     * 错误信息
     */
    private String message;

    // 构造器开始
    /**
     * 无参构造器(构造器私有，外部不可以直接创建)
     */
    private RpcResult() {
        this.code = 200;
        this.success = true;
    }
    /**
     * 有参构造器
     * @param obj
     */
    private RpcResult(T obj) {
        this.code = 200;
        this.data = obj;
        this.success = true;
    }

    /**
     * 有参构造器
     * @param resultCode
     */
    private RpcResult(ResultCodeEnum resultCode) {
        this.success = false;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
    // 构造器结束

    /**
     * 通用返回成功（没有返回结果）
     * @param <T>
     * @return
     */
    public static<T> RpcResult<T> success(){
        return new RpcResult();
    }

    /**
     * 返回成功（有返回结果）
     * @param data
     * @param <T>
     * @return
     */
    public static<T> RpcResult<T> success(T data){
        return new RpcResult<T>(data);
    }

    /**
     * 通用返回失败
     * @param resultCode
     * @param <T>
     * @return
     */
    public static<T> RpcResult<T> failure(ResultCodeEnum resultCode){
        return  new RpcResult<T>(resultCode);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RpcResult{" +
                "success=" + success +
                ", code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}