package com.example.bike_shop_spring.controllers;

import com.example.bike_shop_spring.models.User;
import com.example.bike_shop_spring.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/my-account")

    public String getMyAccount(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("user", userService.findById(user.getId()));
        return "my-account";
    }
}
