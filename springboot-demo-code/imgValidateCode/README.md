# Spring Boot 图片验证码及校验

### 一、思路

1. 后端生成数字和字母混搭的指定位数的验证码，存储在`Redis`中；
1. 将生成的验证码画成图片并转换成`base64`字符，和`Redis` 验证码的`key` 一块返给前端；
1. 前端登录时候，把验证码传给后端，后端	取出 `Redis`中的值进行对比。


#### 二、示例代码

#### 2.1 项目依赖和配置

- `pom.xml`

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    <!-- redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-pool2</artifactId>
        <version>2.0</version>
    </dependency>
</dependencies>
```

- `application.yml`

```yml
spring:
  redis:
    host: 47.98.178.84
    port: 6379
    database: 0
    password: password
    timeout: 60s  # 连接超时时间，2.0 中该参数的类型为Duration，这里在配置的时候需要指明单位
    # 连接池配置，2.0中直接使用jedis或者lettuce配置连接池（使用lettuce，依赖中必须包含commons-pool2包）
    lettuce:
      pool:
        # 最大空闲连接数
        max-idle: 500
        # 最小空闲连接数
        min-idle: 50
        # 等待可用连接的最大时间，负数为不限制
        max-wait:  -1s
        # 最大活跃连接数，负数为不限制
        max-active: -1
```

#### 2.2 生成图片验证码工具类

```java
public class ImgValidateCodeUtil {

    private static Random random = new Random();
    /**
     * 验证码的宽
     */
    private static int width = 160;
    /**
     * 验证码的高
     */
    private static int height = 40;
    /**
     * 验证码的干扰线数量
     */
    private static int lineSize = 30;
    /**
     * 验证码词典
     */
    private static String randomString = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 获取字体
     * @return
     */
    private static Font getFont() {
        return new Font("Times New Roman", Font.ROMAN_BASELINE, 40);
    }

    /**
     * 获取颜色
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandomColor(int fc, int bc) {

        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);

        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 12);

        return new Color(r, g, b);
    }

    /**
     * 绘制干扰线
     * @param g
     */
    private static void drawLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(20);
        int yl = random.nextInt(10);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 获取随机字符
     * @param num
     * @return
     */
    private static String getRandomString(int num) {
        num = num > 0 ? num : randomString.length();
        return String.valueOf(randomString.charAt(random.nextInt(num)));
    }

    /**
     * 绘制字符串
     * @param g
     * @param randomStr
     * @param i
     * @return
     */
    private static String drawString(Graphics g, String randomStr, int i) {
        g.setFont(getFont());
        g.setColor(getRandomColor(108, 190));
        String rand = getRandomString(random.nextInt(randomString.length()));
        randomStr += rand;
        g.translate(random.nextInt(3), random.nextInt(6));
        g.drawString(rand, 40 * i + 10, 25);
        return randomStr;
    }

