package com.example.P03_DIandIoC.circular;

public class CircularAExample {

    private final CircularBExample circularBExample;

    public CircularAExample(CircularBExample circularBExample) {
        this.circularBExample = circularBExample;
    }

    public CircularBExample getCircularBExample() {
        return circularBExample;
    }
}
