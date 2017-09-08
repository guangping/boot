package io.lance.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Author Lance.
 * Date: 2017-09-01 17:27
 * Desc: mvc配置
 */
@Configuration
public class SpringWebConfig {
    @Primary
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        return adapter;
    }

    @Primary
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
        return mapping;
    }

    /**
     * 使用fastjson作为json解析
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteSlashAsSpecial,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteNullStringAsEmpty
        );
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm");
        fastConverter.setFastJsonConfig(fastJsonConfig);

        //设置支持类型
       /* List<MediaType> mediaTypes = Lists.newArrayList();
        mediaTypes.add(new MediaType("text/plain;charset=UTF-8"));
        mediaTypes.add(new MediaType("text/html;charset=UTF-8"));
        mediaTypes.add(new MediaType("application/json;charset=UTF-8"));
        mediaTypes.add(new MediaType("text/xml;charset=UTF-8"));
        fastConverter.setSupportedMediaTypes(mediaTypes);*/

        return new HttpMessageConverters(fastConverter);
    }


}
