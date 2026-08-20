package com.example.P03_DIandIoC.payment;

import org.springframework.stereotype.Service;

@Service
public class BankTransferPaymentService implements PaymentService {

    @Override
    public String providerName() {
        return "Bank Transfer";
    }

    @Override
    public String pay() {
        return "Paid by Bank Transfer";
    }
}
