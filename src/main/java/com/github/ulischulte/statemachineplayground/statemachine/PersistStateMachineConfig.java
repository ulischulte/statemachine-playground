package com.github.ulischulte.statemachineplayground.statemachine;

import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
public class PersistStateMachineConfig {

  @Bean
  public StateMachineRuntimePersister<OrderState, OrderEvent, String> stateMachineRuntimePersister(
      JpaStateMachineRepository jpaStateMachineRepository) {
    return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
  }

  @Bean
  public StateMachineService<OrderState, OrderEvent> stateMachineService(
      StateMachineFactory<OrderState, OrderEvent> stateMachineFactory,
      StateMachineRuntimePersister<OrderState, OrderEvent, String> stateMachineRuntimePersister) {
    return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
  }
}
