package com.paper.dto;

import com.paper.domain.Producer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MMDto3 {

    public MMDto3(Long id, String description, String name, Producer producer, BigDecimal price ,String serialNumber) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.serialNumber = serialNumber;
    }

    public MMDto3(Long id, String description, String name, Producer producer, BigDecimal price, List images, String serialNumber) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.images = images;
        this.serialNumber = serialNumber;
    }

    public MMDto3(List images) {
        this.images = images;
    }

    Long id;

     String description;

     String name;

     Producer producer;

     BigDecimal price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "good_images")
    @OrderColumn(name = "good_images_order")
    @Column(name = "image_id")
    List images;

     String serialNumber;
}
