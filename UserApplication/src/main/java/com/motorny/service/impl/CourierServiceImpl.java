package com.motorny.service.impl;

import com.motorny.dto.CourierDto;
import com.motorny.exceptions.CourierNotFoundException;
import com.motorny.mappers.CourierMapper;
import com.motorny.repositories.CourierRepository;
import com.motorny.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;

    private final CourierMapper courierMapper;

    @Override
    public CourierDto findById(Long id) {
        return courierRepository.findById(id)
                .map(courierMapper::toCourierDto)
                .orElseThrow(() -> new CourierNotFoundException("Courier not found with id '" + id + "'"));
    }
}
