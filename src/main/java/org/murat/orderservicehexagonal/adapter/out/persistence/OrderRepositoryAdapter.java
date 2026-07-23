package org.murat.orderservicehexagonal.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.murat.orderservicehexagonal.adapter.out.persistence.entity.OrderJpaEntity;
import org.murat.orderservicehexagonal.adapter.out.persistence.mapper.OrderPersistenceMapper;
import org.murat.orderservicehexagonal.adapter.out.persistence.repository.OrderJpaRepository;
import org.murat.orderservicehexagonal.application.port.out.OrderRepositoryPort;
import org.murat.orderservicehexagonal.domain.enums.OrderStatus;
import org.murat.orderservicehexagonal.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = mapper.toEntity(order);
        OrderJpaEntity saved = orderJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public boolean findByIdAndStatus(UUID id, OrderStatus status) {
        return orderJpaRepository.existsByUserIdAndStatus(id,status);
    }

    @Override
    public Optional<Order> findByIdWithLock(UUID id) {
        return orderJpaRepository.findByIdWithLock(id)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndStatus(UUID userId, OrderStatus status) {
        return orderJpaRepository.existsByUserIdAndStatus(userId, status);
    }

    @Override
    public Page<Order> findAllByUserId(UUID userId, Pageable pageable) {
        return orderJpaRepository.findAllByUserId(userId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Order> findOrdersBySellerIdInItems(UUID sellerId, Pageable pageable) {
        return orderJpaRepository.findOrdersBySellerIdInItems(sellerId, pageable)
                .map(mapper::toDomain);
    }
}
