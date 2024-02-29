package com.paper.repositories;

import com.paper.domain.Catalog;
import com.paper.dto.CatalogWithGoodsCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    @Query(value = "select c.id, c.name, (select count(id) from manufacture_machine mm where mm.catalog_id = c.id) from catalog c", nativeQuery = true)
    List<CatalogWithGoodsCountDto> findAllCatalogs();

    @Modifying
    @Transactional
    @Query("update Catalog c set c.name = :name where c.id = :id")
    void updateCatalogName(@Param("name") String name, @Param("id") Long id);

    Catalog findByName(String name);
}


