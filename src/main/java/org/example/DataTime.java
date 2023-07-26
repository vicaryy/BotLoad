package org.example;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class DataTime {

    public OffsetDateTime now() {
        return OffsetDateTime.now();
    }
}

