package com.example.barber_app.repository;

import com.example.barber_app.model.Appointment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    //Very important : used in order to find all the appointments that overlap on the slot you want to book
    @Query("""
        select a 
        from Appointment a
        where a.startTime < :end
        and a.endTime > :start
    """)
    List<Appointment> findOverlapping(LocalDateTime start, LocalDateTime end);


    //this is used in order to show to the user his appointments
    @EntityGraph(attributePaths = {"services"})
    @Query("""
        select a 
        from Appointment a
        where a.customer.id = :userId
        order by a.startTime desc
    """)
    List<Appointment> findByUserId(Long userId);
}
