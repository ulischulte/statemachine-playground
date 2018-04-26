package com.github.ulischulte.statemachineplayground.statemachine.actions;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.statemachine.StateMachineUtil;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProduceOrderAction implements Action<OrderState, OrderEvent> {

  @Override
  public void execute(StateContext<OrderState, OrderEvent> context) {
    final Order order = StateMachineUtil.extractOrder(context);
    log.info("Producing {}", order);
  }
}
