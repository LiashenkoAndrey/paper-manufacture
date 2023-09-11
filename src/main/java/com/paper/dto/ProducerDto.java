package com.paper.dto;

import com.paper.domain.Image;
import com.paper.domain.Producer;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProducerDto {

    private Producer producer;

    @NotNull
    private Image image;
}
