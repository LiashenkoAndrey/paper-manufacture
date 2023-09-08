package com.paper.controllers;

import com.paper.domain.ManufactureMachine;
import com.paper.repositories.ManufactureMachineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TestUtils {

    private final ManufactureMachineRepository machineRepository;

    private final EntityManagerFactory entityManagerFactory;

    public void createDefaultManufactureMachine() {
        ManufactureMachine manufactureMachine = ManufactureMachine.builder()
                .description("test")
                .id(1L)
                .name("machine")
                .properties(Map.of("pr1", "val1"))
                .images(List.of("2342423423dfsdf", "sdf3r34r3f34r3"))
                .build();

        machineRepository.save(manufactureMachine);
    }

    public void truncateManufactureMachineAndGoodTypeTable() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("truncate table manufacture_machine cascade").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE manufacture_machine_id_seq RESTART WITH 1").executeUpdate();

        em.createNativeQuery("truncate table good_images cascade").executeUpdate();

        em.createNativeQuery("truncate table manufacture_machine_properties cascade").executeUpdate();
        em.createNativeQuery("truncate table good_group cascade").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE good_group_id_seq RESTART WITH 1").executeUpdate();
        transaction.commit();
    }
}
