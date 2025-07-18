package com.artur.gpms.data.repositories;

import com.artur.gpms.data.entities.SaleOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SaleOrderRepository extends MongoRepository<SaleOrderEntity, Long> {
    Page<SaleOrderEntity> findAllByCustomerId(Long customerId, Pageable pageable);
    Optional<SaleOrderEntity> findByOrderId(String orderId);
    Page<SaleOrderEntity> findAll(Pageable pageable);
}
