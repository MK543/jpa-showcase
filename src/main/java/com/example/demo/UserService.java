package com.example.demo;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.AddressEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.kafka.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public Mono<UserEntity> createUser(UserDTO user) {
        return Mono.fromCallable(() -> {
            log.info("Create user: {}", user);
            UserEntity userEntity = userMapper.toUserEntity(user);
            AddressEntity addressEntity = userMapper.toAddressEntity(user);
            addressEntity.setUser(userEntity);
            userEntity.setAddress(List.of(addressEntity));
            return userEntity;
        }).subscribeOn(Schedulers.parallel());
    }

    @Transactional
    public Mono<User> save(UserEntity userEntity) {
        return Mono.fromCallable(() -> {
                    log.info("Saving user to database: {}", userEntity);
                    UserEntity savedUserEntity = userRepository.save(userEntity);
                    return userMapper.toUser(savedUserEntity);
                }).subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(user -> log.info("User saved successfully: {}", user))
                .doOnError(e -> log.error("Error saving user: {}", userEntity, e));
    }
}
