package com.motorny.service;

import com.motorny.dto.admin.AnalyticsDto;

public interface AnalyticsService {

    /**
     * Collects and returns all analytics data for the admin dashboard:
     * order counts, courier workload, priority distribution,
     * delivery type split, and orders per day for the last 7 days.
     */
    AnalyticsDto getAnalytics();
}
