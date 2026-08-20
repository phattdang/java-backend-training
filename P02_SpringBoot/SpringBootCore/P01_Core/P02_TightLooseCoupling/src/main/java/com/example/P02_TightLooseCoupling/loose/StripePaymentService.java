package com.example.P02_TightLooseCoupling.loose;

public class StripePaymentService implements PaymentService {

    @Override
    public String pay() {
        return "Paid by Stripe";
    }
}
