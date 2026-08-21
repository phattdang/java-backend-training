package com.example.P04_SpringBean.scope;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Component
@RequestScope
public class RequestBean {

    private final String id = UUID.randomUUID().toString();

    public RequestBean() {
        System.out.println("Create RequestBean for one HTTP request");
    }

    public String getId() {
        return id;
    }
}
