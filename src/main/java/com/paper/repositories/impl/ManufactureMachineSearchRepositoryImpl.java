package com.paper.repositories.impl;

import com.paper.repositories.ManufactureMachineSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ManufactureMachineSearchRepositoryImpl implements ManufactureMachineSearchRepository {


    @PersistenceContext
    private EntityManager manager;

    /**
     * Selects all serial numbers from all ManufactureMachine entities with their id
     * @return serial number and entity id as a java.util.Map<String, Long>
     */
    @Override
    public Map<String, Long> getAllSerialNumbers() {
        Map<String, Long> map = manager.createQuery("""
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
           return map;
    }
}
