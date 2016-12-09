package com.github.ulischulte.statemachineplayground.statemachine;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.repository.OrderRepository;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
public class OrderStateMachine {

  private final Log logger = LogFactory.getLog(this.getClass());

  private final static String ORDER_MESSAGE_HEADER_KEY = "order";

  private OrderRepository orderRepository;
  private StateMachine<OrderState, OrderEvent> stateMachine;

  @Autowired
  public OrderStateMachine(final OrderRepository orderRepository, final StateMachine<OrderState, OrderEvent> stateMachine) {
    this.orderRepository = orderRepository;
    this.stateMachine = stateMachine;
  }

  public OrderState sendOrderEvent(final Order order, final OrderEvent orderEvent){
    resetStateMachine(order);

    final boolean eventAccepted = stateMachine.sendEvent(MessageBuilder.withPayload(orderEvent)
        .setHeader(ORDER_MESSAGE_HEADER_KEY, order)
        .build());

    if (eventAccepted) {
      final OrderState orderState = stateMachine.getState().getId();
      orderRepository.updateOrderState(orderState, order.getId());
      return orderState;
    } else {
      logger.warn("Could not accept Event " + orderEvent + " for Order " + order.getId());
      return order.getOrderState();
    }
  }

  private void resetStateMachine(Order order) {
    stateMachine.stop();
    stateMachine.getStateMachineAccessor()
        .withAllRegions()
        .forEach(a -> a
            .resetStateMachine(new DefaultStateMachineContext<OrderState, OrderEvent>(order.getOrderState(), null, null, null)));
    stateMachine.start();
  }
}
