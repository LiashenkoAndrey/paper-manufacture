package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(callSuper = true)
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(schema = "public", name = "manufacture_machine")
public class ManufactureMachine extends Good {

    public ManufactureMachine(Catalog catalog) {
        super(catalog);
    }

    @Builder
    public ManufactureMachine(Long id, String description, String name, Catalog catalog, BigDecimal price, List<String> images, String url) {
        super(id, description, name, catalog, price, images);
        this.url = url;
    }

    @URL
    private String url;


}
