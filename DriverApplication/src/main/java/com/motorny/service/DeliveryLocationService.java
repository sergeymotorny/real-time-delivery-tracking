package com.motorny.service;

import com.motorny.constant.AppConstant;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeliveryLocationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DeliveryLocationService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean updateLocation(String location) {
        kafkaTemplate.send(AppConstant.DELIVERY_LOCATION, location);
        return true;
    }
}
