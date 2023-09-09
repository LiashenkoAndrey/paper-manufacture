package com.paper.domain;

import lombok.*;



@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Image {

    public Image(String type, byte[] image) {
        this.type = type;
        this.image = image;
    }

    private String id;

    private String type;

    private byte[] image;
}
