package com.paper.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paper.TestUtils;
import com.paper.domain.Image;
import com.paper.domain.Producer;
import com.paper.dto.ProducerDto;
import com.paper.exceptions.ProducerNotFoundException;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ProducerRepository;
import com.paper.services.ProducerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TestUtils testUtils;

    @BeforeAll
    public void before() throws IOException {

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

    @Test
    @Order(1)
    public void create() throws Exception {
        Image image = Image.builder()
                .base64Image(Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"))))
                .type(MediaType.IMAGE_JPEG_VALUE)
                .build();

        Producer producer = Producer.builder()
                .name("producer")
                .description("texasdasadadadssadadasdt")
                .websiteUrl("https://mvnrepository.com/artifact/org.assertj/assertj-core/3.24.2")
                .build();

        ProducerDto producerDto = new ProducerDto(producer, image);

        mockMvc.perform(post("/producer/new")
                .content(new ObjectMapper().writeValueAsString(producerDto))
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(result -> {
            Long id = Long.parseLong(result.getResponse().getContentAsString());
            Producer saved = producerRepository.findById(id).orElseThrow(ProducerNotFoundException::new);
            assertThat(imageRepository.existsById(saved.getLogotypeId())).isTrue();

            assertThat(producer)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "logotypeId")
                    .isEqualTo(saved);

            producerService.deleteById(id);
        });
    }

    @Test
    @Order(2)
    public void getAll() throws Exception {

        mockMvc.perform(get("/producer/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    List<Producer> producers = new ObjectMapper().readValue(
                            result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertThat(producers).hasSize(1);
                });
    }

    @AfterAll
    public void after() {
        testUtils.deleteAllImages();
        producerRepository.deleteAll();
        testUtils.truncateProducerSequence();
    }
}
