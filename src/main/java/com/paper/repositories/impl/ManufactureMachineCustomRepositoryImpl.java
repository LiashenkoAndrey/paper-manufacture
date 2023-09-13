package com.paper.repositories.impl;

import com.paper.repositories.ManufactureMachineCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ManufactureMachineCustomRepositoryImpl implements ManufactureMachineCustomRepository {


    @PersistenceContext
    private EntityManager manager;

    /**
     * Selects all serial numbers from all ManufactureMachine entities with their id
     * @return serial number and entity id as a java.util.Map<String, Long>
     */
    @Override
    @Transactional
    public Map<String, Long> getAllSerialNumbers() {
        return manager.createQuery("""
           select 
                m.serialNumber as serialNumber,
                m.id as entityId
           from ManufactureMachine m
           """, Tuple.class)
                .getResultStream()
                .collect(Collectors.toMap(
                        tuple -> (String) tuple.get("serialNumber"),
                        tuple -> ((Number) tuple.get("entityId")).longValue()
                )
           );
    }

}
