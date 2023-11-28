package com.paper.repositories;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMDto;
import com.paper.dto.MMSearchDto;
import com.paper.dto.PricesWithGoodAmountsDto;
import com.paper.dto.SerialNumberDto;
import org.springframework.data.domain.Page;
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

    @Query(value = "select mm.id, mm.serial_number as serialNumber, mm.name, p.logotype_id as producerLogotypeId, p.id as producerId, mm.price, image_id as imageId from manufacture_machine mm" +
            "    inner join good_images gi on mm.id = gi.manufacture_machine_id" +
            "    inner join catalog c on c.id = mm.catalog_id" +
            "    inner join producer p on p.id = mm.producer_id" +
            "    where mm.catalog_id = :catalogId and gi.good_images_order = 0;", nativeQuery = true)
    Page<MMDto> findAllByCatalogId(@Param("catalogId") Long catalogId, Pageable pageable);

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


    @Query("select " +
            "m.id as id, " +
            "SUBSTRING(m.name, 0, 40) as name, " +
            "m.serialNumber as serialNumber " +
            "from ManufactureMachine m " +
            "where lower(m.serialNumber) " +
            "like lower(concat('%', :query, '%')) " +
            "or lower(m.name) like lower(concat('%', :query, '%'))   ")
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
