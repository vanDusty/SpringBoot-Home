# SpringBoot： 全局异常的处理

`@ExceptionHandler` 可以处理异常, 但是仅限于当前`Controller`中处理异常；`@ControllerAdvice`可以配置`basePackage`下的所有`Controller`。结合两者使用,就可以处理全局的异常了。


## 一、开发准备

### 1.1 异常

一般，我们可以把异常分为两种，第一种是请求到达`Controller`层之前，第二种是到达`Controller`层之后项目代码中发生的错误。而第一种又可以分为两种错误类型：

1. 路径错误；
2. 类似于请求方式错误，参数类型不对等类似错误。


### 1.2 项目依赖

```pom
<!-- web 包，默认就内嵌了Tomcat 容器-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>1.8.4</scope>
</dependency>
```

## 二、 项目Demo

### 2.1 结果封装

> 为了保证幂等性，定义了统一返回的类`HttpResult`，以及一个记录错误返回码和错误信息的枚举类`ResultEnum`。
> 
 
* `HttpResult.java`

```java
@Data
public class HttpResult {

    /**
     * 响应状态
     */
    protected boolean status;

    /**
     * 响应代码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private Object data;

    public HttpResult() {
    	this.status = true;
    }


    /**
     * 成功
     * @param data
     * @return
     */
    public static HttpResult success(Object data) {
        HttpResult httpResult = new HttpResult();
        httpResult.setStatus(true);
        httpResult.setCode(ResultEnum.SUCCESS.getCode());
        httpResult.setMessage(ResultEnum.SUCCESS.getMessage());
        httpResult.setData(data);
        return httpResult;
    }

    public static  HttpResult success(Object data, String msg) {
        HttpResult httpResult = new HttpResult();
        httpResult.setStatus(true);
        httpResult.setCode(ResultEnum.SUCCESS.getCode());
        httpResult.setMessage(msg);
        httpResult.setData(data);
        return httpResult;
    }
    /**
     * 失败
     */
    public static HttpResult failure() {
        HttpResult httpResult = new HttpResult();
        httpResult.setStatus(false);
        httpResult.setCode(ResultEnum.DEFAULT.getCode());
        return httpResult;
    }

    public static  HttpResult failure(String msg) {
        HttpResult httpResult = new HttpResult();
        httpResult.setStatus(false);
        httpResult.setCode(ResultEnum.DEFAULT.getCode());
        httpResult.setMessage(msg);
        return httpResult;
    }

    public static  HttpResult failure(int code, String msg) {
        HttpResult result = new HttpResult();
        result.setStatus(false);
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
}
```

* `ResultEnum.java`

```java
public enum ResultEnum {

    SUCCESS(200, "成功!"),

    // 数据操作错误定义,
    DEFAULT(-1, "一般错误"),
    BODY_NOT_MATCH(400,"请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH(401,"请求的数字签名不匹配!"),
    NOT_FOUND(404, "未找到该资源!"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
    SERVER_BUSY(503,"服务器正忙，请稍后再试!")
    ;

    /** 错误码 */
    private Integer code;

    /** 错误描述 */
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
```

### 2.2 自定义异常类

自定义一个异常类，用于处理我们发生的业务异常。

```java
@Data
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误码
	 */
	protected Integer code;
	/**
	 * 错误信息
	 */
	protected String msg;

	public BizException() {
		super();
	}

	
	public BizException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public BizException(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public BizException(Integer code, String msg, Throwable cause) {
		super(msg, cause);
		this.code = code;
		this.msg = msg;
	}

}
```

### 2.3 自定义全局异常处理类

```
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
```

1. `@RestControllerAdvice`和`@ExceptionHandler`会捕获所有Rest接口的异常并封装成我们定义的`HttpResult`的结果集返回，但是：**处理不了拦截器里的异常**；
1. 这里，为了测试，我写了一个简单的用户类`User.java`和`UserController.java`。

## 三、 测试

* `User.java`

```java
@Data
public class User {
    /** 编号*/
    private Long id;
    /** 姓名*/
    private String name;
    /** 年龄*/
    private Integer age;
}
```

* `UserController.java`


```java
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 无异常的请求
     * @return
     */
    @GetMapping("/list")
    public HttpResult list() {
        System.out.println("开始查询...");
        List<User> userList =new ArrayList();
        User user=new User();
        user.setId(1L);
        user.setName("Van");
        user.setAge(18);
        userList.add(user);
        return HttpResult.success(userList);
    }

    /**
     * 模拟自定义异常
     * @param user
     * @return
     */
    @PostMapping("/insert")
    public HttpResult insert(@RequestBody User user) {
        System.out.println("开始新增...");
        //如果姓名为空就手动抛出一个自定义的异常！
        if(user.getName()==null){
            throw  new BizException(-1,"用户姓名不能为空！");
        }
        return new HttpResult();
    }

    /**
     * 这里故意发送post请求，模拟请求方式错误
     * @param user
     * @return
     */
    @PutMapping("/update")
    public HttpResult update(@RequestBody User user) {
        System.out.println("开始更新...");
        return new HttpResult();
    }

    /**
     * 模拟其他异常
     * @param id
     * @return
     */
    @DeleteMapping("/deleteById")
    public HttpResult deleteById(Long id)  {
        System.out.println("开始删除...");
        //这里故意造成一个异常，并且不进行处理
        Integer.parseInt("abc123");
        return new HttpResult();
    }
}
```

> 启动`Application.java`进行测试：

### 3.1 无异常的请求

* Postman GET请求 地址：[http://localhost:8080/user/list](http://localhost:8080/user/list)
* 返回结果：

```
{
    "status": true,
    "code": 200,
    "message": "成功!",
    "data": [
        {
            "id": 1,
            "name": "Van",
            "age": 18
        }
    ]
}
```

### 3.2 自定义异常

* Postman POST请求 地址：[http://localhost:8080/user/insert](http://localhost:8080/user/insert)

* 请求参数：

```
{
	"id":1,"age":18
}
```

* 返回结果：

```
{
    "status": false,
    "code": -1,
    "message": "用户姓名不能为空！",
    "data": null
}
```

### 3.3 模拟请求方式错误

> 根据代码可以看到，这里本该是PUT 请求，我故意发送post请求

1. Postman POST请求 地址：[http://localhost:8080/user/update](http://localhost:8080/user/update)

* 请求参数：

```
{
	"id":1,"age":20
}
```

* 返回结果：

```
{
    "status": false,
    "code": -1,
    "message": "Request method 'POST' not supported",
    "data": null
}
```

### 3.4 其他异常

1. Postman DELETE请求 地址：[http://localhost:8080/user/deleteById](http://localhost:8080/user/deleteById)

* 请求参数：

```
{
	"id":1
}
```

* 返回结果：

```
{
    "status": false,
    "code": -1,
    "message": "For input string: \"abc123\"",
    "data": null
}
```

## 四 其他

> 关于SpringBoot优雅的全局异常处理的文章就讲解到这里了，如有不妥，欢迎指正！
