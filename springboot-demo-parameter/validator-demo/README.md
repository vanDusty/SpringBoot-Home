# Spring Boot 整合 Validator 参数校验

#### 背景

> 在开发中经常需要写一些字段校验的代码，比如非空，长度限制，邮箱格式验证等等，导致充满了`if-else` 的代码,不仅相当冗长,而且很让人抓狂。

`hibernate validator`（[官方文档](http://hibernate.org/validator/documentation/)）提供了一套比较完善、便捷的验证实现方式。它定义了很多常用的校验注解，我们可以直接将这些注解加在我们`JavaBean`的属性上面，就可以在需要校验的时候进行校验了。在`Spring Boot` 火热的现在，该工具已经包含在`spring-boot-starter-web`中，不需额外引入其他包。


### 一、快速入门


#### 1.1 声明要检查的参数

> 需要在`Controller`层的参数位置用`@Validated` 注解声明

```java
@RestController
@RequestMapping("/validator/demo")
public class ValidatorDemoController {

    /**
     * 注解参数校验案例
     * @param userDTO
     * @return
     */
    @PostMapping("/test")
    public HttpResult test(@Validated UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }
}
```

> 这里的`HttpResult ` 是Van 自己封装的一个结果集，详见文末Github地址的源码。

#### 1.2 对`UserDTO`中需要校验的参数添加注解


> 校验说明见代码中注释

```java
@Data
public class UserDTO {

    /**
     性别（不校验）
     */
    private String sex;

    /** 
     用户名（校验：不能为空，不能超过20个字符串）
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20个字符")
    private String userName;

    /** 
     * 手机号（校验：不能为空且按照正则校验格式）
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String mobile;

    /** 
     邮箱（校验：不能唯恐且校验邮箱格式）
     */
    @NotBlank(message = "联系邮箱不能为空")
    @Email(message = "邮箱格式不对")
    private String email;
}
```

#### 1.3 Web全局异常捕获

`@Valid` 在 `Spring Boot `中进行绑定参数校验时会抛出异常,需要在`Spring Boot `中处理。

```java
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

    @ExceptionHandler(Exception.class)
    public HttpResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return HttpResult.failure(400, "系统繁忙,请稍后再试");
    }
}
```
 
#### 1.4 测试接口

```java
@RestController
@RequestMapping("/demo")
public class ValidatorDemoController {

    /**
     * 注解参数校验案例
     * @param userDTO
     * @return
     */
    @PostMapping("/test")
    public HttpResult test(@Validated UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }

}
```

#### 1.5 测试

> 测试工具采用的`postman`

- 请求方式：POST
- 请求地址：localhost:8080/demo/test
- 请求参数：

```
userName:Van
mobile:17098705205
email:123
```
- 返回结果：

```
{
    "success": false,
    "code": 400,
    "data": null,
    "message": "邮箱格式不对"
}
```

1. 更多注解，请各位自行尝试；
1. 测试结果证明：参数校验生效，且按照我们设定的结果集返回异常信息。


#### 1.6 常见的校验注解

1. `@Null`：被注释的元素必须为 null     
1. `@NotNull`：被注释的元素必须不为 null     
1. `@AssertTrue`：被注释的元素必须为 true     
1. `@AssertFalse`：被注释的元素必须为 false     
1. `@Min(value)`：被注释的元素必须是一个数字，其值必须大于等于指定的最小值
1. `@Max(value)`：被注释的元素必须是一个数字，其值必须小于等于指定的最大值
1. `@DecimalMin(value)`：被注释的元素必须是一个数字，其值必须大于等于指定的最小值
1. `@DecimalMax(value)`：被注释的元素必须是一个数字，其值必须小于等于指定的最大值
1. `@Size(max=, min=)`：被注释的元素的大小必须在指定的范围内     
1. `@Digits (integer, fraction)`：被注释的元素必须是一个数字，其值必须在可接受的范围内
1. `@Past`：被注释的元素必须是一个过去的日期     
@Future`：被注释的元素必须是一个将来的日期     
1. `@Pattern(regex=,flag=)`：被注释的元素必须符合指定的正则表达式
1. `@NotBlank(message =)`：验证字符串非null，且长度必须大于0     
1. `@Email`：被注释的元素必须是电子邮箱地址     
1. `@Length(min=,max=)`：被注释的字符串的大小必须在指定的范围内     
1. `@NotEmpty`：被注释的字符串的必须非空     
1. `@Range(min=,max=,message=)`：被注释的元素必须在合适的范围内

### 二、自定义注解校验

`hibernate validator` 自带的注解可以搞定简单的参数校验，加上正则的写法，能解决绝大多数参数校验情况。但是，有些情况，比如：校验是否登录，就需要我们自定义注解校验了。为了方便测试，我这里以身份证校验为例完成自定义校验的过程。

#### 2.1 身份证校验工具类

```java
public class IdCardValidatorUtils {

    protected String codeAndCity[][] = {{"11", "北京"}, {"12", "天津"},
            {"13", "河北"}, {"14", "山西"}, {"15", "内蒙古"}, {"21", "辽宁"},
            {"22", "吉林"}, {"23", "黑龙江"}, {"31", "上海"}, {"32", "江苏"},
            {"33", "浙江"}, {"34", "安徽"}, {"35", "福建"}, {"36", "江西"},
            {"37", "山东"}, {"41", "河南"}, {"42", "湖北"}, {"43", "湖南"},
            {"44", "广东"}, {"45", "广西"}, {"46", "海南"}, {"50", "重庆"},
            {"51", "四川"}, {"52", "贵州"}, {"53", "云南"}, {"54", "西藏"},
            {"61", "陕西"}, {"62", "甘肃"}, {"63", "青海"}, {"64", "宁夏"},
            {"65", "新疆"}, {"71", "台湾"}, {"81", "香港"}, {"82", "澳门"},
            {"91", "国外"}};

    private String cityCode[] = {"11", "12", "13", "14", "15", "21", "22",
            "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
            "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
            "64", "65", "71", "81", "82", "91"};


    // 每位加权因子
    private static int power[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    // 第18位校检码
    private String verifyCode[] = {"1", "0", "X", "9", "8", "7", "6", "5",
            "4", "3", "2"};

    /**
     * 验证所有的身份证的合法性
     *
     * @param idcard
     * @return
     */
    public static boolean isValidatedAllIdcard(String idcard) {
        if (idcard.length() == 15) {
            idcard = convertIdcarBy15bit(idcard);
        }
        return isValidate18Idcard(idcard);
    }

    /**
     * 将15位的身份证转成18位身份证
     *
     * @param idcard
     * @return
     */
    public static String convertIdcarBy15bit(String idcard) {
        String idcard17 = null;
        // 非15位身份证
        if (idcard.length() != 15) {
            return null;
        }

        if (isDigital(idcard)) {
            // 获取出生年月日
            String birthday = idcard.substring(6, 12);
            Date birthdate = null;
            try {
                birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cday = Calendar.getInstance();
            cday.setTime(birthdate);
            String year = String.valueOf(cday.get(Calendar.YEAR));

            idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);

            char c[] = idcard17.toCharArray();
            String checkCode = "";

            if (null != c) {
                int bit[] = new int[idcard17.length()];

                // 将字符数组转为整型数组
                bit = converCharToInt(c);
                int sum17 = 0;
                sum17 = getPowerSum(bit);

                // 获取和值与11取模得到余数进行校验码
                checkCode = getCheckCodeBySum(sum17);
                // 获取不到校验位
                if (null == checkCode) {
                    return null;
                }

                // 将前17位与第18位校验码拼接
                idcard17 += checkCode;
            }
        } else { // 身份证包含数字
            return null;
        }
        return idcard17;
    }

    /**
     * @param idCard
     * @return
     */
    public static boolean isValidate18Idcard(String idCard) {
        // 非18位为假
        if (idCard.length() != 18) {
            return false;
        }
        // 获取前17位
        String idcard17 = idCard.substring(0, 17);
        // 获取第18位
        String idcard18Code = idCard.substring(17, 18);
        char c[] = null;
        String checkCode = "";
        // 是否都为数字
        if (isDigital(idcard17)) {
            c = idcard17.toCharArray();
        } else {
            return false;
        }

        if (null != c) {
            int bit[] = new int[idcard17.length()];
            bit = converCharToInt(c);
            int sum17 = 0;
            sum17 = getPowerSum(bit);

            // 将和值与11取模得到余数进行校验码判断
            checkCode = getCheckCodeBySum(sum17);
            if (null == checkCode) {
                return false;
            }
            // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
            if (!idcard18Code.equalsIgnoreCase(checkCode)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 18位身份证号码的基本数字和位数验校
     *
     * @param idCard
     * @return
     */
    public boolean is18Idcard(String idCard) {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$", idCard);
    }

    /**
     * 数字验证
     *
     * @param str
     * @return
     */
    public static boolean isDigital(String str) {
        return str == null || "".equals(str) ? false : str.matches("^[0-9]*$");
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param bit
     * @return
     */
    public static int getPowerSum(int[] bit) {

        int sum = 0;

        if (power.length != bit.length) {
            return sum;
        }

        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < power.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     *
     * @param sum17
     * @return 校验位
     */
    public static String getCheckCodeBySum(int sum17) {
        String checkCode = null;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
        }
        return checkCode;
    }

    /**
     * 将字符数组转为整型数组
     *
     * @param c
     * @return
     * @throws NumberFormatException
     */
    public static int[] converCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }


    public static void main(String[] args) {
        String idCardForFalse = "350583199108290106";
        String idCardForTrue = "350583197106150219";
        if (IdCardValidatorUtils.isValidatedAllIdcard(idCardForTrue)) {
            System.out.println("身份证校验正确");
        } else {
            System.out.println("身份证校验错误！");
        }
    }
}
```
#### 2.2 自定义注解


```java
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentityCardNumberValidator.class)
public @interface IdentityCardNumber {

    String message() default "身份证号码格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```
> 仔细的你会发现，相对于一般的自定义注解，该注解：
`@Constraint(validatedBy = IdentityCardNumberValidator.class)`,该注解的作用就是调用身份证校验的工具。

#### 2.3 在`UserDTO` 需要校验的字段添加声明

```java
/**
 * 身份证号（校验：自定义注解校验）
 */
@IdentityCardNumber
private String idNumber;
```

#### 2.4 控制层接口


```java
@RestController
@RequestMapping("/custom")
public class ValidatorCustomController {

    /**
     * 自定义注解参数校验案例
     * @param userDTO
     * @return
     */
    @PostMapping("/test")
    public HttpResult test(@Validated UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }

}
```

#### 2.5 自定义注解的测试

- 请求方式：POST
- 请求地址：localhost:8080/private/test
- 请求参数：

```xml
userName:Van
mobile:17098705205
email:110@qq.com
idNumber:350583199108290106
```
- 返回结果：

```java
{
    "success": false,
    "code": 400,
    "data": null,
    "message": "身份证号码格式不正确"
}
```

### 三、分组校验

除了上述的校验外，可能还有这种需求：

在创建用户信息时，不需要校验`userId`;但在更新用户信息时，需要校验`userId`,而用户名，邮箱等两种情况都得校验。这种情况，就可以**分组校验**来解决了。

#### 3.1  定义分组接口

- `Create.java`

```java
import javax.validation.groups.Default;

public interface Create extends Default {
}
```

- Update

```java
import javax.validation.groups.Default;

public interface Update extends Default {
}
```

#### 3.2 在`UserDTO` 需要校验的字段添加声明

```
/**
     * 用户id(只有在有Update分组中校验非空)
     */
    @NotNull(message = "id 不能为空", groups = Update.class)
    private Long userId;
```

#### 3.3 控制层入参位置进行声明

```java
@RestController
@RequestMapping("/groups")
public class ValidatorGroupsController {

    /**
     * 更新数据，需要传入userID
     * @param userDTO
     * @return
     */
    @PostMapping("/update")
    public HttpResult updateData(@Validated(Update.class)UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }
    /**
     * 新增数据，不需要传入userID
     * @param userDTO
     * @return
     */
    @PostMapping("/create")
    public HttpResult createData(@Validated(Create.class)UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }
}
```

#### 3.4 分组校验的测试-新增测试


- 请求方式：POST
- 请求地址：localhost:8080/groups/create
- 请求参数：

```xml
userName:Van
mobile:17098705205
email:110@qq.com
idNumber:350583197106150219
userId:
```
- 返回结果：

```java
{
    "success": true,
    "code": 200,
    "data": {
        "userId": null,
        "sex": null,
        "userName": "Van",
        "mobile": "17098705205",
        "email": "110@qq.com",
        "idNumber": "350583197106150219",
        "passWord": null
    },
    "message": null
}
```

请求成功，说明新增请求，不检验`userId `，即`userId `可以为空。

#### 3.5 分组校验的测试-更新测试


- 请求方式：POST
- 请求地址：localhost:8080/groups/update
- 请求参数：通上（3.4）
- 返回结果：

```java
{
    "success": false,
    "code": 400,
    "data": null,
    "message": "id 不能为空"
}
```

请求失败，说明更新请求，检验`userId `，即`userId `不能为空。

> 结合 3.4 与 3.5 的测试结果，说明分组校验成功。


### 三、总结

希望大家写的每一行代码都是业务需要，还不是无聊且无穷无尽的参数校验。

#### 技术交流

1. [风尘博客](https://www.dustyblog.cn/)
1. [风尘博客-博客园](https://www.cnblogs.com/vandusty)
1. [风尘博客-CSDN](https://blog.csdn.net/weixin_42036952)

关注公众号，了解更多：


![风尘博客](https://github.com/vanDusty/SpringBoot-Home/blob/master/dusty_blog.png?raw=true)