package com.example.demo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Jacksonized
@Builder
@ToString
public class SampleObject {
    private String name;
    private int age;
    private List<String> tags;
    private boolean active;
    private OffsetDateTime offsetDateTime;
    private List<SampleObject2> objects;
}
