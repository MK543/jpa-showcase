package com.example.demo.configuration.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Value(value = "${user.kafka.topic}")
    private String topicName;

    private static final String TRUSTED_PACKAGES = "*";

//    @Bean
//    public ConsumerFactory<String, String> consumerFactory(KafkaProperties kafkaProperties) {
//        log.info("Creating kafka consumer factory");
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
//        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
//        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
//        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
//        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES);
//        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaProperties.getProperties().get(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG));
//        configProps.put(SaslConfigs.SASL_MECHANISM, kafkaProperties.getProperties().get(SaslConfigs.SASL_MECHANISM));
//        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, kafkaProperties.getProperties().get(SaslConfigs.SASL_JAAS_CONFIG));
//        log.info("Kafka consumer properties: {}", configProps);
//        return new DefaultKafkaConsumerFactory<>(configProps);
//    }

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//        return factory;
//    }

    @Bean
    public ReceiverOptions<String, String> kafkaReceiver(KafkaProperties kafkaProperties) {
        log.info("Creating kafka consumer factory");
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES);
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaProperties.getProperties().get(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG));
        configProps.put(SaslConfigs.SASL_MECHANISM, kafkaProperties.getProperties().get(SaslConfigs.SASL_MECHANISM));
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, kafkaProperties.getProperties().get(SaslConfigs.SASL_JAAS_CONFIG));
        log.info("Kafka consumer properties: {}", configProps);

        ReceiverOptions<String, String> basicReceiverOptions = ReceiverOptions.create(configProps);
        return basicReceiverOptions.subscription(Collections.singletonList(topicName));

    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumer(ReceiverOptions<String, String> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }
}
