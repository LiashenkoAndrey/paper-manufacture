package com.paper.repositories;

import com.paper.domain.GoodGroup;
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
public class GoodCatalogTest {

    @Autowired
    private ManufactureMachineRepository machineRepository;

    @Autowired
    private GoodGroupRepository goodGroupRepository;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @BeforeAll
    private void before() {
        GoodGroup saved = goodGroupRepository.save(GoodGroup.builder()
                .id(1L)
                .name("Test 1 catalog")
                .build());


        ManufactureMachine manufactureMachine = ManufactureMachine.builder()
                .description("test")
                .id(1L)
                .goodGroupId(saved)
                .name("machine")
                .properties(Map.of("pr1", "val1"))
                .images(List.of("2342423423dfsdf", "sdf3r34r3f34r3"))
                .build();

        machineRepository.save(manufactureMachine);
    }

    @Test
    @Order(1)
    public void create() {
        goodGroupRepository.save(GoodGroup.builder()
                .id(2L)
                .name("Test 2 catalog")
                .build());

        assertTrue(goodGroupRepository.existsById(1L));
    }
//
//    @Test
//    @Order(2)
//    public void findGoodsByGoodGroupId() {
//        List<ManufactureMachine> machineList = machineRepository.findAllByGood_group_id(1L);
//        assertEquals(1, machineList.size());
//    }

    @Test
    @Order(3)
    public void update() {
        GoodGroup goodGroup = goodGroupRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        goodGroup.setName("another name");

        GoodGroup saved = goodGroupRepository.save(goodGroup);
        System.out.println(goodGroup);
        System.out.println(saved);
        assertNotEquals("Test 1 catalog", saved.getName());
    }

    @Test
    @Order(4)
    public void delete() {
        assertTrue(goodGroupRepository.existsById(2L));
        goodGroupRepository.deleteById(2L);
        assertFalse(goodGroupRepository.existsById(2L));
    }


    @AfterAll
    public void truncateManufactureMachineTable() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("truncate table manufacture_machine cascade").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE manufacture_machine_id_seq RESTART WITH 1").executeUpdate();

        em.createNativeQuery("truncate table good_images cascade").executeUpdate();

        em.createNativeQuery("truncate table manufacture_machine_properties cascade").executeUpdate();
        em.createNativeQuery("truncate table good_group cascade").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE good_group_id_seq RESTART WITH 1").executeUpdate();
        transaction.commit();
    }
}
