package com.github.ulischulte.statemachineplayground.statemachine;

import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class StateMachineListener extends StateMachineListenerAdapter<OrderState, OrderEvent> {

  private final Log logger = LogFactory.getLog(this.getClass());

  @Override
  public void stateChanged(State<OrderState, OrderEvent>  from, State<OrderState, OrderEvent>  to) {
    if (from == null) {
      logger.info(String.format("Initialized with state %s", to.getId()));
    } else {
      logger.info(String.format("State changed from %s to %s", from.getId(), to.getId()));
    }
  }
}