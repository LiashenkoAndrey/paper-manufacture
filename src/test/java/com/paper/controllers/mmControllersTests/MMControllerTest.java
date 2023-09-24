package com.paper.controllers.mmControllersTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paper.TestUtils;
import com.paper.domain.*;
import com.paper.dto.ManufactureMachineDto;
import com.paper.exceptions.CatalogNotFoundException;
import com.paper.repositories.*;
import com.paper.services.ManufactureMachineService;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class MMControllerTest {

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
    @Transactional
    public void before() {
        Catalog saved = catalogRepository.save(Catalog.builder()
                .id(1L)
                .type(CatalogType.MANUFACTURE_MACHINE)
                .name("Test 1 catalog")
                .build());

        for (int i = 0; i < 10; i++) {
            ManufactureMachine manufactureMachine = ManufactureMachine.builder()
                    .description("test" + i)
                    .serialNumber("hX-150" + i)
                    .catalog(saved)
                    .name("machine" + i)
                    .properties(new TreeMap<>(Map.of("pr1", "val1")))
                    .images(List.of("2342423423dfsdf", "sdf3r34r3f34r3"))
                    .build();

            machineRepository.save(manufactureMachine);
        }

    }

    @Nested
    @Component
    class CrudTests {

        @Autowired
        private CatalogRepository catalogRepository;

        @Test
        @Order(1)
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
                                .ignoringFields("catalog", "producer", "images", "videos")
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
        @Order(2)
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
                                .ignoringFields("catalog", "producer", "images", "videos")
                                .isEqualTo(dto.getManufactureMachine());
                    });
        }

        @Test
        @Order(3)
        public void getPageOfGoods() throws Exception {
            mockMvc.perform(get("/good/manufacture-machine/page")
                    .param("catalogId", "1")
                    .param("pageId", "1")
                    .param("pageSize", "2"))
                    .andExpect(status().isOk())
                    .andDo(result -> {
                        String json = result.getResponse().getContentAsString();
                        System.out.println(machineRepository.findAll());
                        List<ManufactureMachine> machines = new ObjectMapper().readValue(json, new TypeReference<>() {});
                        assertEquals(2, machines.size());
                    });
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

    }

    @Nested
    @Component
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ImageTest {

        private final String newImageId = "64fd7839f1992248d31676a0";

        @PersistenceContext
        private EntityManager entityManager;

        @Autowired
        private ManufactureMachineService machineService;

        @BeforeAll
        public void before() throws IOException {
            byte[] image = Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"));
            Image saved = imageRepository.save(Image.builder()
                    .id(newImageId)
                    .type(MediaType.IMAGE_JPEG_VALUE)
                    .base64Image(Base64.getEncoder().encodeToString(Base64.getMimeEncoder().encode(image)))
                    .build());

            machineRepository.save(ManufactureMachine.builder()
                    .id(2L)
                    .description("test image")
                    .name("machine saved")
                    .images(List.of(saved.getId()))
                    .serialNumber("HX-389we45")
                    .properties(new TreeMap<>(Map.of("pr1", "val1")))
                    .build());
        }

        @Test
        @Order(1)
        public void addImage() throws Exception {
            byte[] image = Files.readAllBytes(Path.of("src/test/resources/testImage.png"));

            MockMultipartFile file = new MockMultipartFile("image", "hello.png", MediaType.IMAGE_PNG_VALUE, image);

            mockMvc.perform(multipart("/good/manufacture-machine/image/new")
                            .file(file)
                            .contentType(MediaType.IMAGE_PNG)
                    .param("goodId", "2")
            )
                    .andExpect(status().isOk())
                    .andDo(result -> machineService.deleteImageById(result.getResponse().getContentAsString()));
        }

        @Test
        @Order(2)
        public void deleteImage() throws Exception {
            assertImagesSizeIsEquals(1);
            assertThat(imageRepository.existsById(newImageId)).isTrue();

            mockMvc.perform(delete("/good/manufacture-machine/image/"+ newImageId +"/delete"))
                    .andExpect(status().isOk());

            assertThat(imageRepository.existsById(newImageId)).isFalse();
            assertImagesSizeIsEquals(0);

            mockMvc.perform(delete("/good/manufacture-machine/image/"+ newImageId +"/delete"))
                    .andExpect(status().isBadRequest());
        }

        private void assertImagesSizeIsEquals(int size) {
            assertThat(
                    entityManager.createNativeQuery("select image_id from good_images where manufacture_machine_id = 2").getResultList()
            ).hasSize(size);
        }

    }

    @Nested
    @Component
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class VideoTest {

        @Autowired
        private  VideoRepository videoRepository;

        @Test
        @Order(1)
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
        @Order(2)
        public void deleteVideoById() throws Exception {
            assertTrue(videoRepository.existsById(1L));

            mockMvc.perform(delete("/good/manufacture-machine/video/1/delete"))
                    .andExpect(status().isOk());

            mockMvc.perform(delete("/good/manufacture-machine/video/1/delete"))
                    .andExpect(status().isBadRequest());

            assertFalse(videoRepository.existsById(1L));
        }
        @AfterAll
        public void deleteAllVideos() {
            videoRepository.deleteAll();
            testUtils.truncateVideoSequence();
        }
    }

    @Nested
    @Component
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CatalogTest {
        @Test
        @Order(1)
        public void getAllCatalogs() throws Exception {
            mockMvc.perform(get("/good/manufacture-machine/catalog/all"))
                    .andExpect(status().isOk())
                    .andDo(result -> {
                        String json = result.getResponse().getContentAsString();
                        List<Catalog> list = new ObjectMapper().readValue(json, new TypeReference<>() {
                        });
                        assertThat(list).hasSize(1);
                        assertEquals(CatalogType.MANUFACTURE_MACHINE, list.get(0).getType());
                    });
        }
    }


    @Test
    @Order(5)
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



    @AfterAll
    public void after() {
        testUtils.truncateManufactureMachineAndGoodTypeTable();
        testUtils.truncateGoodImages();
        testUtils.deleteAllImages();

        producerRepository.deleteAll();
        testUtils.truncateProducerSequence();
    }
}
