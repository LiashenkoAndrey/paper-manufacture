package com.paper.repositories;

import com.paper.domain.ManufactureMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface ManufactureMachineRepository extends JpaRepository<ManufactureMachine, Long>, ManufactureMachineCustomRepository {

    @Query("from ManufactureMachine m where m.catalog.id = :catalogId")
    List<ManufactureMachine> findAllByCatalogId(@Param("catalogId") Long catalogId);

    /**
     * Deletes image record from relational database
     * @param id image id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from good_images img where img.image_id = :imageId", nativeQuery = true)
    void deleteGoodImageById(@Param("imageId") String id);
}
