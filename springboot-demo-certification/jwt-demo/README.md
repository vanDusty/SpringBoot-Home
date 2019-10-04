# Spring Boot认证：整合`Jwt`

## 背景

`Jwt`全称是：`json web token`。它将用户信息加密到`token`里，服务器不保存任何用户信息。服务器通过使用保存的密钥验证`token`的正确性，只要正确即通过验证。

### 优点

1. 简洁: 可以通过`URL`、`POST`参数或者在`HTTP header`发送，因为数据量小，传输速度也很快；
2. 自包含：负载中可以包含用户所需要的信息，避免了多次查询数据库；
3. 因为`Token`是以`JSON`加密的形式保存在客户端的，所以`JWT`是跨语言的，原则上任何`web`形式都支持；
4. 不需要在服务端保存会话信息，特别适用于分布式微服务。

### 缺点

1. 无法作废已颁布的令牌；
1. 不易应对数据过期。

## 一、`Jwt`消息构成

### 1.1 组成

一个`token`分3部分，按顺序为

1. 头部（`header`)
1. 载荷（`payload`)
1. 签证（`signature`)

三部分之间用`.`号做分隔。例如：

```xml
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxYzdiY2IzMS02ODFlLTRlZGYtYmU3Yy0wOTlkODAzM2VkY2UiLCJleHAiOjE1Njk3Mjc4OTF9.wweMzyB3tSQK34Jmez36MmC5xpUh15Ni3vOV_SGCzJ8
```

### 1.2 header

`Jwt`的头部承载两部分信息：

1. 声明类型，这里是`Jwt`
1. 声明加密的算法 通常直接使用 `HMAC SHA256`

`Jwt`里验证和签名使用的算法列表如下：

| JWS | 算法名称 |
| -- | -- | 
| HS256 | HMAC256 |
| HS384 | HMAC384 |
| HS512 | HMAC512 |
| RS256 | RSA256 |
| RS384 | RSA384 |
| RS512 | RSA512 |
| ES256 | ECDSA256 |
| ES384 | ECDSA384 |
| ES512 | ECDSA512 |


### 1.3 playload

载荷就是存放有效信息的地方。基本上填`2`种类型数据

1. 标准中注册的声明的数据；
1. 自定义数据。

由这2部分内部做`base64`加密。

- 标准中注册的声明 (建议但不强制使用)

```java
iss: jwt签发者
sub: jwt所面向的用户
aud: 接收jwt的一方
exp: jwt的过期时间，这个过期时间必须要大于签发时间
nbf: 定义在什么时间之前，该jwt都是不可用的.
iat: jwt的签发时间
jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
```

- 自定义数据:存放我们想放在`token`中存放的`key-value`值

### 1.4 signature

`Jwt`的第三部分是一个签证信息，这个签证信息由三部分组成
`base64`加密后的`header`和`base64`加密后的`payload`连接组成的字符串，然后通过`header`中声明的加密方式进行加盐`secret`组合加密，然后就构成了`Jwt`的第三部分。

## 二、`Spring Boot`和`Jwt`集成示例

示例代码采用：

```xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.8.1</version>
</dependency>
```

### 2.1 项目依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.9.1</version>
    </dependency>
    <dependency>
        <groupId>com.auth0</groupId>
        <artifactId>java-jwt</artifactId>
        <version>3.8.1</version>
    </dependency>
    <!-- redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <!-- lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>1.8.4</scope>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.47</version>
    </dependency>
</dependencies>
```

### 2.2 自定义注解`@JwtToken`

> 加上该注解的接口需要登录才能访问


```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtToken {
    boolean required() default true;
}
```

### 2.3 Jwt 认证工具类`JwtUtil.java`

> 主要用来生成签名、校验签名和通过签名获取信息

```java
public class JwtUtil {

