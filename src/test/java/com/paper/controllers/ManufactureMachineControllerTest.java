package com.paper.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.paper.TestUtils;
import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.domain.Producer;
import com.paper.dto.ManufactureMachineDto;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.repositories.ProducerRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManufactureMachineControllerTest {

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ManufactureMachineRepository machineRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @BeforeAll
    public void before() throws IOException {
        byte[] image = Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"));
        Image saved = imageRepository.save(Image.builder()
                .id("64fd7839f1992248d41676c0")
                .type(MediaType.IMAGE_JPEG_VALUE)
                .base64Image(Base64.getEncoder().encodeToString(Base64.getMimeEncoder().encode(image)))
                .build());

        machineRepository.save(ManufactureMachine.builder()
                .description("test")
                .id(1L)
                .name("machine")
                .properties(Map.of("pr1", "val1"))
                .images(new ArrayList<>(List.of(saved.getId())))
                .build());

        producerRepository.save(Producer.builder()
                .logotypeId("64fd7839f1992248d41676c0")
                .name("test")
                .description("tn;lklkjlkj;lkj;lkj;lext")
                .websiteUrl("http://localhost/good/manufacture-machine/view/all")
                .id(1L)
                .build());

    }

    @Test
    @Order(2)
    public void create() throws Exception {
        byte[] image = Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"));
        Image image1 = new Image(MediaType.IMAGE_JPEG_VALUE, Base64.getEncoder().encodeToString(Base64.getMimeEncoder().encode(image)));

        ManufactureMachineDto dto = ManufactureMachineDto.builder()
                .description("description1")
                .id(2L)
                .producerId(1L)
                .name("name1")
                .properties(Map.of("k1", "v1", "k2", "v2"))
                .images(new ArrayList<>(List.of(image1)))
                .build();

        mockMvc.perform(multipart("/good/manufacture-machine/new")
                        .content(new ObjectMapper().writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> {
                    Long id = Long.parseLong(result.getResponse().getContentAsString());
                    ManufactureMachine saved = machineRepository.findById(id)
                            .orElseThrow(EntityNotFoundException::new);

                    assertEquals(1L, saved.getProducer().getId());
                    assertEquals(2, id);
                    assertEquals(1, saved.getImages().size());
                    assertEquals("name1", saved.getName());
                    assertEquals("description1", saved.getDescription());
                    assertTrue(saved.getProperties().containsKey("k1"));
                    assertTrue(saved.getProperties().containsKey("k2"));
                });

        mockMvc.perform(multipart("/good/manufacture-machine/new")
                        .content(new ObjectMapper().writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        byte[] image2 = Files.readAllBytes(Path.of("src/test/resources/testImage.png"));
        Image imagePng = new Image(MediaType.IMAGE_JPEG_VALUE, Base64.getEncoder().encodeToString(Base64.getMimeEncoder().encode(image2)));

        var dto2 = ManufactureMachineDto.builder()
                .images(List.of(imagePng))
                .build();

        mockMvc.perform(multipart("/good/manufacture-machine/new")
                        .content(new ObjectMapper().writeValueAsString(dto2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        dto.getImages().clear();

        mockMvc.perform(multipart("/good/manufacture-machine/new")
                .content(new ObjectMapper().writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void update() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", "name1");
        rootNode.put("description", "new description1");

        ObjectNode properties = mapper.createObjectNode();
        properties.put("v1", "new key1");
        properties.put("v2", "key2");
        rootNode.set("properties", properties);

        mockMvc.perform(put("/good/manufacture-machine/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rootNode.toString())
            ).andExpect(status().isOk())
                .andDo(result -> {
                    ManufactureMachine saved = machineRepository.findById(1L)
                            .orElseThrow(EntityNotFoundException::new);

                    assertEquals("name1", saved.getName());
                    assertEquals("new description1", saved.getDescription());
                    assertEquals(saved.getProperties().get("v1"), "new key1");
                    assertEquals(saved.getProperties().get("v2"), "key2");
                });

        mockMvc.perform(put("/good/manufacture-machine/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rootNode.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(4)
    public void deleteGood() throws Exception {

        assertTrue(machineRepository.existsById(1L));
        mockMvc.perform(delete("/good/manufacture-machine/1/delete"))
                .andExpect(status().isOk());
        assertFalse(machineRepository.existsById(1L));
        assertFalse(imageRepository.existsById("64fd7839f1992248d41676c0"));

        mockMvc.perform(delete("/good/manufacture-machine/100/delete"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/good/manufacture-machine/sadd/delete"))
                .andExpect(status().isBadRequest());
    }

    @AfterAll
    public void after() {
        testUtils.truncateManufactureMachineAndGoodTypeTable();
        testUtils.truncateGoodImages();
        testUtils.deleteAllImages();

        producerRepository.deleteAll();
        testUtils.truncateProducerSequence();
    }
}
