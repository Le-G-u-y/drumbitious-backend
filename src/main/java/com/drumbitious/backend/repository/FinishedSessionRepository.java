package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.FinishedSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FinishedSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinishedSessionRepository extends JpaRepository<FinishedSession, Long>, JpaSpecificationExecutor<FinishedSession> {

}
