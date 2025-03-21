package com.motorny.controllers;

import com.motorny.dto.ShipmentDto;
import com.motorny.service.OrderService;
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
@RequestMapping("/couriers")
public class CourierController {

    private final OrderService orderService;
    private final ShipmentService shipmentService;

    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "courier/orders";
    }

    @GetMapping("/orders/{id}")
    public String getOrderById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "courier/order_full_info";
    }

    @PostMapping("/orders/take")
    public String createShipment(@Valid @ModelAttribute("shipment") ShipmentDto shipment,
                                 BindingResult result,
                                 @RequestParam("orderId") Long orderId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("shipment", shipment);
            return "courier/orders";
        }

        model.addAttribute("orders", shipmentService.createShipmentForOrder(shipment, orderId, userDetails));
        return "redirect:/couriers/orders";
    }
}
