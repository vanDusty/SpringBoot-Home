package cn.van.annotation.multipleDataSource.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DS {
//  该值是为了在切面中判断你是读操作还是写操作
    String value() default "";
}
