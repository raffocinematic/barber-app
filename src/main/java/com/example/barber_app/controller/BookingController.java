package com.example.barber_app.controller;


import com.example.barber_app.dto.BookingForm;
import com.example.barber_app.service.BookingService;
import com.example.barber_app.service.ShopServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Step 1 : services choice
 * Step 2 : date choice
 * Step 3 : slot choice
 * Step 4 : confirm the booking
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;
    private final ShopServiceService shopServiceService;

    /**
     * This shows the booking page : it's the first page where the services are shown.
     *
     * @param model , an empty BookingForm and services (divided by category)
     * @return
     */
    @GetMapping
    public String bookingHome(Model model) {
        BookingForm form = new BookingForm();

        model.addAttribute("form", form);
        model.addAttribute("hairServices", shopServiceService.getHairServices());
        model.addAttribute("beardServices", shopServiceService.getBeardServices());
        model.addAttribute("aestheticServices", shopServiceService.getAestheticServices());

        return "booking/landing";
    }

    /**
     * It receives the services list from the first page.
     * It checks that we have at least 1 service, and that we don't have more than 2 services.
     * This is a mandatory business rule , we need to stop the flow here, if necessary.
     *
     * @param form
     * @param br
     * @param model
     * @return
     */
    @PostMapping("/date")
    public String chooseDate(@ModelAttribute("form") @Valid BookingForm form,
                             BindingResult br,
                             Model model) {

        if (form.getServiceIds() == null || form.getServiceIds().isEmpty()) {
            model.addAttribute("error", "You must select at least one service.");
            model.addAttribute("hairServices", shopServiceService.getHairServices());
            model.addAttribute("beardServices", shopServiceService.getBeardServices());
            model.addAttribute("aestheticServices", shopServiceService.getAestheticServices());
            return "booking/landing";
        }

        if (form.getServiceIds().size() > 2) {
            model.addAttribute("error", "You can select a maximum of two services.");
            model.addAttribute("hairServices", shopServiceService.getHairServices());
            model.addAttribute("beardServices", shopServiceService.getBeardServices());
            model.addAttribute("aestheticServices", shopServiceService.getAestheticServices());
            return "booking/landing";
        }

        //If everything goes well, it sets a default date, then we go to the next page.
        form.setDate(LocalDate.now());
        model.addAttribute("form", form);
        return "booking/choose-date";
    }

    /**
     * It receives choosen services and date
     *
     *
     * @param form
     * @param br
     * @param model
     * @return
     */

    @PostMapping("/slots")
    public String chooseSlot(@ModelAttribute("form") @Valid BookingForm form,
                             BindingResult br,
                             Model model) {

        if (br.hasErrors()) {
            return "booking/choose-date";
        }

        try {
            List<LocalTime> slots = bookingService.availableSlots(form.getDate(), form.getServiceIds());
            model.addAttribute("form", form);
            model.addAttribute("slots", slots);
            return "booking/choose-slot";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("form", form);
            return "booking/choose-date";
        }
    }

    /**
     * This creates the appointment.
     *
     * @param form
     * @param auth
     * @param model
     * @return
     */

    @PostMapping("/confirm")
    public String confirm(@ModelAttribute("form") BookingForm form,
                          Authentication auth,
                          Model model) {
        try {
            var appointment = bookingService.book(
                    //this is the bridge between authentication and applicative
                    auth.getName(),
                    form.getDate(),
                    LocalTime.parse(form.getStartTime()),
                    form.getServiceIds()
            );

            model.addAttribute("appointment", appointment);
            return "booking/confirm";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());

            model.addAttribute("slots",
                    bookingService.availableSlots(form.getDate(), form.getServiceIds()));

            model.addAttribute("form", form);
            return "booking/choose-slot";
        }
    }

    /**
     * This is used for showing the appointment based on the logged user.
     * This controller does not receive userId from the client, but it's retrieved from the authentication session.
     *
     * @param auth
     * @param model
     * @return
     */

    @GetMapping("/mine")
    public String mine(Authentication auth, Model model) {
        var appointments = bookingService.myAppointments(auth.getName());

        System.out.println("LOGGED USER = " + auth.getName());
        System.out.println("APPOINMENTS FOUND = " + appointments.size());

        model.addAttribute("appoinments", appointments);
        return "booking/my-appointments";
    }


}
