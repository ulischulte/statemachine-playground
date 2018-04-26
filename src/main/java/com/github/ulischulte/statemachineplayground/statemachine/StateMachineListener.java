package com.github.ulischulte.statemachineplayground.statemachine;

import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;

import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateMachineListener extends StateMachineListenerAdapter<OrderState, OrderEvent> {

  @Override
  public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
    if (from == null) {
      log.info("Initialized with state {}", to.getId());
    } else {
      log.info("State changed from {} to {}", from.getId(), to.getId());
    }
  }
}