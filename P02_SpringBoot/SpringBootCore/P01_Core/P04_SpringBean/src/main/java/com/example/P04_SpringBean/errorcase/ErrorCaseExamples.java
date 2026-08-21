package com.example.P04_SpringBean.errorcase;

/*
 * These examples are intentionally comments so the application can start normally.
 * Copy one example into a scanned package, or uncomment it carefully, when you want
 * to observe the startup error.
 */
public class ErrorCaseExamples {

    /*
    // Case 1: NoSuchBeanDefinitionException
    // Spring cannot find MissingRepository because no bean of that type exists.
    @org.springframework.stereotype.Service
    static class MissingBeanDemoService {
        MissingBeanDemoService(MissingRepository missingRepository) {
        }
    }

    interface MissingRepository {
    }
    */

    /*
    // Case 2: NoUniqueBeanDefinitionException
    // If MomoPaymentService and PaypalPaymentService both remove @Primary, this fails.
    @org.springframework.stereotype.Service
    static class NoUniquePaymentDemoService {
        NoUniquePaymentDemoService(com.example.P04_SpringBean.service.PaymentService paymentService) {
        }
    }
    */

    /*
    // Case 3: UnsatisfiedDependencyException
    // Spring sees BrokenService, but cannot create it because String is not a configured bean.
    @org.springframework.stereotype.Service
    static class BrokenService {
        BrokenService(String apiKey) {
        }
    }
    */
}
