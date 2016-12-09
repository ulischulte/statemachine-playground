package com.github.ulischulte.statemachineplayground.statemachine.guards;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.statemachine.StateMachineUtil;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class ProductNotInStockGuard implements Guard<OrderState, OrderEvent> {

  @Override
  public boolean evaluate(StateContext<OrderState, OrderEvent> context) {
    final Order order = StateMachineUtil.extractOrder(context);
    return !order.isInStock();
  }
}
