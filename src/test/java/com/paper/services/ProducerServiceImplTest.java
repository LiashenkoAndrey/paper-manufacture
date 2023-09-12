package com.paper.services;

import com.paper.TestUtils;
import com.paper.domain.Image;
import com.paper.domain.Producer;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ProducerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProducerServiceImplTest {

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @BeforeAll
    public void before() throws IOException {
        Image image = imageRepository.save(Image.builder()
                .base64Image(Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"))))
                .type(MediaType.IMAGE_JPEG_VALUE)
                .id("64fb597d49fa243ee76dc7cb")
                .build());

        Producer producer = Producer.builder()
                .id(1L)
                .name("testsdfsdf")
                .description("it is a test sdfsdfdescription")
                .logotypeId(image.getId())
                .websiteUrl("https://mvnrepositsdfsdfory.com/artifact/org.assertj/assertj-core/3.24.2")
                .build();

        producerRepository.save(producer);
}

    @Test
    @Order(1)
    public void save() throws IOException {
        Image image = imageRepository.save(Image.builder()
                .base64Image(Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"))))
                .type(MediaType.IMAGE_JPEG_VALUE)
                .id("64fb5c6d490a243ee76dc6ab")
                .build());

        Producer producer = Producer.builder()
                .id(2L)
                .name("test dfgdfgdf")
                .description("it is a test description 4234234")
                .logotypeId(image.getId())
                .websiteUrl("https://mvnrepository.com")
                .build();

        producerRepository.save(producer);
        assertTrue(producerRepository.existsById(2L));
    }


    @Test
    @Order(2)
    public void deleteById() {
        assertTrue(producerRepository.existsById(1L));
        producerService.deleteById(1L);
        assertFalse(producerRepository.existsById(1L));
    }

    @AfterAll
    public void after() {
        testUtils.deleteAllImages();
        producerRepository.deleteAll();
        testUtils.truncateProducerSequence();
    }
}
