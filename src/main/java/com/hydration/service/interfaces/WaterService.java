package com.hydration.service.interfaces;

import com.hydration.dto.request.AddWaterRequest;
import com.hydration.dto.request.UpdateWaterRequest;
import com.hydration.dto.response.DailySummaryResponse;
import com.hydration.dto.response.WaterResponse;

import java.util.List;

public interface WaterService {
    WaterResponse addWater(AddWaterRequest request);

    WaterResponse updateWater(Long id, UpdateWaterRequest request);

    void deleteWater(Long id);

    List<WaterResponse> getTodayWater();

    List<WaterResponse> getHistory();

    DailySummaryResponse getDailySummary();
}
