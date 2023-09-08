package com.paper.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.paper.domain.ManufactureMachine;
import com.paper.repositories.ManufactureMachineRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

    @BeforeAll
    public void before() {
        testUtils.createDefaultManufactureMachine();
    }

    @Test
    @Order(1)
    public void getViewsTest() throws Exception {
        mockMvc.perform(get("/good/manufacture-machine/new"))
                .andExpect(view().name("manufactureMachine/new"));

        mockMvc.perform(get("/good/manufacture-machine/1/update"))
                .andExpect(view().name("manufactureMachine/update"));
    }

    @Test
    @Order(2)
    public void create() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", "name1");
        rootNode.put("description", "description1");

        ObjectNode properties = mapper.createObjectNode();
        properties.put("v1", "key1");
        properties.put("v2", "key2");
        rootNode.set("properties", properties);


        mockMvc.perform(post("/good/manufacture-machine/new")
                .content(rootNode.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> {
                    Long id = Long.parseLong(result.getResponse().getContentAsString());
                    ManufactureMachine saved = machineRepository.findById(id)
                            .orElseThrow(EntityNotFoundException::new);
                    assertEquals("name1", saved.getName());
                    assertEquals("description1", saved.getDescription());
                    assertTrue(saved.getProperties().containsKey("v1"));
                    assertTrue(saved.getProperties().containsKey("v2"));
                });

        mockMvc.perform(post("/good/manufacture-machine/new")
                        .content(rootNode.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
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

        mockMvc.perform(delete("/good/manufacture-machine/100/delete"))
                .andExpect(status().isBadRequest());
    }

    @AfterAll
    public void after() {
        testUtils.truncateManufactureMachineAndGoodTypeTable();
    }
}
