package com.paper.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.paper.TestUtils;
import com.paper.domain.*;
import com.paper.dto.ManufactureMachineDto;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.repositories.CatalogRepository;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import com.paper.repositories.ProducerRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Autowired
    private CatalogRepository catalogRepository;

    @BeforeAll
    public void before() throws IOException {
        byte[] image = Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"));
        Image saved = imageRepository.save(Image.builder()
                .id("64fd7839f1992248d41676c0")
                .type(MediaType.IMAGE_JPEG_VALUE)
                .base64Image(Base64.getEncoder().encodeToString(Base64.getMimeEncoder().encode(image)))
                .build());

        var savedCatalog = catalogRepository.save(Catalog.builder()
                .id(1L)
                .type(CatalogType.MANUFACTURE_MACHINE)
                .name("catalog name")
                .build());

        machineRepository.save(ManufactureMachine.builder()
                .description("test")
                .id(1L)
                .catalog(savedCatalog)
                .name("machine")
                .serialNumber("HX-3we45")
                .properties(new TreeMap<>(Map.of("pr1", "val1")))
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

        ManufactureMachine machine = ManufactureMachine.builder()
                .description("description1")
                .id(2L)
                .name("name1")
                .serialNumber("HX-345")
                .properties(new TreeMap<>(Map.of("k1", "v1", "k2", "v2")))
                .build();

        ManufactureMachineDto dto = ManufactureMachineDto.builder()
                .catalogId(1L)
                .manufactureMachine(machine)
                .producerId(1L)
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

                    Catalog catalog = catalogRepository.findById(1L).orElseThrow(CatalogNotFoundException::new);
                    assertThat(saved.getCatalog()).usingRecursiveComparison()
                            .isEqualTo(catalog);

                    assertThat(saved).usingRecursiveComparison()
                            .ignoringFields("catalog", "producer", "images")
                            .isEqualTo(machine);

                    assertEquals(1L, saved.getProducer().getId());
                    assertEquals(1, saved.getImages().size());
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
    public void addVideo() throws Exception {
        byte[] videoAsBytes = Files.readAllBytes(Path.of("src/test/resources/testVideo.mp4"));

        mockMvc.perform(multipart("/good/manufacture-machine/video/new")
                .file("video", videoAsBytes)
                .param("goodId", "1")
                .param("duration", "00:01:43")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isOk());
    }



    @Test
    @Order(4)
    public void update() throws Exception {
        byte[] newImage = Files.readAllBytes(Path.of("src/test/resources/testImage.png"));
        Image image1 = new Image(MediaType.IMAGE_PNG_VALUE, Base64.getEncoder().encodeToString(Base64.getMimeEncoder().encode(newImage)));

        ManufactureMachine machine = ManufactureMachine.builder()
                .description("new description")
                .id(1L)
                .name("new name")
                .serialNumber("new HX-345")
                .properties(new TreeMap(Map.of("new k1", "new v1", "new k2", "new v2")) )
                .build();

        ManufactureMachineDto dto = ManufactureMachineDto.builder()
                .catalogId(1L)
                .manufactureMachine(machine)
                .producerId(1L)
                .images
                        (new ArrayList<>(List.of(image1)))
                .build();

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(put("/good/manufacture-machine/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
            ).andExpect(status().isOk())
                .andDo(result -> {
                    ManufactureMachine saved = machineRepository.findById(1L)
                            .orElseThrow(EntityNotFoundException::new);

                    assertThat(saved).usingRecursiveComparison()
                            .ignoringFields("catalog", "producer", "images")
                            .isEqualTo(dto.getManufactureMachine());
                });
    }

    @Test
    @Order(5)
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

    @Test
    @Order(6)
    public void getAllSerialNumbers() throws Exception {
        mockMvc.perform(get("/good/manufacture-machine/serial_numbers/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    Map<String, Long> serialNumbers = new ObjectMapper().readValue(json, new TypeReference<>() {});
                    assertEquals(1, serialNumbers.size());
                });
    }

    @Test
    @Order(7)
    public void getAllCatalogs() throws Exception {
        mockMvc.perform(get("/good/manufacture-machine/catalog/all"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<Catalog> list = new ObjectMapper().readValue(json, new TypeReference<>() {});
                    assertThat(list).hasSize(1);
                    assertEquals(CatalogType.MANUFACTURE_MACHINE, list.get(0).getType());
                });
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
