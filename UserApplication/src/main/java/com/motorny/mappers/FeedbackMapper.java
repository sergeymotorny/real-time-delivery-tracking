package com.motorny.mappers;

import com.motorny.dto.FeedbackDto;
import com.motorny.models.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FeedbackMapper {

    FeedbackDto toFeedbackDto(Feedback feedback);

    Feedback toFeedback(FeedbackDto feedbackDto);
}
