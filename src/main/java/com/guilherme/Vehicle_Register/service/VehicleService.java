package com.guilherme.Vehicle_Register.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.guilherme.Vehicle_Register.entity.Vehicle;
import com.guilherme.Vehicle_Register.repository.VehicleRepository;


@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    // Injeção de dependência via construtor (prática recomendada)
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(Vehicle vehicle){
        try {
            // O método save() retorna a entidade salva, incluindo o ID gerado
            return vehicleRepository.save(vehicle);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Erro: A placa '" + vehicle.getPlate() + "' já existe no sistema.");
        }
    }
}
