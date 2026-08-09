package com.motorny.controllers;

import com.motorny.dto.FeedbackDto;
import com.motorny.service.FeedbackService;
import com.motorny.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final FeedbackService feedbackService;

    @GetMapping("/{id}/feedback")
    public String showFeedbackForm(@PathVariable Long id, Model model) {
        model.addAttribute("shipmentId", id);
        model.addAttribute("feedbackDto", new FeedbackDto());
        model.addAttribute("existingFeedback", feedbackService.getFeedbackShipmentId(id).orElse(null));
        return "user/feedback";
    }

    @PostMapping("/{id}/feedback")
    public String submitFeedback(@PathVariable Long id,
                                 @Valid @ModelAttribute("feedbackDto") FeedbackDto feedbackDto,
                                 BindingResult result,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("shipmentId", id);
            return "user/feedback";
        }
        feedbackService.createFeedback(id, feedbackDto, userDetails);
        return "redirect:/users/orders?success=feedback";
    }
}
