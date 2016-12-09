package com.github.ulischulte.statemachineplayground.controller;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.repository.OrderRepository;
import com.github.ulischulte.statemachineplayground.statemachine.OrderStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/state")
public class StatemachineController {

  private OrderRepository orderRepository;
  private OrderStateMachine orderStateMachine;

  @Autowired
  public StatemachineController(final OrderRepository orderRepository, final OrderStateMachine orderStateMachine) {
    this.orderRepository = orderRepository;
    this.orderStateMachine = orderStateMachine;
  }

  @GetMapping("/{event}/{orderId}")
  public OrderState fireEvent(@PathVariable("event") final OrderEvent orderEvent, @PathVariable("orderId") final Integer orderId,
      final HttpServletResponse response) {
    final Order order = orderRepository.findById(orderId);
    if (order == null) {
      response.setStatus(HttpStatus.NOT_FOUND.value());
      return null;
    }
    return orderStateMachine.sendOrderEvent(order, orderEvent);
  }

  @GetMapping("/{orderId}")
  public OrderState getOrderState(@PathVariable("orderId") final Integer orderId) {
    final Order order = orderRepository.findById(orderId);
    return order.getOrderState();
  }

}
