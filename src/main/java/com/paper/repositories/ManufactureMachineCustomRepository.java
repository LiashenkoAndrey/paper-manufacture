package com.paper.repositories;


import java.util.Map;

public interface ManufactureMachineCustomRepository {

    /**
     * Selects all serial numbers from all ManufactureMachine entities with their id
     * @return serial number and entity id as a java.util.Map<String, Long>
     */
    Map<String, Long> getAllSerialNumbers();
}
