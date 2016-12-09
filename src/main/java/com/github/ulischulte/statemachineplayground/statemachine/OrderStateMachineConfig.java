package com.github.ulischulte.statemachineplayground.statemachine;

import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

import static com.github.ulischulte.statemachineplayground.model.OrderState.*;

@Configuration
@ComponentScan(basePackageClasses=OrderStateMachineConfig.class)
@EnableStateMachine
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {

  @Override
  public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states)
      throws Exception {
    states
        .withStates()
        .initial(INITIAL)
        .states(EnumSet.allOf(OrderState.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions)
      throws Exception {
    // TODO: choices, actions, guards...
    transitions
        .withExternal()
        .source(INITIAL).target(PROCESSING)
        .event(OrderEvent.PROCESS)
        .and()
        .withExternal()
        .source(PROCESSING).target(SENT)
        .event(OrderEvent.SEND)
        .and()
        .withExternal()
        .source(SENT).target(DELIVERED)
        .event(OrderEvent.DELIVER);
  }
}
