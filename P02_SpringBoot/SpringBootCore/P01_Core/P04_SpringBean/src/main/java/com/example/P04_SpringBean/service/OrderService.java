package com.example.P04_SpringBean.service;

import com.example.P04_SpringBean.config.bean.AuditFormatter;
import com.example.P04_SpringBean.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService primaryPaymentService;
    private final PaymentService paypalPaymentService;
    private final AuditFormatter auditFormatter;

    public OrderService(
            OrderRepository orderRepository,
            PaymentService primaryPaymentService,
            @Qualifier("paypalPaymentService") PaymentService paypalPaymentService,
            @Qualifier("simpleAuditFormatter") AuditFormatter auditFormatter) {
        this.orderRepository = orderRepository;
        this.primaryPaymentService = primaryPaymentService;
        this.paypalPaymentService = paypalPaymentService;
        this.auditFormatter = auditFormatter;
    }

    public String sampleOrder() {
        return orderRepository.findSampleOrder();
    }

    public String primaryPaymentProvider() {
        return primaryPaymentService.providerName();
    }

    public String qualifierPaymentProvider() {
        return paypalPaymentService.providerName();
    }

    public String auditMessage() {
        return auditFormatter.format("OrderService uses constructor injection");
    }
}
