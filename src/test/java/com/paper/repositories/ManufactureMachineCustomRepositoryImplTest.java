package com.paper.repositories;

import com.paper.TestUtils;
import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMDto2;
import com.paper.repositories.impl.ManufactureMachineCustomRepositoryImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManufactureMachineCustomRepositoryImplTest {

    @Autowired
    private ManufactureMachineRepository repository;

    @Autowired
    private ManufactureMachineCustomRepositoryImpl searchRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private TestUtils testUtils;

    @BeforeAll
    public void before() throws IOException {
        Catalog saved = catalogRepository.save(Catalog.builder()
                .id(2L)
                .type(CatalogType.MANUFACTURE_MACHINE)
                .name("Test 1 catalog")
                .build());

        System.out.println(catalogRepository.findAll());

        repository.save(ManufactureMachine.builder()
                .id(1L)
                .description("test")
                .name("machine")
                .price(100L)
                .catalog(saved)
                .producer(testUtils.createTestProducerWithId1())
                .serialNumber("HX-150")
                .properties(new TreeMap<>(Map.of("pr1", "val1")))
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

    @Order(2)
    @Test
    public void findAllByCatalogIdAndByPrice() {
        Page<MMDto2> page = searchRepository.findPageAndFilterBy(1L, List.of(1L),  null, null, PageRequest.of(0, 5));
        assertThat(page.stream().toList()).hasSize(1);
        MMDto2 item = page.toList().get(0);
        assertThat(item).isNotNull();
        assertThat(item.getPrice())
                .isBetween(0L, 1000L);
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
