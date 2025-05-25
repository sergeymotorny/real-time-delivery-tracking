package com.motorny.controllers;

import com.motorny.dto.OrderDto;
import com.motorny.service.OrderService;
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
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/courier/form")
    public String showFormCourierOrder(Model model) {
        model.addAttribute("order", new OrderDto());
        return "user/create-order";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("order") OrderDto orderDto,
                              BindingResult result,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("order", orderDto);
            return "user/create-order";
        }

        orderService.createOrder(orderDto, userDetails);
        return "redirect:/home";
    }
}
