package com.example.StockMatching.impl;

public interface StockMatching {

    public void addBuyOrder(User user, Order order);
    public void addSellOrder(User user, Order order);
    public void matchOrders();
    public void notifyUsers();
}
