package com.example.P03_DIandIoC.order;

import org.springframework.stereotype.Service;

import com.example.P03_DIandIoC.payment.PaymentService;

@Service
public class ConstructorInjectionOrderService {

    private final PaymentService paymentService;

    public ConstructorInjectionOrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String checkout() {
        return paymentService.pay();
    }
}
