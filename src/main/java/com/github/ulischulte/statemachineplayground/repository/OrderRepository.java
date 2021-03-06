package com.github.ulischulte.statemachineplayground.repository;

import com.github.ulischulte.statemachineplayground.model.Order;
import com.github.ulischulte.statemachineplayground.model.OrderState;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface OrderRepository extends JpaRepository<Order, Integer> {

  Optional<Order> findById(Integer id);

  @Query("update Order o set o.orderState = :orderState where o.id = :id")
  @Modifying
  void updateOrderState(@Param("orderState") OrderState orderState, @Param("id") Integer id);
}
