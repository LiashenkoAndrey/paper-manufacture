package com.paper.services;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMDto2;
import com.paper.dto.ManufactureMachineDto;

import java.util.List;


public interface ManufactureMachineService {

    ManufactureMachine save(ManufactureMachineDto dto);

    void update(ManufactureMachine saved, ManufactureMachineDto dto);


    /**
     * Deletes image from mongoDb and their record in relational database  (if exist)
     * @param imageId image id
     */
    void deleteImageById(String imageId);


    List<MMDto2> translateAllNamesDto(List<MMDto2> machines);

    List<ManufactureMachine> translateAll(List<ManufactureMachine> machines);
    ManufactureMachine translate(ManufactureMachine machine);
}
