package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;
import java.util.Arrays;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(schema = "public", name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 500)
    private String description;

    @Size(min = 3, max = 255)
    private String name;

    private LocalTime duration;

    @Basic
    private byte[] data;

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", data=" + data.length +
                '}';
    }
}
