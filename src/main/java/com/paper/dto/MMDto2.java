package com.paper.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Setter
public class MMDto2 {

    Long id;

    String serialNumber;

    String name;

    String producerLogotypeId;

    Long producerId;

    Long price;

    String imageId;
}
