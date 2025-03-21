package com.motorny.service.impl;

import com.motorny.dto.CourierDto;
import com.motorny.mappers.CourierMapper;
import com.motorny.repositories.CourierRepository;
import com.motorny.repositories.UserRepository;
import com.motorny.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final UserRepository userRepository;

    private final CourierMapper courierMapper;

    @Override
    public CourierDto findById(Long id) {
        return null;
    }
}
