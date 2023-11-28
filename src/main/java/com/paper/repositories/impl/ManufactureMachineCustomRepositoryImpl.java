package com.paper.repositories.impl;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMDto2;
import com.paper.repositories.ManufactureMachineCustomRepository;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ManufactureMachineCustomRepositoryImpl implements ManufactureMachineCustomRepository {

    private static final Logger logger = LogManager.getLogger(ManufactureMachineCustomRepositoryImpl.class);

    @PersistenceContext
    private EntityManager manager;


    @Transactional
    @Override
    public Long getTotalItems(Long catalogId, List<Long> producersIds, Long priceFrom, Long priceTo) {

        producersIds = processProducersIds(producersIds);

        Stream<Tuple> totalCountStream = (Stream<Tuple>) manager.createNativeQuery("""
           select
            count(*)
            from manufacture_machine mm
               inner join good_images gi on mm.id = gi.manufacture_machine_id
               inner join catalog c on c.id = mm.catalog_id
               inner join producer p on p.id = mm.producer_id
            where mm.catalog_id = :catalogId
              and gi.good_images_order = 0
              and
                (case
                    when cast(:priceFrom as integer) is not null and cast(:priceTo as integer) is not null
                    then (price >= cast(:priceFrom as integer) and price <= cast(:priceTo as integer))
                    else true 
                end)
              and
                (case 
                    when -1 in (:producersIds) then true
                    else p.id in (:producersIds)
                end);
           """, Tuple.class)
                .setParameter("catalogId", catalogId)
                .setParameter("priceFrom", priceFrom)
                .setParameter("priceTo", priceTo)
                .setParameter("producersIds", producersIds.stream().map(Math::toIntExact).toList())
                .getResultStream();

        List<Long> totalCountList = totalCountStream.map(tuple -> tuple.get("count", Long.class)).toList();
        logger.info("totalCountList = " + totalCountList);
        return totalCountList.isEmpty() ? -1 : totalCountList.get(0);
    }

    private List<Long> processProducersIds(List<Long> producersIds) {
        if (producersIds == null) {
            producersIds = List.of(-1L, -2L);
        } else if (producersIds.isEmpty()) producersIds = List.of(-1L, -2L);
        return producersIds;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<ManufactureMachine> findPageAndFilterBy(Long catalogId, List<Long> producersIds, Long priceFrom, Long priceTo, Pageable pageable) {
    logger.info("produs " + producersIds);
        producersIds = processProducersIds(producersIds);

        if (producersIds == null) {
            producersIds = List.of(-1L, -2L);
        } else if (producersIds.isEmpty()) producersIds = List.of(-1L, -2L);

        logger.info("prod " +  producersIds.stream().map(Math::toIntExact).toList());


//        TypedQuery<Tuple> query = (TypedQuery<Tuple>) manager.createNativeQuery("""
//           select
//                mm.id,
//                mm.serial_number as serialNumber,
//                mm.name,
//                p.logotype_id as producerLogotypeId,
//                p.id as producerId,
//                mm.price,
//                image_id as imageId
//            from manufacture_machine mm
//               inner join good_images gi on mm.id = gi.manufacture_machine_id
//               inner join catalog c on c.id = mm.catalog_id
//               inner join producer p on p.id = mm.producer_id
//            where (case
//                             when cast(:catalogId as integer) != -1
//                                 then mm.catalog_id = cast(:catalogId as integer)
//                             else true
//                      end)
//              and gi.good_images_order = 0
//              and
//                (case
//                    when cast(:priceFrom as integer) is not null and cast(:priceTo as integer) is not null
//                    then (price >= cast(:priceFrom as integer) and price <= cast(:priceTo as integer))
//                    else true
//                end)
//              and
//                (case
//                    when -1 in (:producersIds) then true
//                    else p.id in (:producersIds)
//                end);
//           """, Tuple.class);

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
              AND
                (case
                    when -1 in (:producersIds) then true
                    else mm.producer.id in (:producersIds)
                end)
           """, ManufactureMachine.class);

        logger.info(catalogId);
        logger.info(priceFrom);
        logger.info(priceTo);
        logger.info(producersIds);

        List<ManufactureMachine> list = query
                .setParameter("catalogId", catalogId)
                .setParameter("priceFrom", priceFrom)
                .setParameter("priceTo", priceTo)
                .setParameter("producersIds", producersIds.stream().map(Math::toIntExact).toList())
                .getResultList();
        logger.info("res " + list);
        return list;

    }

}
