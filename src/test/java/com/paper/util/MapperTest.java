package com.paper.util;

import com.github.dozermapper.core.loader.DozerBuilder;
import com.github.dozermapper.core.loader.api.FieldsMappingOptions;
import com.github.dozermapper.core.loader.api.TypeMappingOption;
import com.paper.domain.ManufactureMachine;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static com.paper.util.EntityMapper.map;
import static org.assertj.core.api.Assertions.assertThat;

public class MapperTest {

    @Test
    public void mapTest() {
        ManufactureMachine machine = new ManufactureMachine();
        machine.setProperties(new TreeMap<>(Map.of("key", "val", "1", "2")));
        machine.setName("hello");
        machine.setDescription("desc");

        var source = new ManufactureMachine();
        source.setDescription("old");
        source.setProperties(new TreeMap<>(Map.of("key", "val")));

        map(machine, source)
                .setMappingForFields("properties", "properties", FieldsMappingOptions.customConverter(MapConverter.class))
                .map();

        var expected = ManufactureMachine.builder()
                .name("hello")
                .description("desc")
                .properties(new TreeMap<>(Map.of("key", "val", "1", "2")))
                .build();

        assertThat(source).usingRecursiveComparison()
                .ignoringFields("images", "producer", "catalog", "videos")
                .isEqualTo(expected);
    }
}
