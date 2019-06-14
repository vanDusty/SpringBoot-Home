package cn.van.annotation.multipleDataSource.aspect;

import cn.van.annotation.multipleDataSource.annotation.DS;
import cn.van.annotation.multipleDataSource.config.MultipleDBConfig;
import cn.van.annotation.multipleDataSource.config.MyDatasource.MyThreadLocal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DSAspect {
//    此处可以根据表达式进行匹配要进行切面操作的具体包下面的类，也可以根据自定义注解进行匹配
//    @Pointcut("@annotation(填某个注解的全限定名)")
    @Pointcut("execution(* cn.van.annotation.multipleDataSource.service.*.*(..))")
    public void pc(){};
    @Around("pc()")
    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Class<?> aClass = proceedingJoinPoint.getTarget().getClass();
        String name = proceedingJoinPoint.getSignature().getName();
        System.out.println("该方法的名字时"+name);
        Method method = aClass.getMethod(name);
        if(method.isAnnotationPresent(DS.class)){
            String value = method.getAnnotation(DS.class).value();
            System.out.println(value);
            if(value.equals("SLAVES")){
                System.out.println("我在SLAVES");
                MyThreadLocal.setLocal(MultipleDBConfig.SLAVES);
            }else{
                System.out.println("我在MASTER");
                MyThreadLocal.setLocal(MultipleDBConfig.MASTER);
            }
        }
        Object proceed = proceedingJoinPoint.proceed();
//        MyThreadLocal.remoceLocal();//每过一个切入点，就将本地线程变量中的key进去清空
        return proceed;
    }
}
