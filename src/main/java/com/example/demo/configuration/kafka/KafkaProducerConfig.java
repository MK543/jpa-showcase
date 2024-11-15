package com.example.demo.configuration.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaProducerConfig {

//    @Bean
//    public ProducerFactory<String, Object> producerFactory(KafkaProperties kafkaProperties) {
//        log.info("Creating producer factory");
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaProperties.getProperties().get(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG));
//        configProps.put(SaslConfigs.SASL_MECHANISM, kafkaProperties.getProperties().get(SaslConfigs.SASL_MECHANISM));
//        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, kafkaProperties.getProperties().get(SaslConfigs.SASL_JAAS_CONFIG));
//        log.info("Kafka producer properties: {}", configProps);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducer(KafkaProperties kafkaProperties) {
        log.info("Creating producer factory");
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaProperties.getProperties().get(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG));
        configProps.put(SaslConfigs.SASL_MECHANISM, kafkaProperties.getProperties().get(SaslConfigs.SASL_MECHANISM));
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, kafkaProperties.getProperties().get(SaslConfigs.SASL_JAAS_CONFIG));
        log.info("Kafka producer properties: {}", configProps);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(configProps));
    }

//    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
//        return new KafkaTemplate<>(producerFactory);
//    }
}


