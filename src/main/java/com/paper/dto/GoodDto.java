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
public class GoodDto {

    private Long id;

    @NotNull
    @Size(min = 5, max = 255)
    private String name;

    @NotNull
    private BigDecimal price;

    protected BigDecimal oldPrice;

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
