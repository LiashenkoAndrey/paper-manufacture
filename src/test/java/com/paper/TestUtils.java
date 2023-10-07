package com.paper;

import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.Producer;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.repositories.ProducerRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;


@Component
public class TestUtils {

    @Autowired
    public ManufactureMachineRepository machineRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    public ProducerRepository producerRepository;

    @PersistenceContext
    private EntityManager em;


    public void deleteAllImages() {
        imageRepository.deleteAll();
    }


    @Transactional
    public void truncateManufactureMachineAndGoodTypeTable() {
        em.createNativeQuery("truncate table manufacture_machine cascade").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE manufacture_machine_id_seq RESTART WITH 1").executeUpdate();
        em.createNativeQuery("truncate table manufacture_machine_properties cascade").executeUpdate();
        em.createNativeQuery("truncate table catalog cascade").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE good_group_id_seq RESTART WITH 1").executeUpdate();
    }

    public Producer createTestProducerWithId1() throws IOException {
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

        return producerRepository.save(producer);
    }

    @Transactional
    public void truncateProducerSequence() {
        em.createNativeQuery("ALTER SEQUENCE producer_id_seq RESTART WITH 1").executeUpdate();
    }

    @Transactional
    public void truncateVideoSequence() {
        em.createNativeQuery("ALTER SEQUENCE videos_id_seq RESTART WITH 1").executeUpdate();
    }


    @Transactional
    public void truncateGoodImages() {
        em.createNativeQuery("truncate table good_images cascade").executeUpdate();
    }
}
