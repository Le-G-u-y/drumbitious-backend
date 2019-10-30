package com.drumbitious.backend.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ExcerciseType.
 */
@Entity
@Table(name = "excercise_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExcerciseType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "excercise_type_text_key", length = 50, nullable = false)
    private String excerciseTypeTextKey;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExcerciseTypeTextKey() {
        return excerciseTypeTextKey;
    }

    public ExcerciseType excerciseTypeTextKey(String excerciseTypeTextKey) {
        this.excerciseTypeTextKey = excerciseTypeTextKey;
        return this;
    }

    public void setExcerciseTypeTextKey(String excerciseTypeTextKey) {
        this.excerciseTypeTextKey = excerciseTypeTextKey;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExcerciseType)) {
            return false;
        }
        return id != null && id.equals(((ExcerciseType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ExcerciseType{" +
            "id=" + getId() +
            ", excerciseTypeTextKey='" + getExcerciseTypeTextKey() + "'" +
            "}";
    }
}
