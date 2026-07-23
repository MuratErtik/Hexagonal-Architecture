package org.murat.orderservicehexagonal.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.murat.orderservicehexagonal.adapter.out.persistence.entity.OutboxEventJpaEntity;
import org.murat.orderservicehexagonal.adapter.out.persistence.repository.OutboxEventJpaRepository;
import org.murat.orderservicehexagonal.application.port.out.OutboxEventPort;
import org.murat.orderservicehexagonal.domain.event.OutboxMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventAdapter implements OutboxEventPort {
    //able to make type conversion later for OutboxMessage

    private final OutboxEventJpaRepository outboxEventJpaRepository;

    @Override
    public void save(OutboxMessage message) {
        OutboxEventJpaEntity entity = OutboxEventJpaEntity.builder()
                .aggregateId(message.aggregateId())
                .aggregateType(message.aggregateType())
                .eventType(message.eventType())
                .targetSystem(message.targetSystem())
                .payload(message.payload())
                .createdAt(message.createdAt())
                .build();

        outboxEventJpaRepository.save(entity);
    }
}
