package com.paper.controllers;

import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TestUtils {

    private final ManufactureMachineRepository machineRepository;

    private final ImageRepository imageRepository;

    private final EntityManagerFactory entityManagerFactory;



    public void deleteAllImages() {
        imageRepository.deleteAll();
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

    public void truncateGoodImages() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("truncate table good_images cascade").executeUpdate();
        transaction.commit();
    }
}
