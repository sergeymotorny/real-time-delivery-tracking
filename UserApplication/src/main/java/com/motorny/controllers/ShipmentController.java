package com.motorny.controllers;

import com.motorny.models.Shipment;
import com.motorny.models.enums.ShipmentStatus;
import com.motorny.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
    public String showOrders(Model model) {
        model.addAttribute("shipments", shipmentService.getAllShipments());
        return "/user/orders";
    }


    @GetMapping("/order-courier")
    public String showFormOrderCourier(@ModelAttribute("shipment") Shipment shipment) {
        return "/user/order-courier";
    }

    @PostMapping("/save")
    public String createOrder(@Valid @ModelAttribute("shipment") Shipment shipment, Principal principal,
                              BindingResult result, Model model) {
        if (result.hasErrors())
            return "/user/order-courier";


        shipmentService.createShipment(shipment, principal);
        System.out.println("redirect");
        return "redirect:/users/home";
    }
}
