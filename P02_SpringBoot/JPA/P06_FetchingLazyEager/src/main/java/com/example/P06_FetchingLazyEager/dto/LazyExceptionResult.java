package com.example.P06_FetchingLazyEager.dto;

public record LazyExceptionResult(
        boolean exceptionOccurred,
        String exceptionType,
        String message,
        String explanation
) {
}
