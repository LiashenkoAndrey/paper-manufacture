package com.paper.dto;

import com.paper.domain.Image;
import com.paper.domain.ManufactureMachine;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NotNull
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class ManufactureMachineDto {

    @NotNull
    private ManufactureMachine manufactureMachine;

    @NotNull
    private List<Image> images;

    @NotNull
    private Long producerId;
}
