package com.example.P08_ExceptionHandling.model;

public class User {
    private final Long id;
    private final String fullName;
    private final String email;
    private final int age;

    public User(Long id, String fullName, String email, int age) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
