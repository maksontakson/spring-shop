package com.example.springshop.service;

import com.example.springshop.domain.Order;

public interface OrderService {
    void save(Order order);
    Order getOrder(Integer id);
}
