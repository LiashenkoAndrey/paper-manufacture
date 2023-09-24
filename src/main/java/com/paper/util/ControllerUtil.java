package com.paper.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ControllerUtil {


    public static List<Long> parseProducerIds(String producerIds) throws JsonProcessingException {
        List<Long> producerIdsList;
        if (producerIds != null) {
            producerIdsList = new ObjectMapper().readValue(producerIds, new TypeReference<>() {});
        } else {
            producerIdsList = new ArrayList<>();
        }
        return producerIdsList;
    }
}
