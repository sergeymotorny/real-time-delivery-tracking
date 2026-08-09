package com.motorny.service.impl;

import com.motorny.dto.FeedbackDto;
import com.motorny.mappers.FeedbackMapper;
import com.motorny.models.Feedback;
import com.motorny.models.Shipment;
import com.motorny.models.User;
import com.motorny.repositories.FeedbackRepository;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.repositories.UserRepository;
import com.motorny.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ShipmentRepository shipmentRepository;
    private final UserRepository userRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public Optional<FeedbackDto> getFeedbackShipmentId(Long id) {
        return feedbackRepository.findByShipmentId(id)
                .map(feedbackMapper::toFeedbackDto);
    }

    @Override
    public void createFeedback(Long shipmentId, FeedbackDto feedbackDto, UserDetails userDetails) {
        Shipment shipment = shipmentRepository.getReferenceById(shipmentId);
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Feedback saveFeedback = Feedback.builder()
                .shipment(shipment)
                .user(user)
                .comment(feedbackDto.getComment())
                .rating(feedbackDto.getRating())
                .build();

        feedbackRepository.save(saveFeedback);
    }
}
