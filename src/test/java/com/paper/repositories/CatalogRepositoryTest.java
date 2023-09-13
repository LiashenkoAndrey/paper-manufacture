package com.paper.repositories;

import com.paper.TestUtils;
import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
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
public class CatalogRepositoryTest {

    @Autowired
    private ManufactureMachineRepository machineRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private TestUtils testUtils;

    @BeforeAll
    public void before() {
        Catalog saved = catalogRepository.save(Catalog.builder()
                .id(1L)
                .type(CatalogType.MANUFACTURE_MACHINE)
                .name("Test 1 catalog")
                .build());

        ManufactureMachine manufactureMachine = ManufactureMachine.builder()
                .description("test")
                .id(1L)
                .catalog(saved)
                .serialNumber("hX-150")
                .name("machine")
                .properties(Map.of("pr1", "val1"))
                .images(List.of("2342423423dfsdf", "sdf3r34r3f34r3"))
                .build();

        machineRepository.save(manufactureMachine);
    }

    @Test
    @Order(1)
    public void create() {
        catalogRepository.save(Catalog.builder()
                .id(2L)
                .type(CatalogType.MANUFACTURE_MACHINE)
                .name("Test 2 catalog")
                .build());

        assertTrue(catalogRepository.existsById(1L));
    }

    @Test
    @Order(2)
    public void update() {
        var machineGroup = catalogRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        machineGroup.setName("another name");

        Catalog saved = catalogRepository.save(machineGroup);

        assertNotEquals("Test 1 catalog", saved.getName());
    }

    @Test
    @Order(3)
    public void delete() {
        assertTrue(catalogRepository.existsById(2L));
        catalogRepository.deleteById(2L);
        assertFalse(catalogRepository.existsById(2L));
    }


    @AfterAll
    public void truncateManufactureMachineTable() {
        testUtils.truncateManufactureMachineAndGoodTypeTable();
        testUtils.truncateGoodImages();
    }
}
