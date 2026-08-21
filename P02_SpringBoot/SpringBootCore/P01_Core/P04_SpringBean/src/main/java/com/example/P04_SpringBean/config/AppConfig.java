package com.example.P04_SpringBean.config;

import com.example.P04_SpringBean.config.bean.AuditFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean("simpleAuditFormatter")
    public AuditFormatter auditFormatter() {
        return new AuditFormatter("AUDIT");
    }
}
