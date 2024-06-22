package com.example.StockMatching.impl;

import lombok.ToString;

@ToString
public class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void notifyOrderExecution(Order order) {
        // Notify the user about the execution of the order.
        System.out.println("Order executed "+order+" for user "+this);
    }
}