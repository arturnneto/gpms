package com.artur.gpms.data.repositories;

import com.artur.gpms.data.entities.SaleOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SaleOrderRepository extends MongoRepository<SaleOrderEntity, Long> {
    Page<SaleOrderEntity> findAllByCustomerId(Long customerId, PageRequest pageRequest);
}
