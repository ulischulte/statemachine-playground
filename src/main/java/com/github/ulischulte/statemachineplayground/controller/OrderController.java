package com.github.ulischulte.statemachineplayground.controller;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

  private OrderRepository orderRepository;

  @Autowired
  public OrderController(final OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @PostMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
  public Integer createOrder(@RequestBody final Order order) {
    return orderRepository.save(order).getId();
  }

  @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }
}
