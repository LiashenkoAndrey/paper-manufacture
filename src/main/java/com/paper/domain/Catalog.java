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

    public Catalog(String name) {
        this.name = name;
    }

    @Builder

    public Catalog(Long id, String name) {
        super(id);
        this.name = name;
    }



    @Size(min = 4, max = 255)
    @Column(updatable = false)
    private String name;

}
