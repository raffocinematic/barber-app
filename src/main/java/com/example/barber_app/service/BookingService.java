package com.example.barber_app.service;



// RESTART FROM HERE NEXT TIME !


import com.example.barber_app.model.*;
import com.example.barber_app.repository.AppointmentRepository;
import com.example.barber_app.repository.UserRepository;
import com.example.barber_app.util.TimeSlots;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public List<LocalTime> availableSlots(LocalDate date, ServiceType type) {
        // basic rules : 9:00 - 18:00, step 30 minutes
        LocalTime open = LocalTime.of(9, 0);
        LocalTime close = LocalTime.of(18, 0);
        Duration step = Duration.ofMinutes(30);

        Duration duration = TimeSlots.durationOf(type);
        List<LocalTime> all = TimeSlots.dailySlots(open, close, step);

        //Filtering slots that exceed beyond the closure time, considering the duration
        all = all.stream()
                .filter(t -> !t.plus(duration).isAfter(close))
                .collect(Collectors.toList());

        //Taking appointments of the day ( simple : range day )
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

        //Retrieving all booked, then checking the overlap for each slot (ok for this study-stage, later we can make a more efficient query)
        List<Appointment> booked = appointmentRepository.findOverlapping(dayStart, dayEnd);

        return all.stream()
                .filter(t -> isFree(date, t, duration, booked))
                .toList();
    }

    private boolean isFree(LocalDate date, LocalTime start, Duration duration, List<Appointment> booked) {
        LocalDateTime s = LocalDateTime.of(date, start);
        LocalDateTime e = s.plus(duration);
        return booked.stream().noneMatch(a -> a.getStartTime().isBefore(e) && a.getEndTime().isAfter(s));
    }

    @Transactional
    public Appointment book(String username, LocalDate date, LocalTime start, ServiceType type) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not valid."));

        Duration duration = TimeSlots.durationOf(type);
        LocalDateTime s = LocalDateTime.of(date, start);
        LocalDateTime e = s.plus(duration);

        // controllo overlap in transazione
        if (!appointmentRepository.findOverlapping(s, e).isEmpty()) {
            throw new IllegalStateException("Slot non più disponibile");
        }

        Appointment a = new Appointment();
        a.setCustomer(user);
        a.setServiceType(type);
        a.setStartTime(s);
        a.setEndTime(e);
        a.setStatus("BOOKED");

        return appointmentRepository.save(a);
    }

    public List<Appointment> myAppointments(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow();
        return appointmentRepository.findByCustomer(user.getId());
    }
}
