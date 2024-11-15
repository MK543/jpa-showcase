package com.example.demo.kafka;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {

    private String street;

    private String city;

    private String state;

}
