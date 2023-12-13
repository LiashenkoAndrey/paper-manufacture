package com.paper.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
import com.paper.dto.CatalogWithGoodsCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    @Query(value = "select c.id, c.name, (select count(id) from manufacture_machine mm where mm.catalog_id = c.id) from catalog c where c.type = :catalogType", nativeQuery = true)
    List<CatalogWithGoodsCountDto> findAllByType(@Param("catalogType") String catalogType);

    Optional<Catalog> findByName(String name);
}


