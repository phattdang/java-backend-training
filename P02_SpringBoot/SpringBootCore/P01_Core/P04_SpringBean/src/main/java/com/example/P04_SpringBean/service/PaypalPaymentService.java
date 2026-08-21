package com.example.P04_SpringBean.service;

import org.springframework.stereotype.Service;

@Service("paypalPaymentService")
public class PaypalPaymentService implements PaymentService {

    public PaypalPaymentService() {
        System.out.println("Create PaypalPaymentService");
    }

    @Override
    public String providerName() {
        return "Paypal";
    }
}
