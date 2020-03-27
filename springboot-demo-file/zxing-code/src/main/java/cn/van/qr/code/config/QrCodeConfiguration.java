package cn.van.qr.code.config;

import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: OriginalConfig
 *
 * @author: Van
 * Date:     2019-03-05 16:12
 * Description: 二维码配置类
 * Version： V1.0
 */
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

    // /**
    //  * 生成二维码的颜色
    //  */
    // private MatrixToImageConfig matrixToImageConfig;


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
