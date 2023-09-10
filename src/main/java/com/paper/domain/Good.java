package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@NoArgsConstructor
@Getter
@ToString
@Setter
public abstract class Good {

    public Good(Long id, String description, List<String> images, String name, GoodGroup goodGroup) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.images = images;
        this.goodGroup = goodGroup;
    }

    public Good(String description, String name) {
        this.description = description;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    protected String description;

    @NotNull
    private String name;

    @ManyToOne
    private GoodGroup goodGroup;



    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "good_images")
    @OrderColumn(name = "good_images_order")
    @Column(name = "image_id")
    protected List<String> images = new ArrayList<>();
}
