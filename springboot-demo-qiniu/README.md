> 如果还没有七牛云账户，请先去[七牛云注册](https://portal.qiniu.com/signup?code=1hmackwtyy7o2)，本文不详细介绍七牛云的具体使用，只介绍如何将图片上传到七牛云并返回访问链接。

### 一、项目配置

#### 1.1 项目所依赖的jar包

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
</dependency>

<!-- 七牛云 -->
<dependency>
	<groupId>com.qiniu</groupId>
	<artifactId>qiniu-java-sdk</artifactId>
	<version>7.1.1</version>
</dependency>
<!--Swagger-ui配置-->
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.9.2</version>
</dependency>
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.9.2</version>
</dependency>
```

#### 1.2 `yml`项目配置文件中七牛云账户相关的密钥

```yml
# 七牛云相关配置
qinNiuCloud:
  ACCESS_KEY: 你的ACCESS_KEY
  SECRET_KEY: 你的SECRET_KEY
  bucketName: 你的空间名称
  bucketNameUrl: 该空间对应的域名
  imgStyle: 自定义的图片样式（本文未用到）

# 上传图片的限制
upload:
  maxSize: 3.0
```

#### 1.3 新建七牛云配置文件类`QiNiuCloudConfiguration `

> 稍微解释一下该类的必要性，`QiNiuCloudConfiguration `的租用是将配置文件中数据转换成静态数据，详细介绍见下篇文章。

```java
@Component
public class QiNiuCloudConfiguration {
    /**
     * 设置账号的AK和SK
     */
    public static  String accessKey;
    public static String secretKEY;
    /**
     * 要上传的空间和上传空间域名
     */
    public static String bucketName;
    public static String bucketNameUrl;
    /**
     * 图片处理的格式
     */
    public static String imgStyle;

    /**
     * 限制上传图片的大小
     */
    public static Double MB = 1024.0*1024.0;
    public static Double maxSize;

    @Value("${qinNiuCloud.ACCESS_KEY}")
    private String tempAccessKey;
    @Value("${qinNiuCloud.SECRET_KEY}")
    private String tempSecretKEY;


    @Value("${qinNiuCloud.bucketName}")
    private String tempBucketName;
    @Value("${qinNiuCloud.bucketNameUrl}")
    private String tempBucketNameUrl;
    @Value("${qinNiuCloud.imgStyle}")
    private String tempImgStyle;

    @Value("${upload.maxSize}")
    private Double tempMaxSize;

    /**
     * 被@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。
     * PostConstruct在构造函数之后执行,init()方法之前执行。
     */
    @PostConstruct
    public void setAccessKey() {
        accessKey = this.tempAccessKey;
    }
    @PostConstruct
    public void setSecretKEY() {
        secretKEY = this.tempSecretKEY;
    }
    @PostConstruct
    public void setBucketName() {
        bucketName = this.tempBucketName;
    }
    @PostConstruct
    public void setBucketNameUrl() {
        bucketNameUrl = this.tempBucketNameUrl;
    }
    @PostConstruct
    public void setImgStyle() {
        imgStyle = this.tempImgStyle;
    }
    @PostConstruct
    public void setMaxSize() {
        maxSize = this.tempMaxSize;
    }

}
```

#### 1.4 Swagger2 配置

> 为了方便测试，本项目集成了Swagger2

```java
@EnableSwagger2
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定controller存放的目录路径
                .apis(RequestHandlerSelectors.basePackage("cn.van.qiniu.controller"))
                .paths(PathSelectors.any())
                .build();
//                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title("这里是Swagger文档标题")
                // 文档描述
                .description("这里是文档描述")
                .termsOfServiceUrl("https://www.dustyblog.cn")
                .version("v1.0")//定义版本号
                .build();
    }
}
```

### 二、 上传图片业务

#### 2.1 Controller 层 `QiNiuCloudController`

```java
@RestController
@RequestMapping("/upload")
@Api(tags = "七牛云上传接口")
public class QiNiuCloudController {
    @Resource
    UploadService uploadService;



    @ApiOperation(value = "上传图片到七牛云")
    @PostMapping(value="/uploadToQiNiu")
    public String uploadImg(@RequestParam("file") MultipartFile image) {
        return uploadService.uploadImg(image);
    }

    @ApiOperation(value = "测试接口")
    @PostMapping(value="/test")
    public String test() {
        return "test";
    }
}
```

#### 2.2 上传接口 `UploadService`

```java
public interface UploadService {
    /**
     * 上传图片到七牛云
     * @param image
     * @return
     */
    String uploadImg(MultipartFile image);

}
```

#### 2.3 上传图片的实现 `UploadServiceImpl `

```java 
@Service
public class UploadServiceImpl implements UploadService {

