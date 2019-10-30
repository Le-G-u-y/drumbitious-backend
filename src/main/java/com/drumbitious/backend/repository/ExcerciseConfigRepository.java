package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.ExcerciseConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExcerciseConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExcerciseConfigRepository extends JpaRepository<ExcerciseConfig, Long>, JpaSpecificationExecutor<ExcerciseConfig> {

}
