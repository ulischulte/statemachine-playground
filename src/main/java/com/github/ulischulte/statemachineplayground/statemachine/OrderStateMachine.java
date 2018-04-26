package com.github.ulischulte.statemachineplayground.statemachine;

import static com.github.ulischulte.statemachineplayground.statemachine.StateMachineUtil.ORDER_MESSAGE_HEADER_KEY;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.service.OrderService;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderStateMachine {

  private static final String STATEMACHINE_PREFIX = "orderStateMachine_";

  private final OrderService orderService;
  private final StateMachineService<OrderState, OrderEvent> stateMachineService;

  public OrderState sendOrderEvent(final Order order, final OrderEvent orderEvent) {
    try {
      final StateMachine<OrderState, OrderEvent> stateMachine = stateMachineService
          .acquireStateMachine(STATEMACHINE_PREFIX + order.getId());
      stateMachine.addStateListener(new StateMachineListener());

      final boolean eventAccepted = stateMachine.sendEvent(MessageBuilder.withPayload(orderEvent)
          .setHeader(ORDER_MESSAGE_HEADER_KEY, order)
          .build());

      if (eventAccepted) {
        final OrderState orderState = stateMachine.getState().getId();
        orderService.updateOrderState(orderState, order.getId());
        return orderState;
      } else {
        log.warn("Could not accept event {} for order {}", orderEvent, order.getId());
        return order.getOrderState();
      }
    } finally {
      stateMachineService.releaseStateMachine(STATEMACHINE_PREFIX + order.getId());
    }
  }

}
