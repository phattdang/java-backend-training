package com.example.P02_TightLooseCoupling;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.P02_TightLooseCoupling.loose.LooseOrderService;
import com.example.P02_TightLooseCoupling.tight.TightOrderService;

@RestController
@RequestMapping("/coupling")
public class CouplingController {

    private final TightOrderService tightOrderService;
    private final LooseOrderService looseOrderService;

    public CouplingController(TightOrderService tightOrderService, LooseOrderService looseOrderService) {
        this.tightOrderService = tightOrderService;
        this.looseOrderService = looseOrderService;
    }

    @GetMapping
    public Map<String, String> compare() {
        return Map.of(
                "tightCoupling", tightOrderService.checkout(),
                "looseCoupling", looseOrderService.checkout()
        );
    }
}
