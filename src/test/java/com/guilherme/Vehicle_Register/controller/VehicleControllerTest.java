package com.guilherme.Vehicle_Register.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guilherme.Vehicle_Register.entity.Vehicle;
import com.guilherme.Vehicle_Register.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private KafkaTemplate<String, Vehicle> kafkaTemplate;

    @Test
    void deveRetornarListaDeVeiculos() throws Exception {
        // cenário
        List<Vehicle> vehicles = List.of(new Vehicle(1L, "OYY3545", "Modelo 1", "Fabrica 1", 2005));
        when(vehicleService.getAllVehicles()).thenReturn(vehicles);

        // ação e verificação
        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].plate").value("OYY3545"))
                .andExpect(jsonPath("$[0].model").value("Modelo 1"));
    }

    @Test
    void deveRetornarListaDeVeiculosFiltrados() throws Exception {
        // cenário
        List<Vehicle> vehiclesFiltered = List.of(
            new Vehicle(2L, "OYY1123", "Modelo 1", "Fabrica 1", 2006)
        );

        when(vehicleService.getFilteredVehicles(any(Vehicle.class))).thenReturn(vehiclesFiltered);

        // ação e verificação
        mockMvc.perform(get("/vehicles/filter")
                        .param("model", "Modelo 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].plate").value("OYY1123"))
                .andExpect(jsonPath("$[0].model").value("Modelo 1"));

    }

    @Test
    void devePublicarVeiculoComSucesso() throws Exception {
        // cenário
        Vehicle vehicleToPublish = new Vehicle(null, "XYZ-7890", "Modelo 3", "Fabrica 3", 2024);

        // ação e verificação
        mockMvc.perform(post("/vehicles/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleToPublish)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Veículo recebido e enviado para processamento."));

        
        verify(kafkaTemplate, times(1)).send("vehicle-topic", vehicleToPublish.getPlate(), vehicleToPublish);
    }
}