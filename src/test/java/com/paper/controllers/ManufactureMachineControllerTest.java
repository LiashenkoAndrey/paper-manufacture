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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @BeforeAll
    public void before() {
        testUtils.createDefaultManufactureMachine();
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

        MockMultipartFile file1 = new MockMultipartFile(
                "testImage1",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile file2
                = new MockMultipartFile(
                "testImage2",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(multipart("/good/manufacture-machine/new")
                        .file(file1)
                        .file(file2)
                        .content(rootNode.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(result -> {
                    Long id = Long.parseLong(result.getResponse().getContentAsString());
                    ManufactureMachine saved = machineRepository.findById(id)
                            .orElseThrow(EntityNotFoundException::new);

                    assertEquals(2, saved.getImages().size());
                    assertEquals("name1", saved.getName());
                    assertEquals("description1", saved.getDescription());
                    assertTrue(saved.getProperties().containsKey("v1"));
                    assertTrue(saved.getProperties().containsKey("v2"));
                });

        mockMvc.perform(multipart("/good/manufacture-machine/new")
                        .file(file1)
                        .file(file2)
                        .content(rootNode.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        rootNode.put("description", "fwsfsf");
        rootNode.put("name", "asdfsfsdfd");
        rootNode.putNull("properties");

        mockMvc.perform(multipart("/good/manufacture-machine/new")
                        .content(rootNode.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(multipart("/good/manufacture-machine/new")
                        .file(file1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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
        testUtils.truncateGoodImages();
        testUtils.deleteAllImages();
    }
}
