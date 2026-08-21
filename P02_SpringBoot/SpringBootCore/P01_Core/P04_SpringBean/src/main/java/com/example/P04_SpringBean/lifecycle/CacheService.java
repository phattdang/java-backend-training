package com.example.P04_SpringBean.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private boolean ready;

    public CacheService() {
        System.out.println("Create CacheService constructor");
    }

    @PostConstruct
    public void init() {
        ready = true;
        System.out.println("@PostConstruct CacheService.init(): load cache data");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("@PreDestroy CacheService.destroy(): clear cache data");
    }

    public boolean isReady() {
        return ready;
    }
}
