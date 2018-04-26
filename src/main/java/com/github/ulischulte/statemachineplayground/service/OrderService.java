package com.github.ulischulte.statemachineplayground.service;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;

  public Optional<Order> findById(final Integer id) {
    return orderRepository.findById(id);
  }

  public void updateOrderState(final OrderState orderState, final Integer id) {
    orderRepository.updateOrderState(orderState, id);
  }
}
