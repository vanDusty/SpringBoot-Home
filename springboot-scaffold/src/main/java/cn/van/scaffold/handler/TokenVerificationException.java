package cn.van.scaffold.handler;

/**
 * @公众号： 风尘博客
 * @Classname BizException
 * @Description 自定义异常类
 * @Date 2019/3/19 11:32 下午
 * @Author by Van
 */
public class TokenVerificationException extends RuntimeException {

    /**
     * 错误码
     */
    protected Integer code;

    protected String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 有参构造器，返回码在枚举类中，这里可以指定错误信息
     * @param msg
     */
    public TokenVerificationException(String msg) {
        super(msg);
    }
}
