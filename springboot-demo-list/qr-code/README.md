# Spring Boot ：生成二维码

### 背景

> 在网站开发中,经常会遇到要生成二维码的情况,比如要使用微信支付、网页登录等.

#### 二维码应用场景

1. 信息获取（名片、地图、WIFI密码、资料）
1. 网站跳转（跳转到微博、手机网站、网站）
1. 广告推送（用户扫码，直接浏览商家推送的视频、音频广告）
1. 手机电商（用户扫码、手机直接购物下单）
1. 防伪溯源（用户扫码、即可查看生产地；同时后台可以获取最终消费地)
1. 优惠促销（用户扫码，下载电子优惠券，抽奖）
1. 会员管理（用户手机上获取电子会员信息、VIP服务）
1. 手机支付（扫描商品二维码，通过银行或第三方支付提供的手机端通道完成支付）
1. 账号登录（扫描二维码进行各个网站或软件的登录）
1. ......



### 一、编写代码生成二维码

#### 1.1 项目依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!-- 二维码支持包 -->
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>core</artifactId>
    <version>3.2.0</version>
</dependency>
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>javase</artifactId>
    <version>3.2.0</version>
</dependency>
<!-- lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>1.8.4</scope>
</dependency>
```

#### 1.2 项目配置文件

> 对于logo配置和二维码的配置，我放在配置文件中了。

```yml
qr:
  code:
    charset: utf-8
    width: 300
    height: 300
    logoWidth: 60
    logoHeight: 60
    picType: jpg
```

#### 1.3 二维码配置类

> 为了便于注入生成二维码的属性
> 

```java
@Data
@Configuration
public class QrCodeConfiguration {

    @Value("${qr.code.charset}")
    private String configCharset;
    @Value("${qr.code.width}")
    private Integer configWidth;
    @Value("${qr.code.height}")
    private Integer configHeight;
    @Value("${qr.code.logoWidth}")
    private Integer configLogoWidth;
    @Value("${qr.code.logoHeight}")
    private Integer configLogoHeight;
    @Value("${qr.code.picType}")
    private String configPicType;


    /**
     * 编码
     */
    public static String charset;
    /**
     * 生成二维码的宽
     */
    public static Integer width;


    /**
     * 生成二维码的高
     */
    public static Integer height;


    /**
     * logo的宽
     */
    public static Integer logoWidth;


    /**
     * logo的高
     */
    public static Integer logoHeight;


    /**
     * 生成二维码图片的格式 png, jpg
     */
    public static String picType;
    
    @PostConstruct
    public void setCharset() {
        charset = this.configCharset;
    }

    @PostConstruct
    public void setWidth() {
        width = this.configWidth;
    }

    @PostConstruct
    public void setHeight() {
        height = this.configHeight;
    }

    @PostConstruct
    public void setLogoWidth() {
        logoWidth = this.configLogoWidth;
    }

    @PostConstruct
    public void setLogoHeight() {
        logoHeight = this.configLogoHeight;
    }

    @PostConstruct
    public void setPicType() {
        picType = this.configPicType;
    }

}
```

#### 1.4 生成二维码的工具类

```java
public class QrCodeUtil {


    /**
     * 生成二维码图片
     *
     * @param content      二维码内容
     * @param imgPath      中间log地址
     * @param needCompress 是否压缩
     * @return
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();

        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, QrCodeConfiguration.charset);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QrCodeConfiguration.width, QrCodeConfiguration.height,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        QrCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 插入LOGO
     *
     * @param imgPath      二维码图片
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩logo
        if (needCompress) {
            if (width > QrCodeConfiguration.logoWidth) {
                width = QrCodeConfiguration.logoWidth;
            }
            if (height > QrCodeConfiguration.logoHeight) {
                height = QrCodeConfiguration.logoHeight;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QrCodeConfiguration.width - width) / 2;
        int y = (QrCodeConfiguration.height - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 创建文件夹， mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 生成带logo二维码
     *
     * @param content      二维码的内容
     * @param imgPath      中间log地址
     * @param output
     * @param needCompress 是否压缩
     * @throws Exception
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception {

        BufferedImage image = QrCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, QrCodeConfiguration.picType, output);
    }

    /**
     * 生成不带log 二维码
     *
     * @param content 二维码的内容
     * @param output
     * @throws Exception
     */
    public static void encode(String content, OutputStream output) throws Exception {
        QrCodeUtil.encode(content, null, output, false);
    }
}

```

### 二、测试

#### 2.1 `QrCodeController.java`

```java
@RestController
@RequestMapping("/qrCode")
public class QrCodeController {
    /**
     * 生成 普通二维码
     */
    @GetMapping(value = "/getCommonQRCode")
    public void getCommonQRCode(HttpServletResponse response, String url) throws Exception {
        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
            //使用工具类生成二维码
            QrCodeUtil.encode(url, stream);
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }

    /**
     * 生成 带有logo二维码
     */
    @GetMapping(value = "/getQRCodeWithLogo")
    public void getQRCodeWithLogo(HttpServletResponse response, String url) throws Exception {
        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
            // logo 地址
            String logoPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                    + "templates" + File.separator + "advator.jpg";
            // String logoPath = "springboot-demo-list/qr-code/src/main/resources/templates/advator.jpg";
            //使用工具类生成二维码
            QrCodeUtil.encode(url, logoPath, stream, true);
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }
}
```

#### 2.2 二维码生成

启动项目，依次访问如下地址，获取两种二维码：

获取普通二维码

[http://localhost:8080/qrCode/getCommonQRCode?url=http://www.dustyblog.cn](https://localhost:8080/qrCode/getCommonQRCode?url=https://www.dustyblog.cn)

获取带`logo`的二维码

[http://localhost:8080/qrCode/getQRCodeWithLogo?url=https://www.dustyblog.cn](http://localhost:8080/qrCode/getQRCodeWithLogo?url=https://www.dustyblog.cn)

扫码测试，直达风尘博客。

#### 2.3 技术交流


1. [风尘博客](https://www.dustyblog.cn/)
1. [风尘博客-博客园](https://www.cnblogs.com/vandusty)
1. [风尘博客-CSDN](https://blog.csdn.net/weixin_42036952)
1. [风尘博客-掘金](https://juejin.im/user/5d5ea68e6fb9a06afa328f56)

关注公众号，了解更多：

![风尘博客](https://github.com/vanDusty/SpringBoot-Home/blob/master/dusty_blog.png?raw=true)
