package com.example.manafood.model;

public class GoogleUserModel {
    private String username;
    private String email;
    private String address;
    private String phone;

    public GoogleUserModel() {
        //default constructor is needed for firebase
    }

    public GoogleUserModel(String username,String email, String address,String phone) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone=phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}



