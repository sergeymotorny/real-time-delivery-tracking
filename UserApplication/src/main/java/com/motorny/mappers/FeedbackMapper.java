package com.motorny.mappers;

import com.motorny.dto.FeedbackDto;
import com.motorny.models.Feedback;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    FeedbackDto toFeedbackDto(Feedback feedback);

    Feedback toFeedback(FeedbackDto feedbackDto);
}
