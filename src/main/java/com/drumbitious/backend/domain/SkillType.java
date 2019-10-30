package com.drumbitious.backend.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A SkillType.
 */
@Entity
@Table(name = "skill_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SkillType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "skill_text_key", length = 50, nullable = false)
    private String skillTextKey;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillTextKey() {
        return skillTextKey;
    }

    public SkillType skillTextKey(String skillTextKey) {
        this.skillTextKey = skillTextKey;
        return this;
    }

    public void setSkillTextKey(String skillTextKey) {
        this.skillTextKey = skillTextKey;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillType)) {
            return false;
        }
        return id != null && id.equals(((SkillType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SkillType{" +
            "id=" + getId() +
            ", skillTextKey='" + getSkillTextKey() + "'" +
            "}";
    }
}
