package com.guilherme.Vehicle_Register.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.guilherme.Vehicle_Register.entity.Vehicle;
import com.guilherme.Vehicle_Register.repository.VehicleRepository;


@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final KafkaTemplate<String, Vehicle> kafkaTemplate;

    // Injeção de dependência via construtor (prática recomendada)
    public VehicleService(VehicleRepository vehicleRepository, KafkaTemplate<String, Vehicle> kafkaTemplate) {
        this.vehicleRepository = vehicleRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Vehicle createVehicle(Vehicle vehicle){
        try {
            // O método save() retorna a entidade salva, incluindo o ID gerado
            Vehicle savedVehicle = vehicleRepository.save(vehicle);

            // Envia uma notificação para o tópico "vehicle-topic" no Kafka
            // A chave da mensagem será a placa do veículo
            kafkaTemplate.send("vehicle-topic", savedVehicle.getPlate(), savedVehicle);

            return savedVehicle;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Erro: A placa '" + vehicle.getPlate() + "' já existe no sistema.");
        }
    }

    public List<Vehicle> getAllVehicles() {
        // O método findAll() é fornecido pelo JpaRepository e retorna todos os registros da tabela.
        try{
            return vehicleRepository.findAll();
        } catch(Exception e){
            throw new RuntimeException("Não foi possível listar os veículos.");
        }
    }
}
