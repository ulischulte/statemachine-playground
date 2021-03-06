package com.github.ulischulte.statemachineplayground.statemachine;

import static com.github.ulischulte.statemachineplayground.model.OrderState.*;

import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.statemachine.actions.ProduceOrderAction;
import com.github.ulischulte.statemachineplayground.statemachine.guards.ProductNotInStockGuard;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import java.util.EnumSet;

import lombok.RequiredArgsConstructor;

@Configuration
@ComponentScan(basePackageClasses = { OrderStateMachineConfig.class })
@EnableStateMachineFactory
@RequiredArgsConstructor
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {

  private final ProductNotInStockGuard productNotInStockGuard;
  private final ProduceOrderAction produceOrderAction;
  private final StateMachineRuntimePersister<OrderState, OrderEvent, String> stateMachineRuntimePersister;

  @Override
  public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states)
      throws Exception {
    states
        .withStates()
        .initial(INITIAL)
        .choice(CHOICE_BEGIN_PROCESSING)
        .state(WAITING_FOR_PRODUCTION, produceOrderAction, null)
        .end(OrderState.DELIVERED)
        .states(EnumSet.allOf(OrderState.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions)
      throws Exception {
    transitions
        .withExternal()
        .source(INITIAL)
        .target(CHOICE_BEGIN_PROCESSING)
        .event(OrderEvent.PROCESS)
        .and()
        .withChoice()
        .source(CHOICE_BEGIN_PROCESSING)
        .first(WAITING_FOR_PRODUCTION, productNotInStockGuard)
        .last(PROCESSING)
        .and()
        .withExternal()
        .source(WAITING_FOR_PRODUCTION)
        .action(produceOrderAction)
        .and()
        .withExternal()
        .source(WAITING_FOR_PRODUCTION)
        .target(PROCESSING)
        .event(OrderEvent.PRODUCED)
        .and()
        .withExternal()
        .source(PROCESSING)
        .target(SENT)
        .event(OrderEvent.SEND)
        .and()
        .withExternal()
        .source(SENT)
        .target(DELIVERED)
        .event(OrderEvent.DELIVER);
  }

  @Override
  public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
    config
        .withPersistence()
        .runtimePersister(stateMachineRuntimePersister);
  }
}
