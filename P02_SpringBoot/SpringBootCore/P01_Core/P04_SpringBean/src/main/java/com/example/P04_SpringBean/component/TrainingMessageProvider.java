package com.example.P04_SpringBean.component;

import org.springframework.stereotype.Component;

@Component("trainingMessageProvider")
public class TrainingMessageProvider {

    public String message() {
        return "Spring Bean demo is running";
    }
}
