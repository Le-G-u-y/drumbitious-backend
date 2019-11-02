package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.Exercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Exercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long>, JpaSpecificationExecutor<Exercise> {

    @Query("select exercise from Exercise exercise where exercise.creator.login = ?#{principal.username}")
    List<Exercise> findByCreatorIsCurrentUser();

}
