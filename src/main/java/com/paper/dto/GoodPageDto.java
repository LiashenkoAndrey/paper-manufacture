package com.paper.dto;

import com.paper.domain.ManufactureMachine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class GoodPageDto {

    public GoodPageDto(List<ManufactureMachine> goodsList, Long totalItems) {
        this.goodsList = goodsList;
        this.totalItems = totalItems;
    }

    List<ManufactureMachine> goodsList = new ArrayList<>();

    Long totalItems = 0L;
}
