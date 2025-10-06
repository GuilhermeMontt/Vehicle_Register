package com.guilherme.Vehicle_Register.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guilherme.Vehicle_Register.entity.Vehicle;
import com.guilherme.Vehicle_Register.service.VehicleService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final KafkaTemplate<String, Vehicle> kafkaTemplate;

    public VehicleController(VehicleService vehicleService, KafkaTemplate<String, Vehicle> kafkaTemplate){
        this.vehicleService = vehicleService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> vehicles(){
        List<Vehicle> vehicleList = vehicleService.getAllVehicles();
        return ResponseEntity.status(HttpStatus.OK).body(vehicleList);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Vehicle>> filterVehicle(
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false, defaultValue = "0") int year) {
        
        Vehicle filter = new Vehicle(null, plate, model, manufacturer, year);
        List<Vehicle> vehicleListFiltered = vehicleService.getFilteredVehicles(filter);
        return ResponseEntity.status(HttpStatus.OK).body(vehicleListFiltered);
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishVehicle(@Valid @RequestBody Vehicle vehicle){
        // Envia para o kafka objeto recebido
        kafkaTemplate.send("vehicle-topic", vehicle.getPlate(), vehicle);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Veículo recebido e enviado para processamento.");
    }
}