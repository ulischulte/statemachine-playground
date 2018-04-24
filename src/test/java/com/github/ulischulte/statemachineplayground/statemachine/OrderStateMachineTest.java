package com.github.ulischulte.statemachineplayground.statemachine;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ulischulte.statemachineplayground.StatemachineplaygroundApplication;
import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.repository.OrderRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StatemachineplaygroundApplication.class })
@Transactional
public class OrderStateMachineTest {

  @Autowired
  private OrderStateMachine orderStateMachine;

  @Autowired
  private OrderRepository orderRepository;

  @Test
  public void sendOrderEvent__process_on_initial_sets_state_to_processing() {
    final Order order = createOrder(OrderState.INITIAL);
    final OrderState orderState = orderStateMachine.sendOrderEvent(order, OrderEvent.PROCESS);
    assertThat(orderState).isEqualTo(OrderState.PROCESSING);
  }

  @Test
  public void sendOrderEvent__process_results_in_waiting_for_production_if_scarce_item() {
    final Order order = createOrder(OrderState.INITIAL);
    order.setProduct("SCARCE_ITEM");
    final OrderState orderState = orderStateMachine.sendOrderEvent(order, OrderEvent.PROCESS);
    assertThat(orderState).isEqualTo(OrderState.WAITING_FOR_PRODUCTION);
  }

  @Test
  public void sendOrderEvent__send_on_initial_has_no_effect_on_state() {
    final Order order = createOrder(OrderState.INITIAL);
    final OrderState orderState = orderStateMachine.sendOrderEvent(order, OrderEvent.SEND);
    assertThat(orderState).isEqualTo(OrderState.INITIAL);
  }

  @Test
  public void test() {
    final Order order = createOrder(OrderState.INITIAL);
    final OrderState orderState = orderStateMachine.sendOrderEvent(order, OrderEvent.PROCESS);
    assertThat(orderState).isEqualTo(OrderState.PROCESSING);

    final OrderState orderState1 = orderStateMachine.sendOrderEvent(order, OrderEvent.SEND);
    assertThat(orderState1).isEqualTo(OrderState.SENT);
  }

  private Order createOrder(OrderState state) {
    final Order order = new Order();
    order.setOrderState(state);
    return orderRepository.save(order);
  }

}