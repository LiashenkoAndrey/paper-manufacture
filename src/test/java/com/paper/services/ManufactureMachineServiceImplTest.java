package com.paper.services;

import com.paper.TestUtils;
import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.ManufactureMachineDto;
import com.paper.repositories.ProducerRepository;
import com.paper.services.impl.ManufactureMachineServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManufactureMachineServiceImplTest {

    @Autowired
    private ManufactureMachineServiceImpl manufactureMachineService;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private ProducerRepository producerRepository;


    @BeforeAll
    public void before() throws IOException {
        testUtils.createTestProducerWithId1();
    }

    @Test
    public void save() throws IOException {
        ManufactureMachine manufactureMachine = ManufactureMachine.builder()
                .description("test")
                .id(1L)
                .name("machine")
                .serialNumber("HX-150")
                .properties(Map.of("pr1", "val1"))
                .images(List.of("2342423423dfsdf", "sdf3r34r3f34r3"))
                .build();

        byte[] image = Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"));
        manufactureMachineService.save(
                manufactureMachine,
                ManufactureMachineDto.builder()
                        .images(List.of(new Image(MediaType.IMAGE_JPEG_VALUE, Base64.getEncoder().encodeToString(image))))
                        .producerId(1L)
                        .build());
    }

    @AfterAll
    public void after() {
        testUtils.truncateManufactureMachineAndGoodTypeTable();
        producerRepository.deleteAll();
        testUtils.truncateProducerSequence();
    }
}
