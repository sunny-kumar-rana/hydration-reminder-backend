package com.hydration.controller;

import com.hydration.dto.request.AddWaterRequest;
import com.hydration.dto.request.UpdateWaterRequest;
import com.hydration.dto.response.DailySummaryResponse;
import com.hydration.dto.response.WaterResponse;
import com.hydration.service.interfaces.WaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/water")
@RequiredArgsConstructor
public class WaterController {

    private final WaterService waterService;

    @PostMapping
    public ResponseEntity<WaterResponse> addWater(
            @Valid @RequestBody AddWaterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(waterService.addWater(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaterResponse> updateWater(
            @PathVariable Long id,
            @Valid @RequestBody UpdateWaterRequest request) {

        return ResponseEntity.ok(
                waterService.updateWater(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWater(
            @PathVariable Long id) {

        waterService.deleteWater(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/today")
    public ResponseEntity<List<WaterResponse>> getTodayWater() {

        return ResponseEntity.ok(
                waterService.getTodayWater()
        );
    }

    @GetMapping("/history")
    public ResponseEntity<List<WaterResponse>> getHistory() {

        return ResponseEntity.ok(
                waterService.getHistory()
        );
    }

    @GetMapping("/summary")
    public ResponseEntity<DailySummaryResponse> getSummary() {

        return ResponseEntity.ok(
                waterService.getDailySummary()
        );
    }
}