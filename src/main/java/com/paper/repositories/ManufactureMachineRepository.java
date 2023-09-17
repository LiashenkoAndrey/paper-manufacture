package com.paper.repositories;

import com.paper.domain.ManufactureMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ManufactureMachineRepository extends JpaRepository<ManufactureMachine, Long>, ManufactureMachineCustomRepository {

    @Query("from ManufactureMachine m where m.catalog.id = :catalogId")
    List<ManufactureMachine> findAllByCatalogId(@Param("catalogId") Long catalogId);


}
