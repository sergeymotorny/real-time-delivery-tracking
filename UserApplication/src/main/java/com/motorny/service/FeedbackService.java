package com.motorny.service;

import com.motorny.dto.FeedbackDto;

import java.util.Optional;

public interface FeedbackService {
    Optional<FeedbackDto> getFeedbackShipmentId(Long id);
    void createFeedback(Long shipmentId, FeedbackDto feedbackDto);
}
