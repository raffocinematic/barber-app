package com.example.barber_app.service;

import com.example.barber_app.model.Appointment;
import com.example.barber_app.model.ShopService;
import com.example.barber_app.model.User;
import com.example.barber_app.repository.AppointmentRepository;
import com.example.barber_app.repository.ShopServiceRepository;
import com.example.barber_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class BookingService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ShopServiceRepository shopServiceRepository;

    /**
     *
     * @param date
     * @param serviceIds
     * @return
     */

    public List<LocalTime> availableSlots(LocalDate date, List<Long> serviceIds) {
        validateWorkingDay(date);

        List<ShopService> services = loadAndValidateServices(serviceIds);
        int durationMinutes = calculateDurationMinutes(services);

        LocalTime open = LocalTime.of(9, 0);
        LocalTime close = LocalTime.of(18, 0);
        Duration step = Duration.ofMinutes(30);
        Duration appointmentDuration = Duration.ofMinutes(durationMinutes);

        List<LocalTime> allSlots = new ArrayList<>();
        LocalTime current = open;

        while (!current.plus(appointmentDuration).isAfter(close)) {
            allSlots.add(current);
            current = current.plus(step);
        }

        List<Appointment> bookedAppointments = appointmentRepository.findOverlapping(
                date.atTime(open),
                date.atTime(close)
        );

        //with this, you should not be allowed to see past slots because as peer business logic you can't book in the past.
        //better to filter this in the backend logic and not in the UX.
        LocalDateTime now = LocalDateTime.now();

        return allSlots.stream()
                .filter(slot -> LocalDateTime.of(date, slot).isAfter(now))
                .filter(slot -> isFree(date, slot, appointmentDuration, bookedAppointments))
                .toList();
    }

    /**
     *Fundamental business logic here!
     *
     * @param username
     * @param date
     * @param start
     * @param serviceIds
     * @return
     */

    @Transactional
    public Appointment book(String username, LocalDate date, LocalTime start, List<Long> serviceIds) {

        validateWorkingDay(date);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not valid."));

        List<ShopService> services = loadAndValidateServices(serviceIds);
        int durationMinutes = calculateDurationMinutes(services);

        LocalDateTime startDateTime = LocalDateTime.of(date, start);
        LocalDateTime endDateTime = startDateTime.plusMinutes(durationMinutes);

        LocalTime opening = LocalTime.of(9, 0);
        LocalTime closing = LocalTime.of(18, 0);

        // 1. block past appointments
        if(!startDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("You cannot book an appointment in the past.");
        }

        // 2. block appointments outside working hours
        if (start.isBefore(opening) || endDateTime.toLocalTime().isAfter(closing)) {
            throw new IllegalArgumentException("Selected slot is outside working hours.");
        }

        // 3. block overlapping appointments
        if (!appointmentRepository.findOverlapping(startDateTime, endDateTime).isEmpty()) {
            throw new IllegalStateException("Slot no longer available.");
        }

        Appointment appointment = new Appointment();
        appointment.setCustomer(user);
        appointment.setStartTime(startDateTime);
        appointment.setEndTime(endDateTime);
        appointment.setDurationMinutes(durationMinutes);
        appointment.setStatus("BOOKED");
        appointment.setServices(services);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> myAppointments(String username) {
        System.out.println("MY APPOINTMENTS USERNAME = " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        System.out.println("MY APPOINTMENTS USERNAME = " + username);

        return appointmentRepository.findByUserId(user.getId());
    }

    /**
     * This is a crucial business logic for the app. It makes 3 controls , at least 1 service (max 2) and all services must exist in the db.
     * @param serviceIds
     * @return
     */

    private List<ShopService> loadAndValidateServices(List<Long> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            throw new IllegalArgumentException("You must select at least one service.");
        }

        if (serviceIds.size() > 2) {
            throw new IllegalArgumentException("You can select a maximum of two services.");
        }

        List<ShopService> services = shopServiceRepository.findAllById(serviceIds);

        if (services.size() != serviceIds.size()) {
            throw new IllegalArgumentException("One or more selected services do not exist.");
        }

        return services;
    }

    /**
     * business rule here : 1 service = 30 minutes, 2 services = 60 minutes.
     * it's not a UI details, but a business rule so it must be in the service.
     *
     * @param services
     * @return
     */
    private int calculateDurationMinutes(List<ShopService> services) {
        if (services.size() == 1) {
            return 30;
        }

        if (services.size() == 2) {
            return 60;
        }

        throw new IllegalArgumentException("Invalid number of services selected.");
    }

    /**
     * this is another important business rule.
     * @param date
     */
    private void validateWorkingDay(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required.");
        }

        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            throw new IllegalArgumentException("The barber shop is closed on Monday.");
        }
    }

    private boolean isFree(LocalDate date,
                           LocalTime start,
                           Duration duration,
                           List<Appointment> booked) {

        LocalDateTime slotStart = LocalDateTime.of(date, start);
        LocalDateTime slotEnd = slotStart.plus(duration);

        return booked.stream()
                .noneMatch(a -> a.getStartTime().isBefore(slotEnd) && a.getEndTime().isAfter(slotStart));
    }
}