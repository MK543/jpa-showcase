package com.example.demo;

import com.example.demo.kafka.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Service
@PropertySource(value = "classpath:application.yaml")
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    @Value(value = "${user.kafka.topic}")
    private String topicName;

    private final ReactiveKafkaProducerTemplate<String, Object> kafkaTemplate;

    public Mono<SenderResult<Void>> sendMessage(User user) {
        return kafkaTemplate.send(topicName, user)
                .doOnSuccess(result -> log.info("Sent message to Kafka topic {}: {}", topicName, user))
                .doOnError(e -> log.error("Failed to send message to Kafka", e));
    }
}
