package com.manager.repository;

import com.manager.domain.Property;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Property entity.
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    default Optional<Property> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Property> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Property> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct property from Property property left join fetch property.condominium",
        countQuery = "select count(distinct property) from Property property"
    )
    Page<Property> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct property from Property property left join fetch property.condominium")
    List<Property> findAllWithToOneRelationships();

    @Query("select property from Property property left join fetch property.condominium where property.id =:id")
    Optional<Property> findOneWithToOneRelationships(@Param("id") Long id);
}
