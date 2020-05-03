package cn.van.order.result;

/**
 * @公众号： 风尘博客
 * @Classname ResultCodeEnum
 * @Description 结果类枚举
 * @Date 2019/3/19 5:55 下午
 * @Author by Van
 */
public enum ResultCodeEnum {
    /*** 通用部分 100 - 599***/
    // 成功请求
    SUCCESS(200, "successful"),

    /*** 这里可以根据不同模块用不同的区级分开错误码，例如:  ***/

    // 1000～1999 区间表示用户模块错误
    // 2000～2999 区间表示订单模块错误
    // 3000～3999 区间表示商品模块错误
    // 。。。

    ORDER_NOT_FOUND(2000, "order not found"),
    ;
    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String message;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
