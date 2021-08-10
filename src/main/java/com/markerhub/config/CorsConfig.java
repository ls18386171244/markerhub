package com.markerhub.config;

import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.config
 * @ClassName: CorsConfig
 * @Author: admin
 * @Description: 解决跨域
 * @Date: 2021/8/10 16:16
 * @Version: 1.0
 */
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
