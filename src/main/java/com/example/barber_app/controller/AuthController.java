package com.example.barber_app.controller;

import com.example.barber_app.dto.RegisterForm;
import com.example.barber_app.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("form") @Valid RegisterForm form,
                           BindingResult br,
                           Model model) {
        if (br.hasErrors()) return "auth/register";

        try {
            registrationService.register(form);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "auth/register";
        }

        return "redirect:/login?registered";
    }

}