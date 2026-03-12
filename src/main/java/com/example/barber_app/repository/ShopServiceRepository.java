package com.example.barber_app.repository;

import com.example.barber_app.model.ServiceCategory;
import com.example.barber_app.model.ShopService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopServiceRepository extends  JpaRepository<ShopService, Long> {

    List<ShopService> findAllByCategoryOrderByNameAsc(ServiceCategory category);

    boolean existsByCategoryAndName(ServiceCategory category, String name);
}
