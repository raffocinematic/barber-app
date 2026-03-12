package com.example.barber_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "shop_services",
        uniqueConstraints = @UniqueConstraint(columnNames = {"category", "name"}))
// so in this way you can have "Rasatura completa" in both "Capelli" and "Barba", not duplicating in the same category
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ServiceCategory category;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_path", length = 255)
    private String imagePath;
}
