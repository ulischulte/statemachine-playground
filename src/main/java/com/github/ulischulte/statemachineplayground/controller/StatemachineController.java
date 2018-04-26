package com.github.ulischulte.statemachineplayground.controller;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.service.OrderService;
import com.github.ulischulte.statemachineplayground.statemachine.OrderStateMachine;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/statemachine")
@RequiredArgsConstructor
public class StatemachineController {

  private final OrderService orderService;
  private final OrderStateMachine orderStateMachine;

  @PostMapping("/{event}/{orderId}")
  public OrderState fireEvent(
      @PathVariable("event") final OrderEvent orderEvent,
      @PathVariable("orderId") final Integer orderId,
      final HttpServletResponse response) {
    final Optional<Order> maybeOrder = orderService.findById(orderId);
    return maybeOrder.map(order -> orderStateMachine.sendOrderEvent(order, orderEvent))
        .orElseGet(() -> {
          response.setStatus(HttpStatus.NOT_FOUND.value());
          return null;
        });
  }

  @GetMapping("/{orderId}")
  public OrderState getOrderState(@PathVariable("orderId") final Integer orderId) {
    final Optional<Order> maybeOrder = orderService.findById(orderId);
    return maybeOrder.map(Order::getOrderState)
        .orElse(null);
  }

  @GetMapping("/events")
  public List<OrderEvent> getAllEvents() {
    return Arrays.asList(OrderEvent.values());
  }

}
