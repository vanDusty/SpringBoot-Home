package cn.van.qiniu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: QiNiuCloudBean
 *
 * @author: Van
 * Date:     2019-06-25 16:01
 * Description: 七牛云配置文件类（用于将配置数据转换成静态数据）
 * Version： V1.0
 */
@Configuration
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
