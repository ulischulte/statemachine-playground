package com.github.ulischulte.statemachineplayground.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Random;

@Entity
@Table(name = "ORDERS")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(name = "ORDER_STATE")
  private OrderState orderState;

  @Column(name = "PRODUCT")
  private String product;

  public Integer getId() {
    return this.id;
  }

  public OrderState getOrderState() {
    return orderState;
  }

  public void setOrderState(OrderState orderState) {
    this.orderState = orderState;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  @JsonIgnore
  public boolean isInStock() {
    return new Random().nextBoolean();
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", orderState=" + orderState +
        ", product='" + product + '\'' +
        '}';
  }
}
