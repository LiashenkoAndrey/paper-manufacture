package com.paper.repositories;

import com.paper.domain.Catalog;
import com.paper.domain.CatalogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    @Query("from Catalog c where c.type = :catalogType")
    List<Catalog> findAllByType(@Param("catalogType") CatalogType catalogType);
}
