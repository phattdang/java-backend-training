package com.example.P04_SpringBean.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service("momoPaymentService")
public class MomoPaymentService implements PaymentService {

    public MomoPaymentService() {
        System.out.println("Create MomoPaymentService (@Primary)");
    }

    @Override
    public String providerName() {
        return "Momo";
    }
}
