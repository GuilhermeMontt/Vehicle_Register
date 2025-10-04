package com.guilherme.Vehicle_Register.controller;

import java.util.List;
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
    public ResponseEntity<List<Vehicle>> vehicles(){
        List<Vehicle> vehicleList = vehicleService.getAllVehicles();
        return ResponseEntity.status(HttpStatus.OK).body(vehicleList);
    }

    @PostMapping("/create")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle){
        // A anotação @Valid agora cuida de toda a validação.
        // Se a validação falhar, o Spring automaticamente retorna um erro 400 Bad Request.
        if(vehicle.getPlate() == null || vehicle.getPlate().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if(vehicle.getModel() == null || vehicle.getModel().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if(vehicle.getManufacturer() == null || vehicle.getManufacturer().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if(vehicle.getYear() <= 0 || vehicle.getYear() > 2025){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 2. Chamada ao serviço
        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        // 3. Retorna uma resposta HTTP 201 Created com o veículo salvo no corpo.
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }
}