    /**
     * 过期时间5分钟
     */
    private static final long EXPIRE_TIME = 5 * 60 * 1000;
    /**
     * jwt 密钥
     */
    private static final String SECRET = "jwt_secret";

    /**
     * 生成签名，五分钟后过期
     * @param userId
     * @return
     */
    public static String sign(String userId) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    // 将 user id 保存到 token 里面
                    .withAudience(userId)
                    // 五分钟后token过期
                    .withExpiresAt(date)
                    // token 的密钥
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据token获取userId
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            return userId;
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static boolean checkSign(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    // .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("token 无效，请重新获取");
        }
    }
}
```

### 2.4 拦截器拦截带有注解的接口


- `JwtInterceptor.java`

```java
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(JwtToken.class)) {
            JwtToken jwtToken = method.getAnnotation(JwtToken.class);
            if (jwtToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 userId
                String userId = JwtUtil.getUserId(token);
                System.out.println("用户id:" + userId);

                // 验证 token
                JwtUtil.checkSign(token);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
```

- 注册拦截器：`WebConfig.java`

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加jwt拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                // 拦截所有请求，通过判断是否有 @JwtToken 注解 决定是否需要登录
                .addPathPatterns("/**");
    }

    /**
     * jwt拦截器
     * @return
     */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
```

### 2.5 全局异常捕获

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", msg);
        return jsonObject;
    }
}
```

### 2.6 接口

- `JwtController.java `

```java
@RestController
@RequestMapping("/jwt")
public class JwtController {

    /**
     * 登录并获取token
     * @param userName
     * @param passWord
     * @return
     */
    @PostMapping("/login")
    public Object login( String userName, String passWord){
        JSONObject jsonObject=new JSONObject();
        // 检验用户是否存在(为了简单，这里假设用户存在，并制造一个uuid假设为用户id)
        String userId = UUID.randomUUID().toString();
        // 生成签名
        String token= JwtUtil.sign(userId);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("userName", userName);
        userInfo.put("passWord", passWord);
        jsonObject.put("token", token);
        jsonObject.put("user", userInfo);
        return jsonObject;
    }

    /**
     * 该接口需要带签名才能访问
     * @return
     */
    @JwtToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }
}
```

### 2.7 `Postman`测试接口

#### 2.7.1 在没`token`的情况下访问`jwt/getMessage`接口

- 请求方式：`GET`
- 请求参数：无
- 请求地址：[http://localhost:8080/jwt/getMessage](http://localhost:8080/jwt/getMessage)
- 返回结果：

```xml
{
    "message": "无token，请重新登录"
}
```

#### 2.7.2 先登录，在访问`jwt/getMessage`接口

- 登录请求及结果，详见下图：

![风尘博客](https://raw.githubusercontent.com/vanDusty/SpringBoot-Home/master/img/jwt_1.png)

登录后得到签名如箭头处

- 在请求头加上签名，然后再请求`jwt/getMessage`接口

![风尘博客](https://raw.githubusercontent.com/vanDusty/SpringBoot-Home/master/img/jwt_2.png)

请求通过，测试成功！

#### 2.7.3 过期后再次访问

> 我们设置的签名过期时间是五分钟，五分钟后再次访问`jwt/getMessage`接口,结果如下：

![风尘博客](https://raw.githubusercontent.com/vanDusty/SpringBoot-Home/master/img/jwt_3.png)

通过结果，我们发现时间到了，签名失效，说明该方案通过。


## 三、技术交流


1. [风尘博客](https://www.dustyblog.cn/)
1. [风尘博客-博客园](https://www.cnblogs.com/vandusty)
1. [风尘博客-CSDN](https://blog.csdn.net/weixin_42036952)
1. [风尘博客-掘金](https://juejin.im/userDO/5d5ea68e6fb9a06afa328f56)

关注公众号，了解更多：

![风尘博客](https://github.com/vanDusty/SpringBoot-Home/blob/master/dusty_blog.png?raw=true)