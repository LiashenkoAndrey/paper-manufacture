package com.paper.services.impl;

import com.paper.domain.Catalog;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.GoodDto;
import com.paper.exceptions.EntityAlreadyExistException;
import com.paper.exceptions.ServiceException;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ManufactureMachineServiceImpl implements ManufactureMachineService {

    private final ManufactureMachineRepository machineRepository;

    @PersistenceContext
    private EntityManager em;


    @Override
    public ManufactureMachine save(@Valid GoodDto dto) {
        log.info("new ManufactureMachine: " + dto);

        Catalog catalog = em.getReference(Catalog.class, dto.getCatalogId());

        ManufactureMachine machine = ManufactureMachine.builder()
                .images(dto.getImages())
                .name(dto.getName())
                .oldPrice(dto.getOldPrice())
                .catalog(catalog)
                .externalImages(dto.getExternalImagesUrls())
                .videoUrl(dto.getVideoUrl())
                .price(dto.getPrice())
                .url(dto.getUrl())
                .build();

        log.info("after save = {}", machine);
        if (machineRepository.exists(Example.of(machine))) {
            if (machine.getId() == null) {
                log.error("ALREADY exist, " + machine);
                throw new EntityAlreadyExistException();
            }
            log.info("Exits, " + machine);
        } else {
            log.info("NOT EXIST, " + machine);
        }

        return machineRepository.save(machine);
    }
}
