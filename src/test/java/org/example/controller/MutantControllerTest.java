package org.example.controller;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.MutantServiceTest;
import org.example.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MutantController.class)
public class MutantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK si es mutante")
    void testCheckMutant_IsMutant_Returns200() throws Exception{
        when(mutantService.analyzeDna(any())).thenReturn(true);

        String jsonRequest = "{\"dna\":[\"ATCG\",\"CCCC\",\"TTAT\",\"AGAC\"]}";
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden si es humano")
    void testCheckMutant_IsHuman_Returns493() throws Exception{
        when(mutantService.analyzeDna(any())).thenReturn(false);

        String jsonRequest = "{\"dna\":[\"GTGC\",\"CAGT\",\"TTAT\",\"AGAC\"]}";

        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request si el AND es inválido")
    void testCheckMutant_InvalidDna_Returns400() throws Exception{
        when(mutantService.analyzeDna(any())).thenThrow(new IllegalArgumentException("ADN inválido"));
        String jsonRequest = "{\"dna\":[\"ATGC\",\"CCCC\"]}";

        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ADN inválido"));
    }

    @Test
    @DisplayName("GET /stats debe retornar estadísticas correctas")
    void testGetStats_ReturnsJson() throws Exception{
        StatsResponse mockStats = StatsResponse.builder()
                .countMutantDna(40)
                .countHumanDna(100)
                .ratio(0.4)
                .build();

        when(statsService.getStats()).thenReturn(mockStats);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }
}
