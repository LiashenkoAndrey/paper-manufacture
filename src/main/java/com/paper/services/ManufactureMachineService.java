package com.paper.services;

import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;

import java.util.List;

public interface ManufactureMachineService {

    ManufactureMachine save(ManufactureMachine machine, List<Image> images, Long producerId);
}
