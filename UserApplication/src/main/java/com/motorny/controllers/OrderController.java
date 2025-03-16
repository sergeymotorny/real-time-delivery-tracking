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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/admin")
    public String getAllOrdersForAdmin(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "adm/orders";
    }

    @GetMapping
    public String getAllOrdersByUser(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("orders", orderService.getOrdersByUser(userDetails.getUsername()));
        return "user/orders";
    }

    @GetMapping("/courier/form")
    public String showFormCourierOrder(Model model) {
        model.addAttribute("order", new OrderDto());
        return "user/order-courier";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("order") OrderDto orderDto,
                              BindingResult result,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("order", orderDto);
            return "user/order-courier";
        }

        orderService.createOrder(orderDto, userDetails);
        return "redirect:/users/home";
    }
}
