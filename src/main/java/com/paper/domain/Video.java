package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(schema = "public", name = "videos")
public class Video extends Model {


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
                "id=" + super.getId() +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", data=" + data.length +
                '}';
    }
}
