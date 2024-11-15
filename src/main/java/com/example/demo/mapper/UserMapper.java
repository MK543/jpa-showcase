package com.example.demo.mapper;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.AddressEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.kafka.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "uuid", source = "userDTO", qualifiedByName = "generateUUID")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    UserEntity toUserEntity(UserDTO userDTO);

    User toUser(UserEntity userEntity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    AddressEntity toAddressEntity(UserDTO userDTO);

    @Named("generateUUID")
    default String generateUUID(UserDTO userDTO) {
        return UUID.randomUUID().toString();
    }
}
