package com.paper.services;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.ManufactureMachineDto;


public interface ManufactureMachineService {

    ManufactureMachine save(ManufactureMachineDto dto);

//    void update(ManufactureMachine saved, ManufactureMachineDto dto);


    /**
     * Deletes image from mongoDb and their record in relational database  (if exist)
     * @param imageId image id
     */
    void deleteImageById(String imageId);

    void addProperty(Long goodId, String name, String value);

    void deleteProperty(Long goodId, String name);
}
