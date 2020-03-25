package cn.van.scaffold.web.config;

import cn.van.scaffold.enunm.ResultCodeEnum;
import cn.van.scaffold.handler.TokenVerificationException;
import cn.van.scaffold.util.HttpResult;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @公众号： 风尘博客
 * @Classname GlobalExceptionHandler
 * @Description 统一异常处理器
 * @Date 2019/3/19 11:40 下午
 * @Author by Van
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常捕获
     * @param e 捕获的异常
     * @return 封装的返回对象
     **/
    @ExceptionHandler(Exception.class)
    public HttpResult handlerException(Exception e) {
        ResultCodeEnum resultCodeEnum;
        // 自定义异常
        if (e instanceof TokenVerificationException) {
            resultCodeEnum = ResultCodeEnum.TOKEN_VERIFICATION_ERROR;
            resultCodeEnum.setMessage(getConstraintViolationErrMsg(e));
            log.error("tokenVerificationException：{}", resultCodeEnum.getMessage());
        }else {
            // 其他异常，当我们定义了多个异常时，这里可以增加判断和记录
            resultCodeEnum = ResultCodeEnum.SERVER_ERROR;
            resultCodeEnum.setMessage(e.getMessage());
            log.error("common exception:{}", JSON.toJSONString(e));
        }
        return HttpResult.failure(resultCodeEnum);
    }

    /**
     * 获取错误信息
     * @param ex
     * @return
     */
    private String getConstraintViolationErrMsg(Exception ex) {
        // validTest1.id: id必须为正数
        // validTest1.id: id必须为正数, validTest1.name: 长度必须在有效范围内
        String message = ex.getMessage();
        try {
            int startIdx = message.indexOf(": ");
            if (startIdx < 0) {
                startIdx = 0;
            }
            int endIdx = message.indexOf(", ");
            if (endIdx < 0) {
                endIdx = message.length();
            }
            message = message.substring(startIdx, endIdx);
            return message;
        } catch (Throwable throwable) {
            log.info("ex caught", throwable);
            return message;
        }
    }

}
