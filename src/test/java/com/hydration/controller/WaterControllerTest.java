package com.hydration.controller;

import com.hydration.dto.request.AddWaterRequest;
import com.hydration.dto.request.UpdateWaterRequest;
import com.hydration.dto.response.DailySummaryResponse;
import com.hydration.dto.response.WaterResponse;
import com.hydration.security.JwtAuthenticationFilter;
import com.hydration.security.JwtService;
import com.hydration.service.interfaces.WaterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WaterController.class)
@AutoConfigureMockMvc(addFilters = false)
class WaterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WaterService waterService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void addWater_shouldReturnCreated() throws Exception {

        WaterResponse response =
                new WaterResponse(
                        1L,
                        500,
                        LocalDateTime.of(2026, 9, 18, 10, 30)
                );

        when(waterService.addWater(any(AddWaterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/water")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "amount": 500
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.consumedAt")
                        .value("2026-09-18T10:30:00"));

        verify(waterService).addWater(any(AddWaterRequest.class));
    }

    @Test
    void addWater_withInvalidAmount_shouldReturnBadRequest()
            throws Exception {

        mockMvc.perform(
                        post("/api/water")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "amount": 0
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWater_shouldReturnOk() throws Exception {

        WaterResponse response =
                new WaterResponse(
                        1L,
                        750,
                        LocalDateTime.of(2026, 9, 18, 10, 30)
                );

        when(waterService.updateWater(
                eq(1L),
                any(UpdateWaterRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/water/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "amount": 750
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(750));

        verify(waterService)
                .updateWater(eq(1L), any(UpdateWaterRequest.class));
    }

    @Test
    void deleteWater_shouldReturnNoContent() throws Exception {

        doNothing().when(waterService).deleteWater(1L);

        mockMvc.perform(
                        delete("/api/water/1")
                )
                .andExpect(status().isNoContent());

        verify(waterService).deleteWater(1L);
    }

    @Test
    void getTodayWater_shouldReturnOk() throws Exception {

        List<WaterResponse> response = List.of(
                new WaterResponse(
                        1L,
                        500,
                        LocalDateTime.of(2026, 9, 18, 9, 0)
                ),
                new WaterResponse(
                        2L,
                        750,
                        LocalDateTime.of(2026, 9, 18, 11, 0)
                )
        );

        when(waterService.getTodayWater())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/water/today")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].amount").value(500))
                .andExpect(jsonPath("$[1].amount").value(750));

        verify(waterService).getTodayWater();
    }

    @Test
    void getHistory_shouldReturnOk() throws Exception {

        List<WaterResponse> response = List.of(
                new WaterResponse(
                        1L,
                        500,
                        LocalDateTime.of(2026, 9, 18, 9, 0)
                )
        );

        when(waterService.getHistory())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/water/history")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(500));

        verify(waterService).getHistory();
    }

    @Test
    void getSummary_shouldReturnOk() throws Exception {

        DailySummaryResponse response =
                new DailySummaryResponse(
                        LocalDate.of(2026, 9, 18),
                        2500,
                        1500,
                        1000,
                        60.0
                );

        when(waterService.getDailySummary())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/water/summary")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-09-18"))
                .andExpect(jsonPath("$.goal").value(2500))
                .andExpect(jsonPath("$.consumed").value(1500))
                .andExpect(jsonPath("$.remaining").value(1000))
                .andExpect(jsonPath("$.progressPercentage").value(60.0));

        verify(waterService).getDailySummary();
    }
}