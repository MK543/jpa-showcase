package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "transaction")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {

    @Id
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity recipient;

    Double amount;

    @Version
    Long version;
}
