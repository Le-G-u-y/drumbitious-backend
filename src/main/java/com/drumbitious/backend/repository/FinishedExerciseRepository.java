package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.FinishedExercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FinishedExercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinishedExerciseRepository extends JpaRepository<FinishedExercise, Long>, JpaSpecificationExecutor<FinishedExercise> {

}
