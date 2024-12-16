package com.example.luxe;

public class User {
    private String userId;
    private String fullName;
    private String phone;
    private String email;
    private String password;

    public User(String userId, String fullName, String phone, String email, String password) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
