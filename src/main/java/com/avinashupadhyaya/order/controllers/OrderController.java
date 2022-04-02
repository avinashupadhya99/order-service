package com.avinashupadhyaya.order.controllers;

import com.avinashupadhyaya.order.model.Order;
import com.avinashupadhyaya.order.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping(value = "/new", 
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createUser(@RequestBody Order order) {
        Order newOrder = orderRepository.save(order);
        logger.info("New order created with OrderID - {}", newOrder.getId());
        return new ResponseEntity<>(newOrder, HttpStatus.OK);
    }
}