package com.artur.gpms.data.repositories;

import com.artur.gpms.data.entities.SaleOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleOrderRepository extends MongoRepository<SaleOrderEntity, Long> {
    Optional<SaleOrderEntity> findByOrderId(String orderId);
    Page<SaleOrderEntity> findAll(Pageable pageable);
    List<SaleOrderEntity> findByCustomerId(Long customerId);
}
