package com.motorny.controllers;

import com.motorny.dto.OrderDto;
import com.motorny.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/map")
public class MapController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public String showOrderOnMap(@PathVariable("id") Long id, Model model) {
        OrderDto order = orderService.findOrderById(id);

        if (!order.isCourierAssigned()) {
            return "redirect:/orders/" + id + "?error=notTaken";
        }

        model.addAttribute("order", order);
        return "map_page";
    }
}
