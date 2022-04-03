package com.commission.order.controllers;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import com.commission.order.model.Order;
import com.commission.order.repository.OrderRepository;
import com.commission.order.service.ProduceService;
import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProduceService produceService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping(value = "/{orderID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getAllRoadmaps(@PathVariable("orderID") String orderID) {
        try {
            Optional<Order> order = orderRepository.findById(Long.parseLong(orderID));
            if(!order.isPresent()) {
                logger.error("No order");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logger.debug("Order {}", order.get().getType());
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Internal error {} ", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @RequestMapping(value = "/new", 
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            Order newOrder = orderRepository.save(order);
            logger.info("New order created with OrderID - {}", newOrder.getId());
            produceService.createProduce(newOrder);
            return new ResponseEntity<>(newOrder, HttpStatus.OK);
        } catch(ConstraintViolationException ex) {
            logger.error("Constraint Validation error {} ", ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            logger.error("Internal error {} ", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/complete", 
            method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> completeOrder(@RequestBody JsonNode completeOrder) {
        try {
            Optional<Order> order = orderRepository.findById(completeOrder.get("orderId").asLong());
            if(!order.isPresent()) {
                logger.error("No order");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            logger.info("Order completed with OrderID - {}", completeOrder.get("orderId").asLong());
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        }catch(Exception e) {
            logger.error("Internal error {} ", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}