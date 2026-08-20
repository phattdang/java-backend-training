package com.example.P02_TightLooseCoupling.loose;

import org.springframework.stereotype.Service;

@Service
public class MomoPaymentService implements PaymentService {

    @Override
    public String pay() {
        return "Paid by Momo";
    }
}
