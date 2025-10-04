package com.guilherme.Vehicle_Register.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic vehicleTopic() {
        return TopicBuilder.name("vehicle-topic").build();
    }
}
