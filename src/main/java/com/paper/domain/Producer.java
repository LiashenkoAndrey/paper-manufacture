package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Table
public class Producer extends Model {


    @Builder
    public Producer(Long id, String name, String description, String logotypeId, List<String> images, String websiteUrl) {
        super(id);
        this.name = name;
        this.description = description;
        this.logotypeId = logotypeId;
        this.images = images;
        this.websiteUrl = websiteUrl;
    }

    @Size(min = 3, max = 255)
    @NotNull
    private String name;

    @Size(min = 10, max = 500)
    @NotNull
    private String description;

    @NotNull
    private String logotypeId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "producer_images")
    @OrderColumn(name = "producer_images_order")
    @Column(name = "image_id")
    protected List<String> images = new ArrayList<>();

    @URL
    @NotNull
    private String websiteUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producer producer = (Producer) o;
        return Objects.equals(name, producer.name) && Objects.equals(description, producer.description) && Objects.equals(logotypeId, producer.logotypeId) && Objects.equals(websiteUrl, producer.websiteUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, logotypeId, websiteUrl);
    }
}
