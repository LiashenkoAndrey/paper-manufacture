package com.paper.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProducerDto {

    private Long id;

    @Size(min = 3, max = 255)
    @NotNull
    private String name;

    @Size(min = 10, max = 500)
    @NotNull
    private String description;

    private String logotypeId;

    @URL
    @NotNull
    private String websiteUrl;

    @NotNull
    @Size(min = 1, max = 10)
    private List<String> images;
}
