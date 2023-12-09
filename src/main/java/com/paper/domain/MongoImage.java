package com.paper.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "good_images")
public class MongoImage {

    public MongoImage(@NonNull String type, Binary image) {
        this.type = type;
        this.image = image;
    }

    public MongoImage(String id) {
        this.id = id;
    }

    private String id;

    @NonNull
    private String type;

    @NotNull
    private Binary image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MongoImage that = (MongoImage) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, image);
    }
}
