package com.github.ulischulte.statemachineplayground.statemachine;

import static com.github.ulischulte.statemachineplayground.statemachine.StateMachineUtil.ORDER_MESSAGE_HEADER_KEY;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.repository.OrderRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Component;

@Component
public class OrderStateMachine {

  private static final String STATEMACHINE_PREFIX = "orderStateMachine_";
  private final Log logger = LogFactory.getLog(this.getClass());

  private final OrderRepository orderRepository;
  private final StateMachineService<OrderState, OrderEvent> stateMachineService;

  @Autowired
  public OrderStateMachine(final OrderRepository orderRepository, final StateMachineService<OrderState, OrderEvent> stateMachineService) {
    this.orderRepository = orderRepository;
    this.stateMachineService = stateMachineService;
  }

  public OrderState sendOrderEvent(final Order order, final OrderEvent orderEvent) {
    try {
      final StateMachine<OrderState, OrderEvent> stateMachine = stateMachineService
          .acquireStateMachine(STATEMACHINE_PREFIX + order.getId());

      final boolean eventAccepted = stateMachine.sendEvent(MessageBuilder.withPayload(orderEvent)
          .setHeader(ORDER_MESSAGE_HEADER_KEY, order)
          .build());

      if (eventAccepted) {
        final OrderState orderState = stateMachine.getState().getId();
        orderRepository.updateOrderState(orderState, order.getId());
        return orderState;
      } else {
        logger.warn(String.format("Could not accept event %s for order %s", orderEvent, order.getId()));
        return order.getOrderState();
      }
    } finally {
      stateMachineService.releaseStateMachine(STATEMACHINE_PREFIX + order.getId());
    }
  }

}
