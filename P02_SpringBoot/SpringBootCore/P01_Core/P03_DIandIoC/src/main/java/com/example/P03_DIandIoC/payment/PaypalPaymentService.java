package com.example.P03_DIandIoC.payment;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class PaypalPaymentService implements PaymentService {

    @Override
    public String providerName() {
        return "Paypal";
    }

    @Override
    public String pay() {
        return "Paid by Paypal";
    }
}
