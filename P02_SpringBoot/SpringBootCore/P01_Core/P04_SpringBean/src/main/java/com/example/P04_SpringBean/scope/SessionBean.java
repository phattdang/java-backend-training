package com.example.P04_SpringBean.scope;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.UUID;

@Component
@SessionScope
public class SessionBean {

    private final String id = UUID.randomUUID().toString();

    public SessionBean() {
        System.out.println("Create SessionBean for one HTTP session");
    }

    public String getId() {
        return id;
    }
}
