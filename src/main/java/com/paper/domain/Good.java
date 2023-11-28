package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class Good extends Model {

    public Good(List<String> images) {
        this.images = images;
    }

    public Good(Long id) {
        super(id);
    }

    public Good(Long id, String description, String name, Producer producer, Catalog catalog, BigDecimal price, List<String> images, List<Video> videos) {
        super(id);
        this.description = description;
        this.name = name;
        this.producer = producer;
        this.catalog = catalog;
        this.price = price;
        this.images = images;
        this.videos = videos;
    }

    public Good(Long id, String description, String name, Producer producer, BigDecimal price, List<String> images) {
        super(id);
        this.description = description;
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.images = images;
    }

    @NotNull
    protected String description;

    @NotNull
    protected String name;

    @ManyToOne
    protected Producer producer;

    @ManyToOne
    protected Catalog catalog;

    protected BigDecimal price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "good_images")
    @OrderColumn(name = "good_images_order")
    @Column(name = "image_id")
    protected List<String> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacture_machine_id")
    protected List<Video> videos = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Good good = (Good) o;
        return Objects.equals(description, good.description) && Objects.equals(name, good.name) && Objects.equals(producer, good.producer) && Objects.equals(catalog, good.catalog) && Objects.equals(price, good.price) && Objects.equals(images, good.images) && Objects.equals(videos, good.videos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description, name, producer, catalog, price, images, videos);
    }
}