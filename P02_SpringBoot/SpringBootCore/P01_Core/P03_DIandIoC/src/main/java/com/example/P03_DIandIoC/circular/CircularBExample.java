package com.example.P03_DIandIoC.circular;

public class CircularBExample {

    private final CircularAExample circularAExample;

    public CircularBExample(CircularAExample circularAExample) {
        this.circularAExample = circularAExample;
    }

    public CircularAExample getCircularAExample() {
        return circularAExample;
    }
}
