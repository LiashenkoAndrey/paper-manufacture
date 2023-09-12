package com.paper.repositories;

import com.paper.TestUtils;
import com.paper.domain.ManufactureMachine;
import com.paper.repositories.impl.ManufactureMachineSearchRepositoryImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManufactureMachineSearchRepositoryImplTest {

    @Autowired
    private ManufactureMachineRepository repository;

    @Autowired
    private ManufactureMachineSearchRepositoryImpl searchRepository;

    @Autowired
    private TestUtils testUtils;

    @BeforeAll
    public void before() {
        repository.save(ManufactureMachine.builder()
                .description("test")
                .name("machine")
                .serialNumber("HX-150")
                .properties(Map.of("pr1", "val1"))
                .images(List.of("2342423423dfsdf", "sdf3r34r3f34r3"))
                .build());

    }

    @Test
    @Order(1)
    public void getAllSerialNumbers() {
        Map<String, Long> map = searchRepository.getAllSerialNumbers();
        assertThat(map).hasSize(1);
        Long id = map.get("HX-150");
        assertThat(id).isNotNull();
    }

    @AfterAll
    public void after() {
        testUtils.truncateManufactureMachineAndGoodTypeTable();
        testUtils.truncateGoodImages();
        testUtils.deleteAllImages();
    }
}
