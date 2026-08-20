package com.example.P03_DIandIoC.order;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.P03_DIandIoC.payment.PaymentService;

@Service
public class DependencyResolutionService {

    private final PaymentService primaryPaymentService;
    private final PaymentService qualifiedPaymentService;

    public DependencyResolutionService(
            PaymentService primaryPaymentService,
            @Qualifier("bankTransferPaymentService") PaymentService qualifiedPaymentService
    ) {
        this.primaryPaymentService = primaryPaymentService;
        this.qualifiedPaymentService = qualifiedPaymentService;
    }

    public Map<String, String> explain() {
        return Map.of(
                "primaryBean", primaryPaymentService.providerName(),
                "qualifiedBean", qualifiedPaymentService.providerName()
        );
    }
}
