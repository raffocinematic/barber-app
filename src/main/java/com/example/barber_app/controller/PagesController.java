package com.example.barber_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.barber_app.service.ShopServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PagesController {

    private final ShopServiceService shopServiceService;

    @GetMapping("/about")
    public String abou() {
        return "pages/about";
    }

    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("hair", shopServiceService.getHairServices());
        model.addAttribute("beard", shopServiceService.getBeardServices());
        model.addAttribute("aesthetic", shopServiceService.getAestheticServices());
        return "pages/services";
    }

}
