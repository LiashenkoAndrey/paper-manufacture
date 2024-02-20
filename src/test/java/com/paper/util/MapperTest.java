package com.paper.util;

import com.github.dozermapper.core.loader.api.FieldsMappingOptions;
import com.paper.domain.ManufactureMachine;
import com.paper.dto.ManufactureMachineDto;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static com.paper.util.EntityMapper.map;
import static org.assertj.core.api.Assertions.assertThat;

public class MapperTest {

    @Test
    public void mapTest() {
        ManufactureMachine machine = new ManufactureMachine();
        machine.setName("hello");
        machine.setDescription("desc");

        var source = new ManufactureMachine();
        source.setDescription("old");

        map(machine, source)
                .setMappingForFields("properties", "properties", FieldsMappingOptions.customConverter(MapConverter.class))
                .map();

        var expected = ManufactureMachine.builder()
                .name("hello")
                .description("desc")
                .build();

        assertThat(source).usingRecursiveComparison()
                .ignoringFields("images", "producer", "catalog", "videos")
                .isEqualTo(expected);
    }

    @Test
    public void mapAnotherEntityTest() {
        ManufactureMachineDto dto = ManufactureMachineDto.builder()
                .name("sd")
                .build();

        ManufactureMachine machine = new ManufactureMachine();

        map(dto, machine).map();
        System.out.println(machine);
    }
}
