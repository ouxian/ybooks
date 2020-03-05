package com.gdupi;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 提供跨域处理
 */
@Configuration
public class CORSConfiguration extends WebMvcConfigurerAdapter{
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**");
    }

}
