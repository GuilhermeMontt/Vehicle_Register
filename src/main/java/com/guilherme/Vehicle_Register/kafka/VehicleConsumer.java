package com.guilherme.Vehicle_Register.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.guilherme.Vehicle_Register.entity.Vehicle;

@Component
public class VehicleConsumer {

    private static final Logger log = LoggerFactory.getLogger(VehicleConsumer.class);

    @KafkaListener(topics = "vehicle-topic", groupId = "vehicle-group")
    public void consume(Vehicle vehicle) {
        log.info("CONSUMER: Mensagem recebida do Kafka -> Veículo placa: {}", vehicle.getPlate());
        // Aqui você poderia, por exemplo, chamar outro serviço para processar o seguro do veículo.
    }
}
