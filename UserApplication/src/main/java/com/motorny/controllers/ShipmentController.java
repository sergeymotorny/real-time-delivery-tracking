package com.motorny.controllers;

import com.motorny.dto.ShipmentDto;
import com.motorny.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@AllArgsConstructor
@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping("/orders")
    public String showShipments(Model model) {
        model.addAttribute("shipments", shipmentService.getAllShipments());
        return "/user/orders";
    }

    @GetMapping("/order-courier")
    public String showFormShipmentCourier(Model model) {
        model.addAttribute("shipment", new ShipmentDto());
        return "/user/order-courier";
    }

    @PostMapping("/save")
    public String createShipment(@Valid @ModelAttribute("shipment") ShipmentDto shipmentDto, BindingResult result,
                                 Principal principal, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("shipment", shipmentDto);
            return "/user/order-courier";
        }

        shipmentService.createShipment(shipmentDto, principal);
        return "redirect:/users/home";
    }
}



