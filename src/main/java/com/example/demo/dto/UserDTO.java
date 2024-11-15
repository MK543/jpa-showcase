package com.example.demo.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    private String name;

    private String password;

    private String street;

    private String city;

    private String state;

}

