package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.Plan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Plan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long>, JpaSpecificationExecutor<Plan> {

    @Query("select plan from Plan plan where plan.owner.login = ?#{principal.username}")
    List<Plan> findByOwnerIsCurrentUser();

    @Query("select plan from Plan plan where plan.creator.login = ?#{principal.username}")
    List<Plan> findByCreatorIsCurrentUser();

}
