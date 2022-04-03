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
public class ProduceService {

    private static final Logger logger = LoggerFactory.getLogger(ProduceService.class);

    public void createProduce(Order order) {
        String createProduceURL = System.getenv("PRODUCE_SERVICE_URL") + "/api/v1/produce/new";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject produceJsonObject = new JSONObject();
        produceJsonObject.put("orderId", order.getId());
        produceJsonObject.put("quantity", order.getQuantity());
        produceJsonObject.put("product", order.getProduct());

        HttpEntity<String> request = 
            new HttpEntity<String>(produceJsonObject.toString(), headers);
        
        String produceResultAsJsonStr = 
        restTemplate.postForObject(createProduceURL, request, String.class);

        logger.info(produceResultAsJsonStr);
    }
}
