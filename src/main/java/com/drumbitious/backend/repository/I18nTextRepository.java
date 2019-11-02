package com.drumbitious.backend.repository;
import com.drumbitious.backend.domain.I18nText;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the I18nText entity.
 */
@SuppressWarnings("unused")
@Repository
public interface I18nTextRepository extends JpaRepository<I18nText, Long> {

}
