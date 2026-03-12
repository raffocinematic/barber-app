package com.example.barber_app.controller;


import com.example.barber_app.dto.BookingForm;

import com.example.barber_app.model.ServiceType;
import com.example.barber_app.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Controller
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public String landing(Model model) {
        model.addAttribute("services", ServiceType.values());
        return "booking/landing";
    }

    @GetMapping("/date")
    public String chooseDate(@RequestParam ServiceType serviceType, Model model) {
        BookingForm form = new BookingForm();
        form.setServiceType(serviceType);
        form.setDate(LocalDate.now());
        model.addAttribute("form", form);
        return "booking/choose-date";
    }

    @PostMapping("/slots")
    public String chooseSlot(@ModelAttribute("form") @Valid BookingForm form,
                             BindingResult br,
                             Model model) {
        if (br.hasErrors()) return "booking/choose-date";

        List<LocalTime> slots = bookingService.availableSlots(form.getDate(), form.getServiceType());
        model.addAttribute("form", form);
        model.addAttribute("slots", slots);
        return "booking/choose-slot";
    }

    @PostMapping("/confirm")
    public String confirm(@ModelAttribute("form") BookingForm form,
                          Authentication auth,
                          Model model) {
        try {
            var appointment = bookingService.book(
                    auth.getName(),
                    form.getDate(),
                    LocalTime.parse(form.getStartTime()),
                    form.getServiceType()
            );
            model.addAttribute("appointment", appointment);
            return "booking/confirm";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            // ricarico slot
            model.addAttribute("slots", bookingService.availableSlots(form.getDate(), form.getServiceType()));
            return "booking/choose-slot";
        }
    }

    @GetMapping("/mine")
    public String mine(Authentication auth, Model model) {
        model.addAttribute("appointments", bookingService.myAppointments(auth.getName()));
        return "booking/my-appointments";
    }
}
