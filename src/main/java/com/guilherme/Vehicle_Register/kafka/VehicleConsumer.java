package com.guilherme.Vehicle_Register.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.dao.DataIntegrityViolationException;

import com.guilherme.Vehicle_Register.entity.Vehicle;
import com.guilherme.Vehicle_Register.service.VehicleService;

@Component
public class VehicleConsumer {

    private static final Logger log = LoggerFactory.getLogger(VehicleConsumer.class);
    private final VehicleService vehicleService;

    public VehicleConsumer(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @KafkaListener(topics = "vehicle-topic", groupId = "vehicle-group")
    public void consume(Vehicle vehicle) {
        log.info("CONSUMER: Mensagem recebida do Kafka -> Veículo placa: {}", vehicle.getPlate());
        log.info("CONSUMER: Mensagem recebida do Kafka -> Processando veículo placa: {}", vehicle.getPlate());
        try {
            vehicleService.createVehicle(vehicle);
            log.info("CONSUMER: Veículo placa {} salvo com sucesso no banco de dados.", vehicle.getPlate());
        } catch (DataIntegrityViolationException e) {
            log.error("CONSUMER: Erro ao salvar veículo. A placa '{}' já existe.", vehicle.getPlate());
        } catch (Exception e) {
            log.error("CONSUMER: Erro inesperado ao processar veículo placa {}: {}", vehicle.getPlate(), e.getMessage());
        }
    }
}
