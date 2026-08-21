package com.example.P04_SpringBean.config.bean;

public class AuditFormatter {

    private final String prefix;

    public AuditFormatter(String prefix) {
        this.prefix = prefix;
        System.out.println("Create @Bean AuditFormatter with prefix = " + prefix);
    }

    public String format(String text) {
        return "[" + prefix + "] " + text;
    }
}
