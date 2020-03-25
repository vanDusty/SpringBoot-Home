package cn.van.scaffold.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

/**
 * @公众号： 风尘博客
 * @Classname WebConfig
 * @Description TODO
 * @Date 2019/3/19 9:20 下午
 * @Author by Van
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 启用 FastJson: https://github.com/alibaba/fastjson/wiki/%E5%9C%A8-Spring-%E4%B8%AD%E9%9B%86%E6%88%90-Fastjson
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        //自定义配置...
        FastJsonConfig config = new FastJsonConfig();
        //- https://github.com/alibaba/fastjson/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98
        //- 不用 WriteDateUseDateFormat ，日期由客户端自行格式化
        //- 不用 WriteMapNullValue ，默认不输出属性值为null的字段
        //- BrowserCompatible ，会把中文编码为Unicode转义字符，需要的时候可以加上
        config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
        converter.setFastJsonConfig(config);
        converters.add(0, converter);
    }
}
