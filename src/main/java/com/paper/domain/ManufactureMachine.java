package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(schema = "public", name = "manufacture_machine")
public class ManufactureMachine extends Good {

    public ManufactureMachine(Producer producer, Catalog catalog) {
        super(producer, catalog);
    }

    @Builder
    public ManufactureMachine(Long id, String description, String name, Producer producer, Catalog catalog, BigDecimal price, List<String> images, List<Video> videos, SortedMap<String, String> properties, String serialNumber) {
        super(id, description, name, producer, catalog, price, images, videos);
        this.properties = properties;
        this.serialNumber = serialNumber;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "manufacture_machine_properties")
    @MapKeyColumn(name = "property_name")
    @Column(name = "property_value")
    protected SortedMap<String, String> properties = new TreeMap<>();

    private String serialNumber;

    @Override
    public String toString() {
        return "ManufactureMachine{" +
                "properties=" + properties +
                ", serialNumber='" + serialNumber + '\'' +
                ", id=" + super.getId() +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", producer=" + producer+
                ", catalog=" + catalog +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ManufactureMachine that = (ManufactureMachine) o;
        return Objects.equals(properties, that.properties) && Objects.equals(serialNumber, that.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), properties, serialNumber);
    }
}
