package com.paper.services;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.GoodDto;


public interface ManufactureMachineService {

    ManufactureMachine save(GoodDto dto);

    void addProperty(Long goodId, String name, String value);

    void deleteProperty(Long goodId, String name);
}
