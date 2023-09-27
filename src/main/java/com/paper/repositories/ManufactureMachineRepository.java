package com.paper.repositories;

import com.paper.domain.ManufactureMachine;
import com.paper.dto.MMDtoInt;
import com.paper.dto.PricesWithGoodAmountsDto;
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
    List<MMDtoInt> findAllByCatalogId(@Param("catalogId") Long catalogId, Pageable pageable);

    /**
     * Deletes image record from relational database
     * @param id image id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from good_images img where img.image_id = :imageId", nativeQuery = true)
    void deleteGoodImageById(@Param("imageId") String id);


    @Query(value = "select mm.id, mm.serial_number as serialNumber, mm.name, p.logotype_id as producerLogotypeId, p.id as producerId, mm.price, image_id as imageId from manufacture_machine mm" +
            "    inner join good_images gi on mm.id = gi.manufacture_machine_id" +
            "    inner join catalog c on c.id = mm.catalog_id" +
            "    inner join producer p on p.id = mm.producer_id" +
            "    where mm.catalog_id = :catalogId and gi.good_images_order = 0 and p.id in :producerIds", nativeQuery = true)
    List<MMDtoInt> findAllByCatalogIdAndProducerId(
            @Param("catalogId") Long catalogId,
            @Param("producerIds") List<Long> producerIds,
            Pageable pageable);

    @Query(value = "select mm.id, mm.serial_number as serialNumber, mm.name, p.logotype_id as producerLogotypeId, p.id as producerId, mm.price, image_id as imageId from manufacture_machine mm" +
        "    inner join good_images gi on mm.id = gi.manufacture_machine_id" +
        "    inner join catalog c on c.id = mm.catalog_id" +
        "    inner join producer p on p.id = mm.producer_id" +
        "    where mm.catalog_id = :catalogId and gi.good_images_order = 0 and (price >= :from and price <= :to)", nativeQuery = true)
    List<MMDtoInt> findAllByCatalogIdAndByPrice(
            @Param("catalogId") Long catalogId,
            @Param("from") Long from,
            @Param("to") Long to,
            Pageable pageable);

    @Query(value = "select mm.id, mm.serial_number as serialNumber, mm.name, p.logotype_id as producerLogotypeId, p.id as producerId, mm.price, image_id as imageId from manufacture_machine mm" +
            "    inner join good_images gi on mm.id = gi.manufacture_machine_id" +
            "    inner join catalog c on c.id = mm.catalog_id" +
            "    inner join producer p on p.id = mm.producer_id" +
            "    where mm.catalog_id = :catalogId " +
            "       and gi.good_images_order = 0 " +
            "       and (price >= :from and price <= :to) " +
            "       and p.id in :producerIds", nativeQuery = true)
    List<MMDtoInt> findAllByByCatalogIdAndProducerAndPrice(
            @Param("catalogId") Long catalogId,
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("producerIds") List<Long> producerIds,
            Pageable pageable);

    @Query(value = "select max(price) from paper_manufacture.public.manufacture_machine", nativeQuery = true)
    Long getMaxGoodPrice();


    @Query(value = "select" +
            "    price," +
            "    (select" +
            "         count(id) as amount" +
            "     from manufacture_machine mm" +
            "     where" +
            "         mm.price >= (m.price - 100000)" +
            "       and mm.price <= (m.price + 100000)" +
            "     )" +
            "from manufacture_machine m group by m.price;", nativeQuery = true)
    List<PricesWithGoodAmountsDto> getAllPricesWithGoodAmounts();


    @Query(value = "select mm.id, mm.serial_number as serialNumber, mm.name, p.id as producerLogotypeId, p.id as producerId, mm.price, image_id as imageId from manufacture_machine mm" +
            "    inner join good_images gi on mm.id = gi.manufacture_machine_id" +
            "    inner join catalog c on c.id = mm.catalog_id" +
            "    inner join producer p on p.id = mm.producer_id" +
            "    where gi.good_images_order = 0;", nativeQuery = true)
    List<MMDtoInt> getAllDto(Pageable pageable);

}
