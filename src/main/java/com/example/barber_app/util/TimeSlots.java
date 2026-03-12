package com.example.barber_app.util;

import com.example.barber_app.model.ServiceType;

import java.time.*;
import java.util.*;

public class TimeSlots {

    private TimeSlots() {}

    public static Duration durationOf(ServiceType type) {
        return switch (type) {
            case HAIRCUT -> Duration.ofMinutes(30);
            case BEARD -> Duration.ofMinutes(20);
            case HAIRANDBEARD -> Duration.ofMinutes(50);
        };
    }

    public static List<LocalTime> dailySlots(LocalTime open, LocalTime close, Duration step) {
        List<LocalTime> out = new ArrayList<>();
        LocalTime t = open;
        while (!t.isAfter(close.minus(step))) {
            out.add(t);
            t = t.plus(step);
        }
        return out;
    }
}