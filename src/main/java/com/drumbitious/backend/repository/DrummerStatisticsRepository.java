package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.DrummerStatistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DrummerStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DrummerStatisticsRepository extends JpaRepository<DrummerStatistics, Long>, JpaSpecificationExecutor<DrummerStatistics> {

}
