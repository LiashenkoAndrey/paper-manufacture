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

    private String serialNumber;

    @NotNull
    @Size(min = 1, max = 10)
    private List<String> images;

    @NotNull
    private Long producerId;

    @NotNull
    private Long catalogId;
}
