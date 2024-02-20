package com.paper.services.impl;

import com.paper.domain.Catalog;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.ManufactureMachineDto;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import static com.paper.util.EntityMapper.map;

@Service
@Log4j2
@RequiredArgsConstructor
public class ManufactureMachineServiceImpl implements ManufactureMachineService {

    private final ManufactureMachineRepository machineRepository;

    @PersistenceContext
    private EntityManager em;


    @Override
    public ManufactureMachine save(@Valid ManufactureMachineDto dto) {
        log.info("new ManufactureMachine: " + dto);

        Catalog catalog = em.getReference(Catalog.class, dto.getCatalogId());

        ManufactureMachine machine = ManufactureMachine.builder()
                .images(dto.getImages())
                .description(dto.getDescription())
                .name(dto.getName())
                .catalog(catalog)
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


    @Override
    @Transactional
    public void addProperty(Long goodId, String name, String value) {
        try {
            em.createNativeQuery("insert into manufacture_machine_properties(property_name, property_value, manufacture_machine_id) " +
                            "VALUES (:name, :value, :goodId)")
                    .setParameter("name", name)
                    .setParameter("value", value)
                    .setParameter("goodId", goodId)
                    .executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public void deleteProperty(Long goodId, String name) {
        try {
            em.createNativeQuery("delete from manufacture_machine_properties where manufacture_machine_id = :goodId and property_name = :name")
                    .setParameter("goodId", goodId)
                    .setParameter("name", name)
                    .executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(e);
        }
    }

}
