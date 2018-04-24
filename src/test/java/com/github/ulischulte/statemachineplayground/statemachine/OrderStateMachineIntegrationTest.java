package com.github.ulischulte.statemachineplayground.statemachine;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ulischulte.statemachineplayground.StatemachineplaygroundApplication;
import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderEvent;
import com.github.ulischulte.statemachineplayground.model.OrderState;
import com.github.ulischulte.statemachineplayground.repository.OrderRepository;

import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StatemachineplaygroundApplication.class })
@Transactional
public class OrderStateMachineIntegrationTest {

  @Rule
  public JUnitSoftAssertions softly = new JUnitSoftAssertions();

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
    final Order order = createOrder(OrderState.INITIAL)
        .toBuilder()
        .product("SCARCE_ITEM")
        .build();
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
  public void test_multiple_events() {
    final Order order = createOrder(OrderState.INITIAL);
    final OrderState stateProcess = orderStateMachine.sendOrderEvent(order, OrderEvent.PROCESS);
    softly.assertThat(stateProcess).isEqualTo(OrderState.PROCESSING);

    final OrderState stateSend = orderStateMachine.sendOrderEvent(order, OrderEvent.SEND);
    softly.assertThat(stateSend).isEqualTo(OrderState.SENT);

    final OrderState stateDeliver = orderStateMachine.sendOrderEvent(order, OrderEvent.DELIVER);
    softly.assertThat(stateDeliver).isEqualTo(OrderState.DELIVERED);
  }

  private Order createOrder(OrderState state) {
    final Order order = Order.builder()
        .orderState(state)
        .build();

    return orderRepository.save(order);
  }

}