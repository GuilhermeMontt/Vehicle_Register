package com.guilherme.Vehicle_Register.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.guilherme.Vehicle_Register.entity.Vehicle;
import com.guilherme.Vehicle_Register.service.VehicleService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public String debug(){
        return "Aplicação está rodando!";
    }

    @PostMapping("/create")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle){
        // 1. Simplificação: O objeto 'vehicle' já vem pronto do corpo da requisição.
        //    Não é necessário criar um novo e copiar os campos.
        
        // 2. Chamada ao serviço
        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        // 3. Retorna uma resposta HTTP 201 Created com o veículo salvo no corpo.
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }
}
