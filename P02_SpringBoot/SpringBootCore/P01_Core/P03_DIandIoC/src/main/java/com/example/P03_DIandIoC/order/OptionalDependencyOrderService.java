package com.example.P03_DIandIoC.order;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.P03_DIandIoC.discount.DiscountService;

@Service
public class OptionalDependencyOrderService {

    private final Optional<DiscountService> discountService;

    public OptionalDependencyOrderService(Optional<DiscountService> discountService) {
        this.discountService = discountService;
    }

    public String checkout() {
        return discountService
                .map(DiscountService::discount)
                .orElse("No discount bean");
    }
}
