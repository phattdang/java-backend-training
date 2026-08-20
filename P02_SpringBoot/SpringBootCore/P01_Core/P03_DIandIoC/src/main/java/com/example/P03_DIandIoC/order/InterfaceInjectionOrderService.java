package com.example.P03_DIandIoC.order;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.P03_DIandIoC.payment.PaymentService;

@Service
public class InterfaceInjectionOrderService {

    private final List<PaymentService> paymentServices;

    public InterfaceInjectionOrderService(List<PaymentService> paymentServices) {
        this.paymentServices = paymentServices;
    }

    public List<String> getAvailableProviders() {
        return paymentServices.stream()
                .map(PaymentService::providerName)
                .toList();
    }
}
