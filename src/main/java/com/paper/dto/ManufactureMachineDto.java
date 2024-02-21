package com.paper.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NotNull
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class ManufactureMachineDto {

    private Long id;

    @NotNull
    @Size(min = 5, max = 255)
    private String name;

    @NotNull
    @Size(min = 10, max = 1000)
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String url;

    private String videoUrl;

    @Size(max = 10)
    private List<String> images;

    @Size(max = 10)
    private List<String> externalImagesUrls;

    @NotNull
    private Long catalogId;
}
