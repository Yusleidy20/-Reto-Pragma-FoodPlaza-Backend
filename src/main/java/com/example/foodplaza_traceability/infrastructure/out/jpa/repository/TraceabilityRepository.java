package com.example.foodplaza_traceability.infrastructure.out.jpa.repository;

import com.example.foodplaza_traceability.infrastructure.out.jpa.entity.TraceabilityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraceabilityRepository extends MongoRepository<TraceabilityEntity, String> {
    List<TraceabilityEntity> findByIdOrderAndCustomerId(Long idOrder, Long customerId);
    List<TraceabilityEntity> findByIdOrderIn(List<Long> orderIds);

}
