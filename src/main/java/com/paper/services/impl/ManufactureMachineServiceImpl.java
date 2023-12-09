package com.paper.services.impl;

import com.paper.domain.Catalog;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.Producer;
import com.paper.dto.ManufactureMachineDto;
import com.paper.exceptions.EntityAlreadyExistException;
import com.paper.exceptions.ServiceException;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import static com.paper.util.EntityMapper.map;

@Service
@RequiredArgsConstructor
public class ManufactureMachineServiceImpl implements ManufactureMachineService {

    private final ManufactureMachineRepository machineRepository;

    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = LogManager.getLogger(ManufactureMachineServiceImpl.class);

    @Override
    public ManufactureMachine save(ManufactureMachineDto dto) {
        logger.info("new ManufactureMachine: " + dto);

        Catalog catalog = em.getReference(Catalog.class, dto.getCatalogId());
        Producer producer = em.getReference(Producer.class, dto.getProducerId());

        ManufactureMachine machine = new ManufactureMachine(producer, catalog);
        map(dto, machine).map();

        if (machineRepository.exists(Example.of(machine))) {
            if (machine.getId() == null) {
                logger.error("ALREADY exist, " + machine);
                throw new EntityAlreadyExistException();
            }
            logger.info("Exits, " + machine);
        } else {
            logger.info("NOT EXIST, " + machine);
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
            logger.error(e);
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
            logger.error(e);
            throw new ServiceException(e);
        }
    }

}
