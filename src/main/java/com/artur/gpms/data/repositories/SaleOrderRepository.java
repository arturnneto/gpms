package com.artur.gpms.data.repositories;

import com.artur.gpms.data.entities.SaleOrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SaleOrderRepository extends MongoRepository<SaleOrderEntity, Long> {
}
