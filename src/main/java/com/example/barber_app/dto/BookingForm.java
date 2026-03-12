package com.example.barber_app.dto;

import com.example.barber_app.model.ServiceType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class BookingForm {
    @NotNull
    private ServiceType serviceType;

    @NotNull
    private LocalDate date;

    @NotNull
    private String startTime;
}
