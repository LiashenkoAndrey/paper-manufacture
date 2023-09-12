package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(schema = "public", name = "manufacture_machine")
public class ManufactureMachine extends Good {

    public ManufactureMachine(String description, String name, Map<String, String> properties) {
        super(description, name);
        this.properties = properties;
    }

    @Builder
    public ManufactureMachine(Long id, String description, List<String> images, String name, GoodGroup goodGroupId, Map<String, String> properties, String serialNumber) {
        super(id, description, images, name, goodGroupId);
        this.serialNumber = serialNumber;
        this.properties = properties;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "manufacture_machine_properties")
    @MapKeyColumn(name = "property_name")
    @Column(name = "property_value")
    @NotNull
    protected Map<String, String> properties = new HashMap<>();

    @NotNull
    private String serialNumber;

    @Override
    public String toString() {
        return "ManufactureMachine{" +
                "properties=" + properties +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", images=" + images +
                '}';
    }
}
