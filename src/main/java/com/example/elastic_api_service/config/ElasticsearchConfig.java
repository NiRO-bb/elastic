package com.example.elastic_api_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elastic-service.index.method}")
    private String methodIndex;

    @Value("${elastic-service.index.request}")
    private String requestIndex;

    /**
     * Creates bean used as index name for MethodLog document.
     *
     * @return index name
     */
    @Bean
    public String getMethodIndex() {
        return methodIndex;
    }

    /**
     * Creates bean used as index name for RequestLog document.
     *
     * @return index name
     */
    @Bean
    public String getRequestIndex() {
        return requestIndex;
    }

}
