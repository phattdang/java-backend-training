package com.example.P03_DIandIoC.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.P03_DIandIoC.payment.PaymentService;

@Service
public class FieldInjectionOrderService {

    @Autowired
    @Qualifier("bankTransferPaymentService")
    private PaymentService paymentService;

    public String checkout() {
        return paymentService.pay();
    }
}
