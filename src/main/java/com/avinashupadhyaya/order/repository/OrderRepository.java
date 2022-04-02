package com.avinashupadhyaya.order.repository;

import java.util.Optional;

import com.avinashupadhyaya.order.model.Order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    Optional<Order> findById(Long id);
}
