package com.paper.domain;

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

    public Image(String type, String  base64Image) {
        this.type = type;
        this.base64Image = base64Image;
    }

    private String id;

    private String type;

    private String base64Image;
}
