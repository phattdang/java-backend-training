package com.example.P04_SpringBean.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeBean {

    private final String id = UUID.randomUUID().toString();

    public PrototypeBean() {
        System.out.println("Create PrototypeBean");
    }

    public String getId() {
        return id;
    }
}
