package com.example.StockMatching.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NearestMatchStrategy implements MatchingStrategy {
    @Override
    public void match(List<Order> buyOrders, List<Order> sellOrders) {
        if (buyOrders.isEmpty() || sellOrders.isEmpty()) {
            // There are no orders to match.
            return;
        }

        // Sort the buy and sell orders by price.
        Collections.sort(buyOrders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Double.compare(o2.getPrice(), o1.getPrice());
            }
        });
        Collections.sort(sellOrders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
        });

        int buyIndex = 0;
        int sellIndex = 0;
        Order buyOrder = buyOrders.get(buyIndex);
        Order sellOrder = sellOrders.get(sellIndex);

        while (buyIndex < buyOrders.size() && sellIndex < sellOrders.size()) {
            if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                // Match the buy and sell orders.
                int matchedQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
                buyOrder.setQuantity(buyOrder.getQuantity() - matchedQuantity);
                sellOrder.setQuantity(sellOrder.getQuantity() - matchedQuantity);

                // Notify the users about the execution of their orders.
                buyOrder.getUser().notifyOrderExecution(buyOrder);
                sellOrder.getUser().notifyOrderExecution(sellOrder);

                if (buyOrder.getQuantity() == 0) {
                    buyIndex++;
                    if (buyIndex < buyOrders.size()) {
                        buyOrder = buyOrders.get(buyIndex);
                    }
                }
                if (sellOrder.getQuantity() == 0) {
                    sellIndex++;
                    if (sellIndex < sellOrders.size()) {
                        sellOrder = sellOrders.get(sellIndex);
                    }
                }
            } else {
                break; // No more matches.
            }
        }
    }
}

