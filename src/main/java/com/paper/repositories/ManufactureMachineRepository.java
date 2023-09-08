package com.paper.repositories;

import com.paper.domain.ManufactureMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufactureMachineRepository extends JpaRepository<ManufactureMachine, Long> {

    @Query("from  ManufactureMachine m where m.goodGroup.id = :groupId")
    List<ManufactureMachine> findAllByByGoodGroupId(@Param("groupId") Long groupId);
}
