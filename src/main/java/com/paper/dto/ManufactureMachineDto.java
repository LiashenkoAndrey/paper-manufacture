package com.paper.dto;

import com.paper.domain.Image;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NotNull
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class ManufactureMachineDto {

    private Map<String, String> properties = new HashMap<>();

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private String name;

    private List<Image> images;

    private Long producerId;
}
