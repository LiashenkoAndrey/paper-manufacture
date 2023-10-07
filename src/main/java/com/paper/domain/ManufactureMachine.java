package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(schema = "public", name = "manufacture_machine")
public class ManufactureMachine extends Good {

    public ManufactureMachine(Long id, SortedMap<String, String> properties) {
        super(id);
        this.properties = properties;
    }

    @Builder
    public ManufactureMachine(Long id, String description, String name, Producer producer, Catalog catalog, Long price, List<String> images, List<Video> videos, SortedMap<String, String> properties, String serialNumber) {
        super(id, description, name, producer, catalog, price, images, videos);
        this.properties = properties;
        this.serialNumber = serialNumber;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "manufacture_machine_properties")
    @MapKeyColumn(name = "property_name")
    @Column(name = "property_value")
    @NotNull
    protected SortedMap<String, String> properties = new TreeMap<>();

    @NotNull
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

}
