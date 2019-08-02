# SpringBoot 处理跨域请求

## 一、跨域背景

### 1.1 何为跨域？

`Url`的一般格式： 

`协议 + 域名（子域名 + 主域名） + 端口号 + 资源地址`

示例：

`https://www.dustyblog.cn:8080/say/Hello` 是由

`https` + `www` + `dustyblog.cn` + `8080` + `say/Hello` 
组成。

> 只要协议，子域名，主域名，端口号这四项组成部分中有一项不同，就可以认为是不同的域，不同的域之间互相访问资源，就被称之为跨域。

### 1.2 一次正常的请求

* Controller层代码：

```java
@RequestMapping("/demo")
@RestController
public class CorsTestController {

    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello world !";
    }
}
```
* 启动项目，测试请求

浏览器打开[localhost:8080/demo/sayHello](localhost:8080/demo/sayHello)

可以打印出“hello world”

### 1.3 跨域测试

> 以Chrome为例：

* 打开任意网站，如：[https://blog.csdn.net/weixin_42036952/article/details/87983032](https://blog.csdn.net/weixin_42036952/article/details/87983032)

* 按F12，打开【开发者工具】，在里面的【Console】可以直接输入js代码测试；

```js
var token= "LtSFVqKxvpS1nPARxS2lpUs2Q2IpGstidMrS8zMhNV3rT7RKnhLN6d2FFirkVEzVIeexgEHgI/PtnynGqjZlyGkJa4+zYIXxtDMoK/N+AB6wtsskYXereH3AR8kWErwIRvx+UOFveH3dgmdw1347SYjbL/ilGKX5xkoZCbfb1f0=,LZkg22zbNsUoHAgAUapeBn541X5OHUK7rLVNHsHWDM/BA4DCIP1f/3Bnu4GAElQU6cds/0fg9Li5cSPHe8pyhr1Ii/TNcUYxqHMf9bHyD6ugwOFTfvlmtp6RDopVrpG24RSjJbWy2kUOOjjk5uv6FUTmbrSTVoBEzAXYKZMM2m4=,R4QeD2psvrTr8tkBTjnnfUBw+YR4di+GToGjWYeR7qZk9hldUVLlZUsEEPWjtBpz+UURVmplIn5WM9Ge29ft5aS4oKDdPlIH8kWNIs9Y3r9TgH3MnSUTGrgayaNniY9Ji5wNZiZ9cE2CFzlxoyuZxOcSVfOxUw70ty0ukLVM/78=";
var xhr = new XMLHttpRequest();
xhr.open('GET', 'http://127.0.0.1:8080/demo/sayHello');
xhr.setRequestHeader("x-access-token",token);
xhr.send(null);
xhr.onload = function(e) {
    var xhr = e.target;
    console.log(xhr.responseText);
}
```

* 输入完后直接按回车键就可以返回结果:

```js
Access to XMLHttpRequest at 'http://127.0.0.1:8080/demo/sayHello' 
from origin 'https://blog.csdn.net' has been blocked by CORS policy: 
No 'Access-Control-Allow-Origin' header is present on the requested resource.
``` 

该结果表明：该请求在`https://blog.csdn.net`域名下请求失败！

## 二、解决方案 - Cors跨域

### 2.1 Cors是什么

> CORS全称为Cross Origin Resource Sharing（跨域资源共享）, 每一个页面需要返回一个名为`Access-Control-Allow-Origin`的http头来允许外域的站点访问，你可以仅仅暴露有限的资源和有限的外域站点访问。

我们可以理解为：如果一个请求需要允许跨域访问，则需要在http头中设置`Access-Control-Allow-Origin`来决定需要允许哪些站点来访问。如假设需要允许[www.dustyblog.c](www.dustyblog.cn)这个站点的请求跨域，则可以设置：

`Access-Control-Allow-Origin:http://www.dustyblog.cn。`

### 2.2 方案一：使用`@CrossOrigin`注解

#### 2.2.1 在Controller上使用`@CrossOrigin`注解

> 该类下的所有接口都可以通过跨域访问

```java
@RequestMapping("/demo2")
@RestController
//@CrossOrigin //所有域名均可访问该类下所有接口
@CrossOrigin("https://blog.csdn.net") // 只有指定域名可以访问该类下所有接口
public class CorsTest2Controller {

    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello world --- 2";
    }
}
```

这里指定当前的CorsTest2Controller中所有的方法可以处理https://csdn.net域上的请求,这里可以测试一下：

* 在[https://blog.csdn.net/weixin_42036952/article/details/87983032](https://blog.csdn.net/weixin_42036952/article/details/87983032)页面打开调试窗口，输入(注意：这里请求地址是`/demo2`，请区别于1.2 案例中的`/demo`)


```js
var token= "LtSFVqKxvpS1nPARxS2lpUs2Q2IpGstidMrS8zMhNV3rT7RKnhLN6d2FFirkVEzVIeexgEHgI/PtnynGqjZlyGkJa4+zYIXxtDMoK/N+AB6wtsskYXereH3AR8kWErwIRvx+UOFveH3dgmdw1347SYjbL/ilGKX5xkoZCbfb1f0=,LZkg22zbNsUoHAgAUapeBn541X5OHUK7rLVNHsHWDM/BA4DCIP1f/3Bnu4GAElQU6cds/0fg9Li5cSPHe8pyhr1Ii/TNcUYxqHMf9bHyD6ugwOFTfvlmtp6RDopVrpG24RSjJbWy2kUOOjjk5uv6FUTmbrSTVoBEzAXYKZMM2m4=,R4QeD2psvrTr8tkBTjnnfUBw+YR4di+GToGjWYeR7qZk9hldUVLlZUsEEPWjtBpz+UURVmplIn5WM9Ge29ft5aS4oKDdPlIH8kWNIs9Y3r9TgH3MnSUTGrgayaNniY9Ji5wNZiZ9cE2CFzlxoyuZxOcSVfOxUw70ty0ukLVM/78=";
var xhr = new XMLHttpRequest();
xhr.open('GET', 'http://127.0.0.1:8080/demo2/sayHello');
xhr.setRequestHeader("x-access-token",token);
xhr.send(null);
xhr.onload = function(e) {
    var xhr = e.target;
    console.log(xhr.responseText);
}
```

返回结果：
```js
ƒ (e) {
    var xhr = e.target;
    console.log(xhr.responseText);
}
VM156:8 hello world --- 2
```

说明跨域成功！

* 换个域名测试一下看跨域是否还有效，在[https://www.baidu.com](https://www.baidu.com)按照上述方法测试一下，返回结果：

```js
OPTIONS http://127.0.0.1:8080/demo2/sayHello 403
(anonymous)
Access to XMLHttpRequest at 'http://127.0.0.1:8080/demo2/sayHello' 
from origin 'http://www.cnblogs.com' has been blocked by CORS policy: 
Response to preflight request doesn't pass access control check: 
No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

说明跨域失败！证明该方案成功！


### 2.3 方案二：CORS全局配置-实现`WebMvcConfigurer`

* 新建跨域配置类：`CorsConfig.class`:

```java
/**
 * 跨域配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").
                        allowedOrigins("https://www.dustyblog.cn"). //允许跨域的域名，可以用*表示允许任何域名使用
                        allowedMethods("*"). //允许任何方法（post、get等）
		                allowedHeaders("*"). //允许任何请求头
                        allowCredentials(true). //带上cookie信息
                        exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L); //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
            }
        };
    }
}
```

* 测试，在允许访问的域名[https://www.dustyblog.cn/](https://www.dustyblog.cn/)控制台输入(注意，这里请求的是http://127.0.0.1:8080/demo3)：

```js
var token= "LtSFVqKxvpS1nPARxS2lpUs2Q2IpGstidMrS8zMhNV3rT7RKnhLN6d2FFirkVEzVIeexgEHgI/PtnynGqjZlyGkJa4+zYIXxtDMoK/N+AB6wtsskYXereH3AR8kWErwIRvx+UOFveH3dgmdw1347SYjbL/ilGKX5xkoZCbfb1f0=,LZkg22zbNsUoHAgAUapeBn541X5OHUK7rLVNHsHWDM/BA4DCIP1f/3Bnu4GAElQU6cds/0fg9Li5cSPHe8pyhr1Ii/TNcUYxqHMf9bHyD6ugwOFTfvlmtp6RDopVrpG24RSjJbWy2kUOOjjk5uv6FUTmbrSTVoBEzAXYKZMM2m4=,R4QeD2psvrTr8tkBTjnnfUBw+YR4di+GToGjWYeR7qZk9hldUVLlZUsEEPWjtBpz+UURVmplIn5WM9Ge29ft5aS4oKDdPlIH8kWNIs9Y3r9TgH3MnSUTGrgayaNniY9Ji5wNZiZ9cE2CFzlxoyuZxOcSVfOxUw70ty0ukLVM/78=";
var xhr = new XMLHttpRequest();
xhr.open('GET', 'http://127.0.0.1:8080/demo3/sayHello');
xhr.setRequestHeader("x-access-token",token);
xhr.send(null);
xhr.onload = function(e) {
    var xhr = e.target;
    console.log(xhr.responseText);
}
```

输出结果

```js
ƒ (e) {
    var xhr = e.target;
    console.log(xhr.responseText);
}
VM433:8 hello world --- 3
```

说明跨域成功，换个网址如[https://www.baidu.com](https://www.baidu.com)测试依旧出现需要跨域的错误提示，证明该配置正确，该方案测试通过。

### 2.3 拦截器实现

通过实现`Fiter`接口在请求中添加一些`Header`来解决跨域的问题

```java
@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN");
        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
            response.getWriter().println("ok");
            return;
        }
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
```