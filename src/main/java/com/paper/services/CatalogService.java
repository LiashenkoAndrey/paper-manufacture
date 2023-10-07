package com.paper.services;

import com.paper.domain.Catalog;

import java.util.List;

public interface CatalogService {

    List<Catalog> translateAll(List<Catalog> catalogList);
}
