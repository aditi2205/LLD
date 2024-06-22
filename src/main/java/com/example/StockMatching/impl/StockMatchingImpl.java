package com.example.StockMatching.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockMatchingImpl {
    private List<Order> buyOrders;
    private List<Order> sellOrders;
    private MatchingStrategy matchStrategy;

    public StockMatchingImpl(MatchingStrategy matchStrategy) {
        buyOrders = new ArrayList<>();
        sellOrders = new ArrayList<>();
        this.matchStrategy = matchStrategy;
    }

    public void addOrder(Order order) {
        if (order.getOrderType() == OrderType.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    public void matchOrders() {
        List<Order> buyOrdersCopy;
        List<Order> sellOrdersCopy;

        synchronized (buyOrders) {
            buyOrdersCopy = new ArrayList<>(buyOrders);
        }
        synchronized (sellOrders) {
            sellOrdersCopy = new ArrayList<>(sellOrders);
        }

        matchStrategy.match(buyOrdersCopy, sellOrdersCopy);

        synchronized (buyOrders) {
            buyOrders.removeAll(buyOrdersCopy);
            buyOrders.addAll(buyOrdersCopy);
        }
        synchronized (sellOrders) {
            sellOrders.removeAll(sellOrdersCopy);
            sellOrders.addAll(sellOrdersCopy);
        }

        notifyUsers();
    }

    public List<Order> getBuyOrders() {
        return Collections.unmodifiableList(buyOrders);
    }

    public List<Order> getSellOrders() {
        return Collections.unmodifiableList(sellOrders);
    }

    private void notifyUsers() {
        // Notify users about order execution.
    }
}

