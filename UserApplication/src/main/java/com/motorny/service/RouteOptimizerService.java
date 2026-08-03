package com.motorny.service;

import com.motorny.dto.RouteStepDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface RouteOptimizerService {

    /**
     * Builds an optimized delivery route for the courier
     * using the Nearest Neighbor algorithm.
     *
     * Starting point: depot (46.974429, 32.019642) — Mykolaiv
     * Algorithm: at each step pick the closest unvisited order
     *
     * @param userDetails the currently authenticated courier
     * @return ordered list of route steps
     */
    List<RouteStepDto> buildOptimizedRoute(UserDetails userDetails);
}
