package com.paper.domain;

import jakarta.persistence.*;
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

    public Good(String description, List<String> images, String name) {
        this.name = name;
        this.description = description;
        this.images = images;
    }

    public Good(Long id, String description, String name, List<String> images) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.images = images;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String description;

    private String name;

    @ElementCollection
    @CollectionTable(name = "good_images")
    @OrderColumn(name = "good_images_order")
    @Column(name = "image_id")
    protected List<String> images = new ArrayList<>();
}
