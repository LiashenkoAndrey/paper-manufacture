package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table
@ToString(callSuper = true)
@Entity
public class Catalog extends Model {

    @Builder
    public Catalog(Long id, String name, CatalogType type) {
        super(id);
        this.name = name;
        this.type = type;
    }

    @Size(min = 4, max = 255)
    @Column(updatable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private CatalogType type;

}
