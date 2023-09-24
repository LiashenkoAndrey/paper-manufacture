package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class Good {

    public Good(Long id) {
        this.id = id;
    }

    public Good(Long id, String description, String name, Producer producer, Catalog catalog, List<String> images, List<Video> videos, Long price) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.producer = producer;
        this.catalog = catalog;
        this.images = images;
        this.videos = videos;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    protected String description;

    @NotNull
    protected String name;

    @ManyToOne
    protected Producer producer;

    @ManyToOne
    protected Catalog catalog;

    protected Long price = 1000L; //default

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "good_images")
    @OrderColumn(name = "good_images_order")
    @Column(name = "image_id")
    protected List<String> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacture_machine_id")
    protected List<Video> videos = new ArrayList<>();
}