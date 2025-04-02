package com.motorny.service.impl;

import com.motorny.dto.FeedbackDto;
import com.motorny.mappers.FeedbackMapper;
import com.motorny.models.Feedback;
import com.motorny.models.Shipment;
import com.motorny.repositories.FeedbackRepository;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ShipmentRepository shipmentRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public Optional<FeedbackDto> getFeedbackShipmentId(Long id) {
        return feedbackRepository.findByShipmentId(id)
                .map(feedbackMapper::toFeedbackDto);
    }

    @Override
    public void createFeedback(Long shipmentId, FeedbackDto feedbackDto) {
        Shipment shipment = shipmentRepository.getReferenceById(shipmentId);

        Feedback saveFeedback = feedbackMapper.toFeedback(feedbackDto);
        saveFeedback.setShipment(shipment);
        saveFeedback.setComment(feedbackDto.getComment());
        saveFeedback.setRating(feedbackDto.getRating());

        feedbackRepository.save(saveFeedback);
    }
}