    /**
     * 限制上传图片的大小
     */
    private static Double Mb = QiNiuCloudConfiguration.MB;
    private static Double maxSize = QiNiuCloudConfiguration.maxSize;

    @Override
    public String uploadImg(MultipartFile image) {
        String result = "";
        if(!image.isEmpty()) {
            if (!checkSize(image)) {
                //超出限制大小
                return "OUTOFSIZE";
            }
            String widthAndHeight = this.getWidthAndHeight(image);
            if (("ERROR").equals(widthAndHeight)) {
                return "ERROR";
            }
            try {
                byte[] bytes = image.getBytes();
                // 用随机字符串当做图片名存储
                String imageName = UUID.randomUUID().toString();
                QiuNiuCloudUtil qiuNiuCloudUtil = new QiuNiuCloudUtil();
                try {
                    //使用base64方式上传到七牛云
                    result = qiuNiuCloudUtil.uploadPicByBase64(bytes, imageName);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            } catch (IOException e) {
                //上传失败
                result = "UPLOADFAILED！";
                return result;
            }
        }
        // 图片为空
        return "EMPTY！";
    }
    /**
     * 校验大小，上传图片不能大于指定大小
     * @param file
     * @return
     */
    public static boolean checkSize(MultipartFile file){
        //校验大小
        double ratio = file.getSize() / Mb;
        if(ratio > maxSize){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 获取图片宽高拼接字符串：?width=100&height=200
     * @return
     */
    private String getWidthAndHeight(MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            if (null == image){
                return "ERROR";
            }
            int width = image.getWidth();
            int height = image.getHeight();
            return "?width="+width+"&height="+height;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```


#### 2.4 七牛上传工具类 `QiNiuCloudUtil `

```java
public class QiNiuCloudUtil {
    /**
     * 设置账号的AK和SK
     */
    private static final String ACCESS_KEY = QiNiuCloudConfiguration.accessKey;
    private static final String SECRET_KEY = QiNiuCloudConfiguration.secretKEY;
    /**
     * 要上传的空间
     */
    private static final String bucketName = QiNiuCloudConfiguration.bucketName;
    /**
     * 上传空间域名
     */
    private static final String bucketNameUrl = QiNiuCloudConfiguration.bucketNameUrl;
    /**
     * 密钥
     */
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    /**
     * 自定义的图片样式
     */
    private static final String imgStyle = QiNiuCloudConfiguration.imgStyle;

    public String getUpToken() {
        //生成凭证
        return auth.uploadToken(bucketName, null, 3600, new StringMap().put("insertOnly", 1));
    }

    /**
     *  base64方式上传
     * @param base64 ：图片编码
     * @param fileName：图片名称
     * @return
     * @throws Exception
     */
    public String uploadPicByBase64(byte[] base64, String fileName) throws Exception{
        String file64 = Base64.encodeToString(base64, 0);
        Integer length = base64.length;
        String dateStr = LocalDate.now().toString();
        // 将当前日期拼接在文件名称中
        fileName = dateStr + "/" + fileName;
        String url = "http://upload.qiniu.com/putb64/" + length + "/key/"+ UrlSafeBase64.encodeToString(fileName);
        //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody requestBody = RequestBody.create(null, file64);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(requestBody).build();
        System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);
        //如果不需要添加图片样式，使用以下方式
        // 返回图片地址
        String imgUrl = bucketNameUrl + "/" + fileName;
        return imgUrl;
    }
}

```

### 三、测试

启动项目，访问swagger地址 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

#### 3.1 测试上传接口

1. 打开上上传图片的接口，上传一张本地的图片
1. 成功上传，返回访问该图片的链接地址，如：[http://wechat.dustyblog.cn/2019-06-25/4aac5a69-a280-4e3e-9fe4-06fd07984923
](http://wechat.dustyblog.cn/2019-06-25/4aac5a69-a280-4e3e-9fe4-06fd07984923
)
1. 进入七牛云上该项目对应的空间，发现该图片是否存在。

#### 3.2 访问图片

打开返回的链接地址，即可访问该图片。

如：[http://wechat.dustyblog.cn/2019-06-25/4aac5a69-a280-4e3e-9fe4-06fd07984923
](http://wechat.dustyblog.cn/2019-06-25/4aac5a69-a280-4e3e-9fe4-06fd07984923
)