    /**
     * 生成随机图片，返回 base64 字符串
     * @param
     * @return
     */
    public static Map<String, String> getImgCodeBaseCode(int length) {
        Map<String, String> result = new HashMap<>();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        // 获取颜色
        g.setColor(getRandomColor(105, 189));
        // 获取字体
        g.setFont(getFont());
        // 绘制干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }

        // 绘制随机字符
        String randomCode = "";
        for (int i = 0; i < length; i++) {
            randomCode = drawString(g, randomCode, i);
        }
        System.out.println("验证码是：" + randomCode);
        g.dispose();

        result.put("imgCode", randomCode);

        String base64Code = "";
        try {
            //返回 base64
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", bos);

            byte[] bytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            base64Code = encoder.encodeToString(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("data", "data:image/png;base64," + base64Code);
        return result;
    }

}
```

- 说明：
1. 为了方便，本案例中：验证码的字体/宽/高/干扰线数量等参数直接写死了，当然，你可以放在配置文件中做成可配置项；
1. 同样是为了方便，本案例中：验证码词典直接取的是0-9数字以及所有小写字母，可根据自己的需要更改或者也可作为可配置项。

#### 2.3 测试接口

为了便于测试，我这里封装了几个`RestFul`接口。(存/取缓存等业务操作，应该放在业务层中，这里也是为了偷懒，直接放在控制层里面了)

```java
@RestController
@RequestMapping("/code")
public class ValidateCodeController {

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 生成图片验证码
     * @return
     */
    @GetMapping("/getImgCode")
    public Map<String, String> getImgCode() {

        Map<String, String> result = new HashMap<>();

        try {
            // 获取 4位数验证码
            result= ImgValidateCodeUtil.getImgCodeBaseCode(4);
            // 将验证码存入redis 中（有效时长5分钟）
            cacheImgCode(result);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    /**
     * 校验验证码
     * @param imgCodeKey
     * @param imgCode
     * @return
     */
    @GetMapping("/checkImgCode")
    public String checkImgCode(String imgCodeKey, String imgCode) {
        String cacheCode = redisTemplate.opsForValue().get(imgCodeKey);
        if (null == cacheCode) {
            return "图片验证码已过期，请重新获取";
        }
        if (cacheCode.equals(imgCode.toLowerCase())) {
            return "验证码输入正确";
        }
        return "验证码输入错误";

    }

    /**
     * 将验证码存入redis 中
     * @param result
     */
    public void cacheImgCode(Map<String, String> result) {
        String imgCode = result.get("imgCode");
        UUID randomUUID = UUID.randomUUID();
        String imgCodeKey = randomUUID.toString();
        System.out.println("imgCodeKey:" + imgCodeKey);
        // 图片验证码有效时间 ：5 分钟
        redisTemplate.opsForValue().set(imgCodeKey, imgCode, 5, TimeUnit.MINUTES);
        result.put("imgCodeKey", imgCodeKey);
    }
}
```

### 三、测试及总结

#### 3.1 获取图片验证码

- 请求类型： GET
- 请求地址： 

[http://localhost:8080/code/getImgCode](http://localhost:8080/code/getImgCode)

- 返回结果： 

```xml
{
    "data": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAKAAAAAoCAIAAAD2TmbPAAACvElEQVR42u3cuWrEMBAGYL1zCKTZPl2qFCEsgbR5SMUgWIyO8Rz/yJLWxk02XiH78+gY2RuiYXv7+s32eG2DbQGla6zH7f4KOR9UOc8O7BG4QOOL2QTs1CxjVS5mJbBrpwsnuYwXB14plN9/bhpg0cn76SYGPwxjyZ/fL9ve/ybeUKu7LIL5J99hUrSXtpBk5aSi1AWeAtzSFTEH/j1e6pbzYIj6vhpVJxQ5s7SkiwKuGpcVO9RlGgd+U1bl9DBuVeD+95H2RyUfn2y7U8/30HUFlsauHphg5gPDW+89ZALOPnFi3uueAsyx14+iM2aasGVsHDS1IIndSbc/MDO+rdOkxMwJ0NYxlr5TAQwxLnWx2MQFOWTLgOlry50HK4Czw+DMxH/XBs6M6WuLBOYcpmMuCbNC+jTU2GGzMekB64NTVVDAutMr8eYChifUkH0wNoKdgKvHDDhNgjCDB1kewNJTreLRQTz1PBgVvlG02ICNYFHucDFgYyj3AOYMpEW5QyNwmdhCdXhOwGpmfS66PzCfmROdWXorghYfXYEVzNIFJdl6MB9Yd5KHWWgCuBXlrTKlKw1+wPz6KFYMTQv+9vBlMquB6WvHCZ1uwB66EfhEB3zFcH/ppcCt9bjDEV952IDAom9hnqp0fcxDAZyJcgbtrTtgEGBCl17hhj0X7ffUe3VxkNatNs6c0dyYESyK3cw7xBk2/mIDc/DCZ64C9/TWtcyACD4dmJ9/1mUV0recVpOAuvQBiwD7JQhPBObrrgZsNLMbjzAp4syaZu2Dy6FTN+P+U17jo3dzR3DnUB5Td0Hg1sAYPv+eXXfiCFZMfsZkNr5yseY0yWO07Md8WILrnRQmchVNfOFxhmWm/7yAfV9X8WbOkuSurfQETfRoI2RItJVZcacgnqMPXvhF/ZL5GYHj6j/G4NdW/wMQ1li94E5Z4gAAAABJRU5ErkJggg==",
    "imgCodeKey": "26393d5c-5d56-4ac3-bf38-c2cac1f33181",
    "imgCode": "6pt2"
}
```

- 说明：验证码是`6pt2 `，前端可以直接拿`data`中的数值直接展示，即为图片验证码，如下：

<img src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAKAAAAAoCAIAAAD2TmbPAAACvElEQVR42u3cuWrEMBAGYL1zCKTZPl2qFCEsgbR5SMUgWIyO8Rz/yJLWxk02XiH78+gY2RuiYXv7+s32eG2DbQGla6zH7f4KOR9UOc8O7BG4QOOL2QTs1CxjVS5mJbBrpwsnuYwXB14plN9/bhpg0cn76SYGPwxjyZ/fL9ve/ybeUKu7LIL5J99hUrSXtpBk5aSi1AWeAtzSFTEH/j1e6pbzYIj6vhpVJxQ5s7SkiwKuGpcVO9RlGgd+U1bl9DBuVeD+95H2RyUfn2y7U8/30HUFlsauHphg5gPDW+89ZALOPnFi3uueAsyx14+iM2aasGVsHDS1IIndSbc/MDO+rdOkxMwJ0NYxlr5TAQwxLnWx2MQFOWTLgOlry50HK4Czw+DMxH/XBs6M6WuLBOYcpmMuCbNC+jTU2GGzMekB64NTVVDAutMr8eYChifUkH0wNoKdgKvHDDhNgjCDB1kewNJTreLRQTz1PBgVvlG02ICNYFHucDFgYyj3AOYMpEW5QyNwmdhCdXhOwGpmfS66PzCfmROdWXorghYfXYEVzNIFJdl6MB9Yd5KHWWgCuBXlrTKlKw1+wPz6KFYMTQv+9vBlMquB6WvHCZ1uwB66EfhEB3zFcH/ppcCt9bjDEV952IDAom9hnqp0fcxDAZyJcgbtrTtgEGBCl17hhj0X7ffUe3VxkNatNs6c0dyYESyK3cw7xBk2/mIDc/DCZ64C9/TWtcyACD4dmJ9/1mUV0recVpOAuvQBiwD7JQhPBObrrgZsNLMbjzAp4syaZu2Dy6FTN+P+U17jo3dzR3DnUB5Td0Hg1sAYPv+eXXfiCFZMfsZkNr5yseY0yWO07Md8WILrnRQmchVNfOFxhmWm/7yAfV9X8WbOkuSurfQETfRoI2RItJVZcacgnqMPXvhF/ZL5GYHj6j/G4NdW/wMQ1li94E5Z4gAAAABJRU5ErkJggg=="/>

#### 3.2 校验图片验证码

- 请求类型： GET
- 请求地址： 

[http://localhost:8080/code/checkImgCode?imgCode=6pt2&imgCodeKey=26393d5c-5d56-4ac3-bf38-c2cac1f33181](http://localhost:8080/code/checkImgCode?imgCode=6pt2&imgCodeKey=26393d5c-5d56-4ac3-bf38-c2cac1f33181)

- 返回结果：验证码输入正确/验证码输入错误/图片验证码已过期，请重新获取
- 说明：前端需将请求验证码时返回的`imgCodeKey ` 字段的值以及用户数据的验证码请求到后端，进行校验；验证码有效期为五分钟，可按照实际需求配置。

#### 3.3 技术交流

1. [风尘博客-个人博客](https://www.dustyblog.cn/)
1. [风尘博客-博客园](https://www.cnblogs.com/vandusty)
1. [风尘博客-CSDN](https://blog.csdn.net/weixin_42036952)


最新文章，欢迎关注：公众号-风尘博客

![风尘博客](https://github.com/vanDusty/SpringBoot-Home/blob/master/dusty_blog.png?raw=true)