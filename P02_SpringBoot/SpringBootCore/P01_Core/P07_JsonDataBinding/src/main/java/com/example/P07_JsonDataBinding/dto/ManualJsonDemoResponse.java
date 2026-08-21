package com.example.P07_JsonDataBinding.dto;

// Response cho endpoint ObjectMapper manual demo.
public record ManualJsonDemoResponse(
        String javaObjectToJson,
        CreateUserRequest jsonToJavaObject
) {
}
