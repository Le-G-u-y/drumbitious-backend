package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.FinishedExcercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FinishedExcercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinishedExcerciseRepository extends JpaRepository<FinishedExcercise, Long>, JpaSpecificationExecutor<FinishedExcercise> {

}
