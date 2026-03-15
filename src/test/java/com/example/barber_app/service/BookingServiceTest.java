package com.example.barber_app.service;

import com.example.barber_app.model.Appointment;
import com.example.barber_app.model.ShopService;
import com.example.barber_app.model.User;
import com.example.barber_app.repository.AppointmentRepository;
import com.example.barber_app.repository.ShopServiceRepository;
import com.example.barber_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 * Testing with Mockito, so we are independent of the real database
 * Mock is a fake object that simulate the dependency behavior
 *
 */

//this tells to JUnit to activate Mockito in the test
@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    //@Mock creates fake objects
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShopServiceRepository shopServiceRepository;

    //this creates the BookingService and inserts the mock objects -> so you don't use the real database.
    @InjectMocks
    private BookingService bookingService;


    @Test
    void shouldThrowExceptionWhenBookingInPast() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("Raffo");
        user.setPasswordHash("hashedPassword");
        user.setRole("ROLE_USER");

        ShopService service = new ShopService();
        service.setId(10L);
        service.setName("Taglio");

        //this tells : when the service is looking for Raffo, return a fake object
        when(userRepository.findByUsername("Raffo"))
                .thenReturn(Optional.of(user));

        when(shopServiceRepository.findAllById(List.of(10L)))
                .thenReturn(List.of(service));

        //Act + Assert
        // here the most important part of a JUNIT test: here I expect that it's launched an IllegalArgumentException. If not, test fails.
        assertThrows(IllegalArgumentException.class, () ->
                bookingService.book(
                        "Raffo",
                        LocalDate.now().minusDays(1), // past date
                        LocalTime.of(10, 0),
                        List.of(10L)
                )
        );


    }

    @Test
    void shouldThrowExceptionWhenSelectingMoreThanTwoServices() {
        User user = buildUser();

        when(userRepository.findByUsername("Raffo")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.book(
                        "Raffo",
                        nextNonMonday(),
                        LocalTime.of(10, 0),
                        List.of(10L)
                )
        );
    }





















    // --------------- utility methods used for JUNIT tests purposes ------------------------------------

    private User buildUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Raffo");
        user.setPasswordHash("hashedPassword");
        user.setRole("ROLE_USER");
        return user;
    }

    private ShopService buildService(Long id, String name) {
        ShopService service = new ShopService();
        service.setId(id);
        service.setName(name);
        return service;
    }

    private LocalDate nextNonMonday() {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek().getValue() == 1) {
            date = date.plusDays(1);
        }
        return date;
    }

    private LocalDate nextMonday() {
        LocalDate date = LocalDate.now();
        while (date.getDayOfWeek().getValue() != 1) {
            date = date.plusDays(1);
        }
        return date;
    }


}
