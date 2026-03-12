package com.example.barber_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/*
 Keep in mind : DTO/Form -> input data's ... Entity = persistent data's
 */

@Getter
@Setter
public class RegisterForm {

    @NotBlank
    @Size(min = 3, max = 60)
    private String username;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    //this is necessary to have it only in the form, it's not a data to save in the database, that's why we don't put it in the entity.
    @NotBlank
    private String confirmPassword;
}

