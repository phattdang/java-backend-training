package com.example.P04_SpringBean.scope;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SingletonBean {

    private final String id = UUID.randomUUID().toString();

    public SingletonBean() {
        System.out.println("Create SingletonBean");
    }

    public String getId() {
        return id;
    }
}
