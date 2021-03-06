package cn.van.springboot.validator.web.expection;

import cn.van.springboot.validator.domain.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;

/**
 * @公众号： 风尘博客
 * @Classname WebExceptionHandler
 * @Description 统一异常捕获
 * @Date 2019/9/09 9:32 下午
 * @Author by Van
 */
@RestControllerAdvice
@Slf4j
public class WebExceptionHandler {

    /**
     * 方法参数校验
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public HttpResult handleMethodArgumentNotValidException(BindException e) {
        log.error(e.getMessage(), e);
        return HttpResult.failure(400,e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 参数校验注解错误抛出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedTypeException.class)
    public HttpResult handleMethodArgumentNotValidException(UnexpectedTypeException e) {
        log.error(e.getMessage(), e);
        return HttpResult.failure(400,"检验参数注解错误！");
    }

    @ExceptionHandler(Exception.class)
    public HttpResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return HttpResult.failure(400, "系统繁忙,请稍后再试");
    }
}
