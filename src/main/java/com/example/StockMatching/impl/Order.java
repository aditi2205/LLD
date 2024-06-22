package com.example.StockMatching.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Order {
    private String symbol;
    private OrderType orderType;
    private int quantity;
    private double price;

    private User user;

    public Order(User user1, OrderType orderType, int i, double v) {
        this.user = user1;
        this.orderType = orderType;
        this.quantity = i;
        this.price = v;
    }
}
