package com.paper.services;

import com.paper.controllers.TestUtils;
import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import com.paper.repositories.ImageRepository;
import com.paper.repositories.ManufactureMachineRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private ManufactureMachineRepository machineRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    public void save() throws IOException {
        ManufactureMachine manufactureMachine = ManufactureMachine.builder()
                .description("test")
                .id(1L)
                .name("machine")
                .properties(Map.of("pr1", "val1"))
                .images(List.of("2342423423dfsdf", "sdf3r34r3f34r3"))
                .build();

        byte[] image = Files.readAllBytes(Path.of("src/test/resources/testImage.jpg"));
        manufactureMachineService.save(manufactureMachine, List.of(new Image(MediaType.IMAGE_JPEG_VALUE, image)));
    }

    @AfterAll
    public void after() {
        imageRepository.deleteAll();
        machineRepository.deleteAll();
    }
}
