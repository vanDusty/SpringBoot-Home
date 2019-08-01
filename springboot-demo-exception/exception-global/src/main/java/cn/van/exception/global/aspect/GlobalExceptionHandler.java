package cn.van.exception.global.aspect;

import cn.van.exception.global.exception.BizException;
import cn.van.exception.global.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: GlobalExceptionHandler
 *
 * @author: Van
 * Date:     2019-08-01 15:29
 * Description: 用于做全局异常处理的异常切面
 * Version： V1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 重写handleExceptionInternal，自定义处理过程
     **/
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //这里将异常直接传给handlerException()方法进行处理，返回值为OK保证友好的返回，而不是出现500错误码。
        return new ResponseEntity(handlerException(ex), HttpStatus.OK);
    }
    /**
     * 异常捕获
     * @param e 捕获的异常
     * @return 封装的返回对象
     **/
    @ExceptionHandler(Exception.class)
    public HttpResult handlerException(Throwable e) {
        HttpResult httpResult;
        String msg = null;

        // 自定义异常
        if (e instanceof BizException) {
            msg = ((BizException) e).getMsg();
            log.error("发生业务异常！原因是：{}",msg);
        }else {
            // 其他异常，当我们定义了多个异常时，这里可以增加判断和记录
            msg = e.getMessage();
            log.error("异常的原因是：{}",msg);
        }
        httpResult = HttpResult.failure(msg);

        return httpResult;
    }
}
