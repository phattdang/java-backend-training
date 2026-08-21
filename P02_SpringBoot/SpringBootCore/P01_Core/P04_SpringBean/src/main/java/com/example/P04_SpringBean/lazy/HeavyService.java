package com.example.P04_SpringBean.lazy;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class HeavyService {

    public HeavyService() {
        System.out.println("Create HeavyService only when it is used because of @Lazy");
    }

    public String work() {
        return "HeavyService is now initialized and working";
    }
}
