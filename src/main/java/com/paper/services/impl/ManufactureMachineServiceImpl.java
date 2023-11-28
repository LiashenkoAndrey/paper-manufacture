package com.paper.services.impl;

import com.paper.domain.Catalog;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.Producer;
import com.paper.dto.ManufactureMachineDto;
import com.paper.exceptions.ServiceException;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.services.ManufactureMachineService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ManufactureMachineServiceImpl implements ManufactureMachineService {

    private final ManufactureMachineRepository machineRepository;

    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = LogManager.getLogger(ManufactureMachineServiceImpl.class);

    @Override
    public ManufactureMachine save(ManufactureMachineDto dto) {
        logger.info("new image: " + dto);

        Catalog catalog = em.getReference(Catalog.class, dto.getCatalogId());
        Producer producer = em.getReference(Producer.class, dto.getProducerId());

        return machineRepository.save(ManufactureMachine.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .serialNumber(dto.getSerialNumber())
                .images(dto.getImages())
                .catalog(catalog)
                .producer(producer)
                .build());
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
