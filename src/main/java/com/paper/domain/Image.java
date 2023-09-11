package com.paper.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "good_images")
public class Image {

    public Image(@NonNull String type, @NonNull String  base64Image) {
        this.type = type;
        this.base64Image = base64Image;
    }

    private String id;

    @NonNull
    private String type;

    @NotNull
    private String base64Image;
}
