package com.guilherme.Vehicle_Register.service;

import com.guilherme.Vehicle_Register.entity.Vehicle;
import com.guilherme.Vehicle_Register.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void deveRetornarListaDeVeiculosQuandoSucesso() {
        //  cenário
        List<Vehicle> mockVehicles = Arrays.asList(
                new Vehicle(1L, "ABC", "Modelo 1", "Fabrica 1", 2010),
                new Vehicle(2L, "ACA", "Modelo 2", "Fabrica 1", 2015)
        );
        when(vehicleRepository.findAll()).thenReturn(mockVehicles);

        //  ação
        List<Vehicle> result = vehicleService.getAllVehicles();

        //  verificação
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ABC", result.get(0).getPlate());
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void deveLancarExcecaoQuandoFindAllFalhar() {
        //  cenário
        when(vehicleRepository.findAll()).thenThrow(new RuntimeException("Erro no banco"));

        //  ação e verificação
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            vehicleService.getAllVehicles();
        });

        assertEquals("Não foi possível listar os veículos.", thrown.getMessage());
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverVeiculos() {
        // Lista vazia, cenário
        when(vehicleRepository.findAll()).thenReturn(Collections.emptyList());

        // Ação
        List<Vehicle> result = vehicleService.getAllVehicles();

        // Verificação
        assertNotNull(result);
        assertTrue(result.isEmpty());
        // Verifica se o método do repositório foi chamado.
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void deveCriarVeiculoComSucesso() {
        //cenário
        Vehicle vehicleToSave = new Vehicle(null, "ABC-1234", "Modelo Teste", "Fabrica Teste", 2023);
        Vehicle savedVehicle = new Vehicle(1L, "ABC-1234", "Modelo Teste", "Fabrica Teste", 2023);

        when(vehicleRepository.save(vehicleToSave)).thenReturn(savedVehicle);

        // ação
        Vehicle result = vehicleService.createVehicle(vehicleToSave);

        // verificação
        assertNotNull(result);
        assertEquals(savedVehicle.getId(), result.getId());
        assertEquals(savedVehicle.getPlate(), result.getPlate());
        verify(vehicleRepository, times(1)).save(vehicleToSave);
    }

    @Test
    void deveRetornarListaDeVeiculosFiltradosPorPlaca() {
        // cenário
        Vehicle filter = new Vehicle(null, "ABC", null, null, 0);
        List<Vehicle> mockFilteredVehicles = List.of(
                new Vehicle(1L, "ABC-1234", "Modelo 1", "Fabrica 1", 2010)
        );

        
        when(vehicleRepository.findAll(any(Specification.class))).thenReturn(mockFilteredVehicles);

        // ação
        List<Vehicle> result = vehicleService.getFilteredVehicles(filter);

        // verificação
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ABC-1234", result.get(0).getPlate());
        verify(vehicleRepository, times(1)).findAll(any(Specification.class));
    }
}
