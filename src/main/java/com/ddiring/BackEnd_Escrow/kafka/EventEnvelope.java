package com.ddiring.BackEnd_Escrow.kafka;

import lombok.*;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEnvelope<T> {
    private String eventId;
    private Instant timestamp;
    private T payload;
}