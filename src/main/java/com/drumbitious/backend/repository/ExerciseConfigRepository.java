package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.ExerciseConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExerciseConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseConfigRepository extends JpaRepository<ExerciseConfig, Long>, JpaSpecificationExecutor<ExerciseConfig> {

}
