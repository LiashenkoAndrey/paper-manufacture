package com.paper.repositories;

import com.paper.domain.ManufactureMachine;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManufactureMachineCustomRepository {


    List<ManufactureMachine> findPageAndFilterBy(String catalogName, List<Long> producersIds, Long priceFrom, Long priceTo, Pageable pageable);

     Long getTotalItems(Long catalogId, List<Long> producersIds, Long priceFrom, Long priceTo);
}
