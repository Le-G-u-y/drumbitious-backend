package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.ExcerciseType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExcerciseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExcerciseTypeRepository extends JpaRepository<ExcerciseType, Long> {

}
