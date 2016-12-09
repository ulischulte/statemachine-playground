package com.github.ulischulte.statemachineplayground.statemachine.actions;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.statemachine.StateMachineUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import org.springframework.stereotype.Component;

@Component
public class ProduceOrderAction implements Action<OrderState, OrderEvent> {

  private final Log logger = LogFactory.getLog(this.getClass());

  @Override
  public void execute(StateContext<OrderState, OrderEvent> context) {
    final Order order = StateMachineUtil.extractOrder(context);
    logger.info("Producing " + order);
  }
}
