package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table
@Builder
@Entity
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    private CatalogType type;
}
