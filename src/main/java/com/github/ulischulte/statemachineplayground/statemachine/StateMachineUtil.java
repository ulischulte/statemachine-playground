package com.github.ulischulte.statemachineplayground.statemachine;

import com.github.ulischulte.statemachineplayground.model.Order;
import org.springframework.statemachine.StateContext;

import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;

public class StateMachineUtil {

  final static String ORDER_MESSAGE_HEADER_KEY = "order";

  public static Order extractOrder(StateContext<OrderState, OrderEvent> context) {
    return (Order) context.getMessageHeader(ORDER_MESSAGE_HEADER_KEY);
  }
}
