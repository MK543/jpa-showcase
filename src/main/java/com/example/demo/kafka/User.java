package com.example.demo.kafka;

import com.example.demo.entity.Transaction;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    private String name;

    private String password;

    private List<Address> address;

    private String uuid;

    @OneToMany
    private List<Transaction> orders;

}
