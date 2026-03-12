package com.example.barber_app.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class BookingForm {

    //from the HTML form it will arrive the service's list of ids
    @NotEmpty
    private List <Long> serviceIds = new ArrayList<>();

    private LocalDate date;

    private String startTime;


}
