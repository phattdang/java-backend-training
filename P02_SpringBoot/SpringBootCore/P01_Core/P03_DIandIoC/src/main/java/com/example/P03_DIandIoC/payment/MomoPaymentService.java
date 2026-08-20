package com.example.P03_DIandIoC.payment;

import org.springframework.stereotype.Service;

@Service
public class MomoPaymentService implements PaymentService {

    @Override
    public String providerName() {
        return "Momo";
    }

    @Override
    public String pay() {
        return "Paid by Momo";
    }
}
