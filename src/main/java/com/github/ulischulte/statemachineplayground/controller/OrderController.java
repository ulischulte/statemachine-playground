package com.github.ulischulte.statemachineplayground.controller;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

@RestController
public class OrderController {

  private OrderRepository orderRepository;

  @Autowired
  public OrderController(final OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @PutMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
  public Integer createOrder(@RequestBody final Order order) {
    return orderRepository.save(order).getId();
  }

  @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  @GetMapping(value = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Order getOrder(@PathVariable("id") final Integer id, final HttpServletResponse response) {
    return orderRepository.findById(id)
        .orElseGet(() -> {
          response.setStatus(HttpStatus.NOT_FOUND.value());
          return null;
        });
  }
}
