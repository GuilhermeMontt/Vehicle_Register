package com.guilherme.Vehicle_Register.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guilherme.Vehicle_Register.entity.Vehicle;
import com.guilherme.Vehicle_Register.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VehicleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        // Limpa o repositório antes de cada teste para garantir isolamento
        vehicleRepository.deleteAll();
    }

    @Test
    void deveFiltrarVeiculosComSucesso() throws Exception {
        // Cenário: Inserimos dados diretamente no banco de dados para o teste.
        vehicleRepository.saveAll(List.of(
                new Vehicle(null, "INT-2024", "Modelo Integrado", "Testcontainers", 2024),
                new Vehicle(null, "OUT-2025", "Outro Modelo", "Testcontainers", 2025)
        ));

        // Ação: Fazemos uma chamada GET para o endpoint de filtro.
        mockMvc.perform(get("/vehicles/filter")
                        .param("model", "Integrado") // Filtra por parte do modelo
                        .contentType(MediaType.APPLICATION_JSON))
                // Verificação: Checamos se a resposta está correta.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].plate").value("INT-2024"));
    }
}
