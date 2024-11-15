package com.example.demo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Service
@EnableAsync
@PropertySource(value = "classpath:application.yaml")
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;

//    @KafkaListener(topics = "${user.kafka.topic}")
//    public void consume(ConsumerRecord<String, String> kafkaRecord) {
//        log.info("key:{}  value:{}", kafkaRecord.key(), kafkaRecord.value());
//    }

    @PostConstruct
    public void consume() {
        reactiveKafkaConsumerTemplate.receive()
                .doOnNext(receiverRecord -> log.info("Consumed message: key={}, value={}", receiverRecord.key(), receiverRecord.value()))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)).filter(throwable -> throwable instanceof RuntimeException))
                .doOnError(e -> log.error("Error consuming Kafka messages", e))
                .doOnComplete(() -> log.info("Consumer completed"))
                .subscribe();

//        reactiveKafkaConsumerTemplate.receive()
//                .flatMap(receiverRecord -> processMessage(receiverRecord)
//                                .subscribeOn(Schedulers.boundedElastic())
//                                .then(receiverRecord.receiverOffset().commit())
//                                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
//                                        .filter(RuntimeException.class::isInstance))
//                        , 4)
//                .doOnError(e -> log.error("Error consuming Kafka messages", e))
//                .subscribe();
    }

//    private Mono<Object> processMessage(ReceiverRecord<String, String> receiverRecord) {
//    }
}

