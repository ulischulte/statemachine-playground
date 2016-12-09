package com.github.ulischulte.statemachineplayground.statemachine;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.repository.OrderRepository;
import com.github.ulischulte.statemachineplayground.repository.OrderRepositoryConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { OrderStateMachineConfig.class, OrderRepositoryConfig.class})
@Transactional
public class OrderStateMachineTest {

  @Autowired
  private OrderStateMachine orderStateMachine;

  @Autowired
  private OrderRepository orderRepository;

  @Test
  public void sendOrderEvent__process_on_initial_sets_state_to_processing() throws Exception {
    final Order order = createOrder(OrderState.INITIAL);
    final OrderState orderState = orderStateMachine.sendOrderEvent(order, OrderEvent.PROCESS);
    assertThat(orderState).isEqualTo(OrderState.PROCESSING);
  }

  @Test
  public void sendOrderEvent__send_on_initial_has_no_effect_on_state() throws Exception {
    final Order order = createOrder(OrderState.INITIAL);
    final OrderState orderState = orderStateMachine.sendOrderEvent(order, OrderEvent.SEND);
    assertThat(orderState).isEqualTo(OrderState.INITIAL);
  }

  private Order createOrder(OrderState state) {
    final Order order = new Order();
    order.setOrderState(state);
    return orderRepository.save(order);
  }

}