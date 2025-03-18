package com.motorny.controllers;

import com.motorny.dto.ShipmentDto;
import com.motorny.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    /*
        Method for displaying page from Courier Orders lists
     */

    /*
        The method when the courier will press "Take Order"
     */


    @GetMapping
    public String show(Model model) {
        model.addAttribute("shipments", new ShipmentDto());
        return "/courier/courier_orders";
    }

    @PostMapping("/create")
    public String createShipment(@Valid @ModelAttribute("shipment") ShipmentDto shipmentDto, BindingResult result,
                                 Long orderId, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("shipment", shipmentDto);
            return "/courier/courier_orders";
        }

        shipmentService.createShipmentForOrder(shipmentDto, orderId, userDetails);
        return "redirect:/courier/home";
    }
}