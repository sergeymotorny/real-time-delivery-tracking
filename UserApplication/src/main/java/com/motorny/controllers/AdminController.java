package com.motorny.controllers;

import com.motorny.service.AssignmentService;
import com.motorny.service.OrderService;
import com.motorny.service.PriorityService;
import com.motorny.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final PriorityService priorityService;

    @GetMapping("/profile")
    public String getAdminProfile() {
        return "user/profile";
    }

    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @GetMapping("/orders/priority")
    public String getOrdersByPriority(Model model) {
        model.addAttribute("orders", priorityService.getOrdersSortedByPriority());
        model.addAttribute("priorityView", true);
        return "admin/orders";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/find")
    public String findUsers(@RequestParam(name = "role", required = false) String role,
                            @RequestParam(name = "action", required = false) String action,
                            Model model) {
        if ("reset".equals(action)) {
            model.addAttribute("users", userService.getAllUsers());
        } else
            model.addAttribute("users", userService.getUsersByRole(role));

        return "admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.setUserStatusDeleted(id));
        return "redirect:/admin/users";
    }

    @PostMapping("/users/restore")
    public String restoreUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.setUserStatusActive(id));
        return "redirect:/admin/users";
    }

    @PostMapping("/users/inactive")
    public String inactivateUser(@RequestParam("id") Long id) {
        userService.setUserStatusInactive(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/assign-role")
    public String assignRole(@RequestParam("id") Long id,
                             @RequestParam("role") String role) {
        userService.assignRole(id, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/orders/assign")
    public String autoAssignCourier(@RequestParam("orderId") Long orderId) {
        assignmentService.assignCourierToOrder(orderId);
        return "redirect:/admin/orders";
    }
}
