package com.paper.services;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMDto;
import com.paper.dto.ManufactureMachineDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ManufactureMachineService {

    ManufactureMachine save(ManufactureMachineDto dto);

    void update(ManufactureMachine saved, ManufactureMachineDto dto);


    /**
     * Deletes image from mongoDb and their record in relational database  (if exist)
     * @param imageId image id
     */
    void deleteImageById(String imageId);

    Page<MMDto> findAllWithFilters(Long catalogId, Long from, Long to, List<Long> producerId, Pageable pageable);
}
