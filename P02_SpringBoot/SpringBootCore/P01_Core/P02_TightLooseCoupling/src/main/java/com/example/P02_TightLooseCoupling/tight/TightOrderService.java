package com.example.P02_TightLooseCoupling.tight;

import org.springframework.stereotype.Service;

@Service
public class TightOrderService {

    private final MomoPaymentService paymentService = new MomoPaymentService();

    public String checkout() {
        return paymentService.pay();
    }
}
