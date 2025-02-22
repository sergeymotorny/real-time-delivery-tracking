package com.motorny.controllers;

import com.motorny.models.Shipment;
import com.motorny.models.ShipmentStatus;
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

@AllArgsConstructor
@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("shipments", shipmentService.getAllShipments());
        return "home";
    }

    @GetMapping("/order-courier")
    public String showFormOrderCourier(@ModelAttribute("shipment") Shipment shipment) {
        return "form_courier";
    }

    @PostMapping("/save")
    public String createOrder(@Valid @ModelAttribute("shipment") Shipment shipment, BindingResult result, Model model) {
        if (result.hasErrors())
            return "form_courier";

        shipment.setStatus(ShipmentStatus.CREATED);

        shipmentService.create(shipment);
        return "redirect:/home";
    }
}
