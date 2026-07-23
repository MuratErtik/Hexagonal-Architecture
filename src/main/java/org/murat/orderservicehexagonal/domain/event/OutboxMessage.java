package org.murat.orderservicehexagonal.domain.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OutboxMessage(
        String aggregateId,
        String aggregateType,
        String eventType,
        String targetSystem,
        String payload,
        LocalDateTime createdAt
) {}
