package com.paper.repositories;


import com.paper.TestUtils;
import com.paper.domain.ManufactureMachine;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManufactureMachineRepositoryTest {

    @Autowired
    private ManufactureMachineRepository repository;

    @Autowired
    private TestUtils testUtils;

    @Test
    @Order(1)
    public void create() {
        ManufactureMachine manufactureMachine = ManufactureMachine.builder()
                .description("test")
                .name("machine")
                .serialNumber("HX-150")
                .properties(Map.of("pr1", "val1"))
                .images(List.of("2342423423dfsdf", "sdf3r34r3f34r3"))
                .build();


        ManufactureMachine saved = repository.save(manufactureMachine);
        assertTrue(repository.existsById(saved.getId()));
    }

    @Test
    @Order(2)
    public void read() {
        var manufactureMachine = repository.findById(1L);
        assertTrue(manufactureMachine.isPresent());
    }


    @Test
    @Order(3)
    public void update() {
        var manufactureMachine = repository.findById(1L).orElseThrow(EntityNotFoundException::new);
        manufactureMachine.setDescription("new description");

        manufactureMachine.getProperties().put("new key", "new val");

        var saved = repository.save(manufactureMachine);

        assertEquals(manufactureMachine.getDescription(), saved.getDescription());
        assertTrue(manufactureMachine.getProperties().containsKey("new key"));
    }

    @Test
    @Order(4)
    public void delete() {
        ManufactureMachine manufactureMachine = ManufactureMachine.builder()
                .description("test d")
                .name("delete me")
                .serialNumber("HX-150")
                .properties(Map.of("pr1443", "val443"))
                .images(List.of("23dfdfdf", "dfdfdfdff"))
                .build();

        ManufactureMachine saved = repository.save(manufactureMachine);
        assertTrue(repository.existsById(saved.getId()));
        repository.deleteById(saved.getId());

        assertFalse(repository.existsById(saved.getId()));
    }


    @AfterAll
    public void truncateManufactureMachineTable() {
        testUtils.truncateManufactureMachineAndGoodTypeTable();
        testUtils.truncateGoodImages();
    }
}
