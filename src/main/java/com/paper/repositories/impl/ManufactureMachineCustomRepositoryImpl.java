package com.paper.repositories.impl;

import com.paper.domain.Catalog;
import com.paper.domain.ManufactureMachine;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ManufactureMachineCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Log4j2
@Repository
@RequiredArgsConstructor
public class ManufactureMachineCustomRepositoryImpl implements ManufactureMachineCustomRepository {

    @PersistenceContext
    private EntityManager manager;

    private final CatalogRepository catalogRepository;

    private List<Long> processProducersIds(List<Long> producersIds) {
        if (producersIds == null) {
            producersIds = List.of(-1L, -2L);
        } else if (producersIds.isEmpty()) producersIds = List.of(-1L, -2L);
        return producersIds;
    }

    @Override
    @Transactional
    public List<ManufactureMachine> findPageAndFilterBy(String catalogName, Long priceFrom, Long priceTo, Pageable pageable) {

        Catalog catalog = catalogRepository.findByName(catalogName)
                .orElse(Catalog.builder().id(1000L).build());

        TypedQuery<ManufactureMachine> query = manager.createQuery("""
      
            from ManufactureMachine mm
              where
               (case
                    when :catalogId <> 1000 then mm.catalog.id = :catalogId
                    else true
                end)
              AND
                (case
                    when :priceFrom is not null and :priceTo is not null then (price >= :priceFrom  and price <= :priceTo)
                    else true
                end)
     
           """, ManufactureMachine.class);

        return query
                .setParameter("catalogId", catalog.getId())
                .setParameter("priceFrom", priceFrom)
                .setParameter("priceTo", priceTo)
                .getResultList();
    }
}
