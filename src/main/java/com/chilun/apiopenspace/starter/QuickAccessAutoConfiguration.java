package com.chilun.apiopenspace.starter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 齿轮
 * @date 2024-02-17-14:30
 */
@Configuration
@EnableConfigurationProperties(com.chilun.apiopenspace.starter.PublicKeyPrivateKeyUtils.class)
public class QuickAccessAutoConfiguration {

    @Bean(name = "T")
    public APIAccessClientFactory<Object> generalFactory() {
        return new APIAccessClientFactory<>();
    }

    @Bean(name = "general")
    public APIAccessClientFactory generalFactory2() {
        return new APIAccessClientFactory();
    }
}
