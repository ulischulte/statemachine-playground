package com.github.ulischulte.statemachineplayground.model;

public enum OrderState {
  INITIAL,
  CHOICE_BEGIN_PROCESSING,
  WAITING_FOR_PRODUCTION,
  PROCESSING,
  SENT,
  DELIVERED
}
