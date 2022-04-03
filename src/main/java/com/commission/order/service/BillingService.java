package com.commission.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.commission.order.model.Order;

import org.json.JSONObject;

@Component
public class BillingService {

    private static final Logger logger = LoggerFactory.getLogger(BillingService.class);

    public void createBilling(Order order) {
        String createBillingURL = System.getenv("BILLING_SERVICE_URL") + "/api/v1/billing/new";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject billingJsonObject = new JSONObject();
        billingJsonObject.put("orderId", order.getId());
        billingJsonObject.put("quantity", order.getQuantity());
        billingJsonObject.put("product", order.getProduct());
        billingJsonObject.put("customerId", order.getCustomerId());

        HttpEntity<String> request = 
            new HttpEntity<String>(billingJsonObject.toString(), headers);
        
        String billingResultAsJsonStr = 
        restTemplate.postForObject(createBillingURL, request, String.class);

        logger.info(billingResultAsJsonStr);
    }
}
