package com.example.P02_TightLooseCoupling.loose;

import org.springframework.stereotype.Service;

@Service
public class LooseOrderService {

    private final PaymentService paymentService;

    public LooseOrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String checkout() {
        return paymentService.pay();
    }
}
