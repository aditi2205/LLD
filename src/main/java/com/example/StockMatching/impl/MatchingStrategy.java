package com.example.StockMatching.impl;

import java.util.List;

public interface MatchingStrategy {
    void match(List<Order> buyOrders, List<Order> sellOrders);
}
