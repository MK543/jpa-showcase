package com.example.demo;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class SerializationController {

    private final ObjectMapper objectMapper;
    private final KafkaProducerService kafkaProducer;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TransactionService transactionService;

    @PostMapping("serializeDeserialize")
    public ResponseEntity<String> serializeDeserialize(@RequestBody Request request) throws Exception {
        List<String> list = List.of("First", "Second", "Third");
//        List<SampleObject2> object2s = List.of(
//                SampleObject2.builder().build(),
//                SampleObject2.builder().age(5).name("ABC").build(),
//                SampleObject2.builder().age(6).build(),
//                SampleObject2.builder().name("DEF").build());
        SampleObject result = SampleObject.builder()
                .age(10000)
                .name("testObjectSample")
                .active(true)
                .offsetDateTime(OffsetDateTime.now())
                .tags(list)
//                .objects(object2s)
                .build();

        Instant start = Instant.now();
        for (int i = 0; i < request.getTimes(); i++) {
            String jsonString = objectMapper.writeValueAsString(result);
            result = objectMapper.readValue(jsonString, SampleObject.class);
        }
        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();
        String response = "Total time for serialization and deserialization " + request.getTimes() + " times: " + duration + " ms";
        log.info(result.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("serialize")
    public ResponseEntity<String> serialize(@RequestBody Request request) throws JsonProcessingException {
        List<String> list = List.of("First", "Second", "Third");
        List<SampleObject2> object2s = List.of(
                SampleObject2.builder().build(),
                SampleObject2.builder().age(5).name("ABC").build(),
                SampleObject2.builder().age(6).build(),
                SampleObject2.builder().name("DEF").build());
        SampleObject result = SampleObject.builder()
                .age(10000)
                .name("testObjectSample")
                .active(true)
                .offsetDateTime(OffsetDateTime.now())
                .tags(list)
                .objects(object2s)
                .build();

        Instant start = Instant.now();
        for (int i = 0; i < request.getTimes(); i++) {
            String jsonString = objectMapper.writeValueAsString(result);
        }
        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();
        String response = "Total time for serialization " + request.getTimes() + " times: " + duration + " ms";
        return ResponseEntity.ok(response);
    }

    @PostMapping("deserialize")
    public ResponseEntity<String> deserialize(@RequestBody Request request) throws JsonProcessingException {
        List<String> list = List.of("First", "Second", "Third");
        List<SampleObject2> object2s = List.of(
                SampleObject2.builder().build(),
                SampleObject2.builder().age(5).name("ABC").build(),
                SampleObject2.builder().age(6).build(),
                SampleObject2.builder().name("DEF").build());
        SampleObject result = SampleObject.builder()
                .age(10000)
                .name("testObjectSample")
                .active(true)
                .offsetDateTime(OffsetDateTime.now())
                .tags(list)
                .objects(object2s)
                .build();
        String jsonString = objectMapper.writeValueAsString(result);

        Instant start = Instant.now();
        for (int i = 0; i < request.getTimes(); i++) {
            result = objectMapper.readValue(jsonString, SampleObject.class);
        }
        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();
        String response = "Total time for deserialization " + request.getTimes() + " times: " + duration + " ms";
        return ResponseEntity.ok(response);
    }

    @PostMapping("/kafka")
//    public Mono<ResponseEntity<Void>> sendToKafka(@RequestBody List<UserDTO> requests) {
    public ResponseEntity<Void> sendToKafka(@RequestBody List<UserDTO> requests) {
        log.info("Received requests: {}", requests);
        Flux.fromIterable(requests)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(userService::createUser)
                .flatMap(userService::save)
                .flatMap(kafkaProducer::sendMessage)
                .subscribe();
//                .then().thenReturn(ResponseEntity.noContent().build());
//                .subscribe(
//                        null,
//                        error -> log.error("Error processing requests", error),
//                        () -> log.info("All requests processed successfully")
//                );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transaction")
    @SneakyThrows
    public ResponseEntity<Void> postTransaction(@RequestBody TransactionDTO transactionDTO) {
        transactionService.doTransaction(transactionDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(@PathVariable Long id) {
        return ResponseEntity.ok().body(transactionService.getTransactions(id));
    }
}
