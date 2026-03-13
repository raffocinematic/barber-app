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

    private final ShopServiceRepository shopServiceRepository;

    public List<ShopService> getHairServices() {
        return shopServiceRepository.findAllByCategoryOrderByNameAsc(ServiceCategory.HAIR);
    }

    public List<ShopService> getBeardServices() {
        return shopServiceRepository.findAllByCategoryOrderByNameAsc(ServiceCategory.BEARD);
    }

    public List<ShopService> getAestheticServices() {
        return shopServiceRepository.findAllByCategoryOrderByNameAsc(ServiceCategory.AESTHETIC);
    }


}
