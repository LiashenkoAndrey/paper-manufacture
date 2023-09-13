package com.paper.services;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.ManufactureMachineDto;


public interface ManufactureMachineService {

    ManufactureMachine save(ManufactureMachine machine, ManufactureMachineDto dto);
}
