package com.paper.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@NoArgsConstructor
@Getter
@ToString
@Setter
@EqualsAndHashCode(callSuper = false)
public abstract class Good extends Model {

    public Good(List<String> images) {
        this.images = images;
    }

    public Good(Catalog catalog) {
        this.catalog = catalog;
    }

    public Good(Long id) {
        super(id);
    }

    public Good(Long id, String description, String name, Catalog catalog, BigDecimal price, List<String> images) {
        super(id);
        this.description = description;
        this.name = name;
        this.catalog = catalog;
        this.price = price;
        this.images = images;
    }

    public Good(Long id, String description, String name, BigDecimal price, List<String> images) {
        super(id);
        this.description = description;
        this.name = name;
        this.price = price;
        this.images = images;
    }

    @NotNull
    protected String description;

    @NotNull
    protected String name;

    @ManyToOne
    @JsonIgnoreProperties("hibernateLazyInitializer")
    protected Catalog catalog;

    protected BigDecimal price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "good_images")
    @OrderColumn(name = "good_images_order")
    @Column(name = "image_id")
    protected List<String> images = new ArrayList<>();

}