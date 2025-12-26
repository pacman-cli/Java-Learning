package com.puspo.codearena.businessanalytics.repository;

import com.puspo.codearena.businessanalytics.dto.intermediate.CategorySalesDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.TopProductDto;

import java.util.List;

public interface OrderAnalyticsRepository {
    List<CategorySalesDto> fetchCategorySales();

    List<TopProductDto> fetchTopProducts();
}
