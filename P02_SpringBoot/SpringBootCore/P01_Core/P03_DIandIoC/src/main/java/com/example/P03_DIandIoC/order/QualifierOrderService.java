package com.example.P03_DIandIoC.order;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.P03_DIandIoC.payment.PaymentService;

@Service
public class QualifierOrderService {

    private final PaymentService paymentService;

    public QualifierOrderService(@Qualifier("momoPaymentService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String checkout() {
        return paymentService.pay();
    }
}
