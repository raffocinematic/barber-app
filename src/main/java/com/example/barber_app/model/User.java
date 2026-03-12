package com.example.barber_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint (name = "uq_users_username", columnNames  = "username")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(nullable = false, length =  60, unique = true)
        private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    //this is necessary for Spring Security
    @Column(nullable = false, length = 30)
    private String role;

}
