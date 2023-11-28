package com.paper.dto;

import com.paper.domain.Producer;

import java.util.List;

public interface MMDto {

     Long getId();

     String getSerialNumber();

     String getName();

     Long getPrice();

     List<String> getImages();
}
