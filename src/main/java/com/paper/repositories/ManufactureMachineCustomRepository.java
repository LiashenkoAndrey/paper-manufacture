package com.paper.repositories;

import com.paper.dto.MMDto2;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ManufactureMachineCustomRepository {

    /**
     * Selects all serial numbers from all ManufactureMachine entities with their id
     * @return serial number and entity id as a java.util.Map<String, Long>
     */
    Map<String, Long> getAllSerialNumbers();

    List<MMDto2> findPageAndFilterBy(Long catalogId, List<Long> producersIds, Long priceFrom, Long priceTo, Pageable pageable);

     Long getTotalItems(Long catalogId, List<Long> producersIds, Long priceFrom, Long priceTo);
}
