package com.paper;

import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.Producer;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.repositories.ProducerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;


@Component
@RequiredArgsConstructor
public class TestUtils {

    public final ManufactureMachineRepository machineRepository;

    public final ImageRepository imageRepository;

    public final EntityManagerFactory entityManagerFactory;

    public final ProducerRepository producerRepository;

    public void deleteAllImages() {
        imageRepository.deleteAll();
    }


    public void truncateManufactureMachineAndGoodTypeTable() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("truncate table manufacture_machine cascade").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE manufacture_machine_id_seq RESTART WITH 1").executeUpdate();
        em.createNativeQuery("truncate table manufacture_machine_properties cascade").executeUpdate();
        em.createNativeQuery("truncate table good_group cascade").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE good_group_id_seq RESTART WITH 1").executeUpdate();
        transaction.commit();
    }

    public void createTestProducerWithId1() throws IOException {
        Image image = imageRepository.save(Image.builder()
                .base64Image(Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"))))
                .type(MediaType.IMAGE_JPEG_VALUE)
                .id("64fb5c6d490a243ee76dc7cb")
                .build());

        Producer producer = Producer.builder()
                .id(1L)
                .name("test")
                .description("it is a test description")
                .logotypeId(image.getId())
                .websiteUrl("https://mvnrepository.com/artifact/org.assertj/assertj-core/3.24.2")
                .build();

        producerRepository.save(producer);
    }

    public void truncateProducerSequence() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("ALTER SEQUENCE producer_id_seq RESTART WITH 1").executeUpdate();
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
