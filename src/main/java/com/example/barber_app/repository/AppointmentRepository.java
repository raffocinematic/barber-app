package com.example.barber_app.repository;
import com.example.barber_app.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        select a from Appointment a
        where a.status = 'BOOKED'
          and a.startTime < :end
          and a.endTime > :start
    """)
    List<Appointment> findOverlapping(LocalDateTime start, LocalDateTime end);

    @Query("""
        select a from Appointment a
        where a.customer.id = :userId
        order by a.startTime desc
    """)
    List<Appointment> findByCustomer(Long userId);
}
