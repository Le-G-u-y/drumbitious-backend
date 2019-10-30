package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.Excercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Excercise entity.
 */
@Repository
public interface ExcerciseRepository extends JpaRepository<Excercise, Long>, JpaSpecificationExecutor<Excercise> {

    @Query("select excercise from Excercise excercise where excercise.creator.login = ?#{principal.username}")
    List<Excercise> findByCreatorIsCurrentUser();

    @Query(value = "select distinct excercise from Excercise excercise left join fetch excercise.skillNames left join fetch excercise.excerciseTypes",
        countQuery = "select count(distinct excercise) from Excercise excercise")
    Page<Excercise> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct excercise from Excercise excercise left join fetch excercise.skillNames left join fetch excercise.excerciseTypes")
    List<Excercise> findAllWithEagerRelationships();

    @Query("select excercise from Excercise excercise left join fetch excercise.skillNames left join fetch excercise.excerciseTypes where excercise.id =:id")
    Optional<Excercise> findOneWithEagerRelationships(@Param("id") Long id);

}
