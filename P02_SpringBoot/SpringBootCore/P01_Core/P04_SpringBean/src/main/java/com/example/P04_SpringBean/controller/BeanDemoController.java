package com.example.P04_SpringBean.controller;

import com.example.P04_SpringBean.component.TrainingMessageProvider;
import com.example.P04_SpringBean.lazy.HeavyService;
import com.example.P04_SpringBean.lifecycle.CacheService;
import com.example.P04_SpringBean.scope.PrototypeBean;
import com.example.P04_SpringBean.scope.RequestBean;
import com.example.P04_SpringBean.scope.SessionBean;
import com.example.P04_SpringBean.scope.SingletonBean;
import com.example.P04_SpringBean.service.OrderService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class BeanDemoController {

    private final TrainingMessageProvider messageProvider;
    private final OrderService orderService;
    private final SingletonBean singletonBean;
    private final ObjectProvider<PrototypeBean> prototypeBeanProvider;
    private final ObjectProvider<RequestBean> requestBeanProvider;
    private final ObjectProvider<SessionBean> sessionBeanProvider;
    private final CacheService cacheService;
    private final ObjectProvider<HeavyService> heavyServiceProvider;

    public BeanDemoController(
            TrainingMessageProvider messageProvider,
            OrderService orderService,
            SingletonBean singletonBean,
            ObjectProvider<PrototypeBean> prototypeBeanProvider,
            ObjectProvider<RequestBean> requestBeanProvider,
            ObjectProvider<SessionBean> sessionBeanProvider,
            CacheService cacheService,
            ObjectProvider<HeavyService> heavyServiceProvider) {
        this.messageProvider = messageProvider;
        this.orderService = orderService;
        this.singletonBean = singletonBean;
        this.prototypeBeanProvider = prototypeBeanProvider;
        this.requestBeanProvider = requestBeanProvider;
        this.sessionBeanProvider = sessionBeanProvider;
        this.cacheService = cacheService;
        this.heavyServiceProvider = heavyServiceProvider;
    }

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", messageProvider.message());
        result.put("tryEndpoints", new String[]{
                "/order",
                "/scope/singleton",
                "/scope/prototype",
                "/scope/request-session",
                "/payment",
                "/lifecycle",
                "/lazy"
        });
        return result;
    }

    @GetMapping("/order")
    public Map<String, Object> order() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sampleOrder", orderService.sampleOrder());
        result.put("auditMessageFromBeanConfig", orderService.auditMessage());
        return result;
    }

    @GetMapping("/payment")
    public Map<String, Object> payment() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("primaryInjection", orderService.primaryPaymentProvider());
        result.put("qualifierInjection", orderService.qualifierPaymentProvider());
        return result;
    }

    @GetMapping("/scope/singleton")
    public Map<String, Object> singletonScope() {
        SingletonBean first = singletonBean;
        SingletonBean second = singletonBean;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("firstId", first.getId());
        result.put("secondId", second.getId());
        result.put("sameInstance", first == second);
        return result;
    }

    @GetMapping("/scope/prototype")
    public Map<String, Object> prototypeScope() {
        PrototypeBean first = prototypeBeanProvider.getObject();
        PrototypeBean second = prototypeBeanProvider.getObject();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("firstId", first.getId());
        result.put("secondId", second.getId());
        result.put("sameInstance", first == second);
        return result;
    }

    @GetMapping("/scope/request-session")
    public Map<String, Object> requestAndSessionScope() {
        RequestBean requestBean = requestBeanProvider.getObject();
        SessionBean sessionBean = sessionBeanProvider.getObject();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("requestBeanId", requestBean.getId());
        result.put("sessionBeanId", sessionBean.getId());
        result.put("note", "requestBeanId changes each request; sessionBeanId stays the same in one browser session");
        return result;
    }

    @GetMapping("/lifecycle")
    public Map<String, Object> lifecycle() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("cacheServiceReady", cacheService.isReady());
        result.put("note", "Check console log for constructor, @PostConstruct, and @PreDestroy when app stops");
        return result;
    }

    @GetMapping("/lazy")
    public Map<String, Object> lazy() {
        HeavyService heavyService = heavyServiceProvider.getObject();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", heavyService.work());
        result.put("note", "HeavyService is created only when this endpoint is called");
        return result;
    }
}
