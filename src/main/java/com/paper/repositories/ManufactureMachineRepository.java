package com.paper.repositories;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMSearchDto;
import com.paper.dto.PricesWithGoodAmountsDto;
import com.paper.dto.SerialNumberDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ManufactureMachineRepository extends JpaRepository<ManufactureMachine, Long>, ManufactureMachineCustomRepository {


    /**
     * Deletes image record from relational database
     * @param id image id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from good_images img where img.image_id = :imageId", nativeQuery = true)
    void deleteGoodImageById(@Param("imageId") String id);


    @Query(value = "select max(price) from paper_manufacture.public.manufacture_machine", nativeQuery = true)
    Long getMaxGoodPrice();


    @Query(value = "select distinct on (id) id, name, serial_number as serialNumber, gi.image_id as imageId from manufacture_machine\n" +
            "inner join public.good_images gi on manufacture_machine.id = gi.manufacture_machine_id\n" +
            "where lower(serial_number) like lower(concat('%', :query, '%'))\n" +
            "or lower(name) like lower(concat('%', :query, '%'))  ;", nativeQuery = true)
    List<MMSearchDto> findByNameContainingOrSerialNumberContainingIgnoreCase(@Param("query") String query);

    @Query(value = "select" +
            "    price," +
            "    (select" +
            "         count(id) as amount" +
            "     from manufacture_machine mm" +
            "     where" +
            "         mm.price >= (m.price - 5000)" +
            "       and mm.price <= (m.price + 5000)" +
            "     )" +
            "from manufacture_machine m group by m.price order by price", nativeQuery = true)
    List<PricesWithGoodAmountsDto> getAllPricesWithGoodAmounts();


    @Query(" from ManufactureMachine m")
    List<ManufactureMachine> getAllDto(Pageable pageable);


    @Query("select" +
            "                m.serialNumber as serialNumber," +
            "                m.id as goodId" +
            "           from ManufactureMachine m")
    List<SerialNumberDto> getAllSerialNumbers();
}
