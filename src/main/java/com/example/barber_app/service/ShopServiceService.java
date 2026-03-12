package com.example.barber_app.service;

import com.example.barber_app.model.ServiceCategory;
import com.example.barber_app.model.ShopService;
import com.example.barber_app.repository.ShopServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceService {

    private final ShopServiceRepository repo;

    public List<ShopService> getHairServices() {
        return repo.findAllByCategoryOrderByNameAsc(ServiceCategory.HAIR);
    }

    public List<ShopService> getBeardServices() {
        return repo.findAllByCategoryOrderByNameAsc(ServiceCategory.BEARD);
    }

    public List<ShopService> getAestheticServices() {
        return repo.findAllByCategoryOrderByNameAsc(ServiceCategory.AESTHETIC);
    }
}
