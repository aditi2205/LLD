package com.example.StockMatching.impl;

public class StockMarketDemo {
    public static void main(String[] args) {
        // Create some user accounts.
        User user1 = new User("Alice", "alice@example.com");
        User user2 = new User("Bob", "bob@example.com");

        // Create a stock matching system.
        MatchingStrategy matchStrategy = new NearestMatchStrategy();
        StockMatchingImpl stockMatchingSystem = new StockMatchingImpl(matchStrategy);

        // Place some buy and sell orders.
        stockMatchingSystem.addOrder(new Order(user1, OrderType.BUY, 100, 10.00));
        stockMatchingSystem.addOrder(new Order(user2, OrderType.SELL, 50, 9.50));
        stockMatchingSystem.addOrder(new Order(user1, OrderType.BUY, 150, 9.25));
        stockMatchingSystem.addOrder(new Order(user2, OrderType.SELL, 200, 10.50));
        stockMatchingSystem.addOrder(new Order(user1, OrderType.BUY, 50, 10.25));

        // Execute the orders.
        stockMatchingSystem.matchOrders();

        // Print out the remaining buy and sell orders.
        System.out.println("Remaining buy orders:");
        for (Order order : stockMatchingSystem.getBuyOrders()) {
            System.out.println(order.toString());
        }

        System.out.println("Remaining sell orders:");
        for (Order order : stockMatchingSystem.getSellOrders()) {
            System.out.println(order.toString());
        }
    }
}
