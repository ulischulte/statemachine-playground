package com.github.ulischulte.statemachineplayground.model;

public enum OrderState {
  INITIAL(true),
  CHOICE_BEGIN_PROCESSING(false),
  WAITING_FOR_PRODUCTION(true),
  PROCESSING(true),
  SENT(true),
  DELIVERED(true);

  private boolean isNoPseudoState;

  OrderState(boolean isNoPseudoState) {
    this.isNoPseudoState = isNoPseudoState;
  }
}
