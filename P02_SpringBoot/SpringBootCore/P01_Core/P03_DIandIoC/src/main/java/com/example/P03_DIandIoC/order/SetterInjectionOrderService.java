package com.example.P03_DIandIoC.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.P03_DIandIoC.payment.PaymentService;

@Service
public class SetterInjectionOrderService {

    private PaymentService paymentService;

    public SetterInjectionOrderService() {
    }

    @Autowired
    public void setPaymentService(@Qualifier("momoPaymentService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String checkout() {
        return paymentService.pay();
    }
}
