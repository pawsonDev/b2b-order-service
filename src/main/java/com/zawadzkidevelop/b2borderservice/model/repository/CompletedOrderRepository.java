package com.zawadzkidevelop.b2borderservice.model.repository;

import com.zawadzkidevelop.b2borderservice.model.entity.CompletedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedOrderRepository extends JpaRepository<CompletedOrder, Long> {
}
