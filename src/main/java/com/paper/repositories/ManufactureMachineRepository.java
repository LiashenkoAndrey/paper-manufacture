package com.paper.repositories;

import com.paper.domain.ManufactureMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufactureMachineRepository extends JpaRepository<ManufactureMachine, Long> {
}
