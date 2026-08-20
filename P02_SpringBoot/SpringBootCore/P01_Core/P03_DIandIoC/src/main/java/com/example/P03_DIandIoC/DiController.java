package com.example.P03_DIandIoC;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.P03_DIandIoC.order.ConstructorInjectionOrderService;
import com.example.P03_DIandIoC.order.DependencyResolutionService;
import com.example.P03_DIandIoC.order.FieldInjectionOrderService;
import com.example.P03_DIandIoC.order.InterfaceInjectionOrderService;
import com.example.P03_DIandIoC.order.OptionalDependencyOrderService;
import com.example.P03_DIandIoC.order.QualifierOrderService;
import com.example.P03_DIandIoC.order.SetterInjectionOrderService;

@RestController
@RequestMapping("/di")
public class DiController {

    private final ConstructorInjectionOrderService constructorInjectionOrderService;
    private final SetterInjectionOrderService setterInjectionOrderService;
    private final FieldInjectionOrderService fieldInjectionOrderService;
    private final InterfaceInjectionOrderService interfaceInjectionOrderService;
    private final OptionalDependencyOrderService optionalDependencyOrderService;
    private final QualifierOrderService qualifierOrderService;
    private final DependencyResolutionService dependencyResolutionService;

    public DiController(
            ConstructorInjectionOrderService constructorInjectionOrderService,
            SetterInjectionOrderService setterInjectionOrderService,
            FieldInjectionOrderService fieldInjectionOrderService,
            InterfaceInjectionOrderService interfaceInjectionOrderService,
            OptionalDependencyOrderService optionalDependencyOrderService,
            QualifierOrderService qualifierOrderService,
            DependencyResolutionService dependencyResolutionService
    ) {
        this.constructorInjectionOrderService = constructorInjectionOrderService;
        this.setterInjectionOrderService = setterInjectionOrderService;
        this.fieldInjectionOrderService = fieldInjectionOrderService;
        this.interfaceInjectionOrderService = interfaceInjectionOrderService;
        this.optionalDependencyOrderService = optionalDependencyOrderService;
        this.qualifierOrderService = qualifierOrderService;
        this.dependencyResolutionService = dependencyResolutionService;
    }

    @GetMapping
    public Map<String, Object> getExamples() {
        return Map.of(
                "constructorInjection", constructorInjectionOrderService.checkout(),
                "setterInjection", setterInjectionOrderService.checkout(),
                "fieldInjection", fieldInjectionOrderService.checkout(),
                "injectByInterface", interfaceInjectionOrderService.getAvailableProviders(),
                "qualifier", qualifierOrderService.checkout(),
                "optionalDependency", optionalDependencyOrderService.checkout(),
                "dependencyResolution", dependencyResolutionService.explain(),
                "circularDependency", "See circular package. Classes are not beans because enabling them would break startup."
        );
    }
}
