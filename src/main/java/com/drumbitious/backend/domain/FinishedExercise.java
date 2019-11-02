package com.drumbitious.backend.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A FinishedExercise.
 */
@Entity
@Table(name = "finished_exercise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FinishedExercise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 1)
    @Max(value = 500)
    @Column(name = "actual_bpm")
    private Integer actualBpm;

    @Min(value = 1)
    @Max(value = 600)
    @Column(name = "actual_minutes")
    private Integer actualMinutes;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @NotNull
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @ManyToOne
    @JsonIgnoreProperties("finishedExercises")
    private FinishedSession finishedSession;

    /**
     * Optional reference to exercise
     */
    @ManyToOne
    @JsonIgnoreProperties("finishedExercises")
    private Exercise exercise;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActualBpm() {
        return actualBpm;
    }

    public FinishedExercise actualBpm(Integer actualBpm) {
        this.actualBpm = actualBpm;
        return this;
    }

    public void setActualBpm(Integer actualBpm) {
        this.actualBpm = actualBpm;
    }

    public Integer getActualMinutes() {
        return actualMinutes;
    }

    public FinishedExercise actualMinutes(Integer actualMinutes) {
        this.actualMinutes = actualMinutes;
        return this;
    }

    public void setActualMinutes(Integer actualMinutes) {
        this.actualMinutes = actualMinutes;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public FinishedExercise createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public FinishedExercise modifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }

    public FinishedSession getFinishedSession() {
        return finishedSession;
    }

    public FinishedExercise finishedSession(FinishedSession finishedSession) {
        this.finishedSession = finishedSession;
        return this;
    }

    public void setFinishedSession(FinishedSession finishedSession) {
        this.finishedSession = finishedSession;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public FinishedExercise exercise(Exercise exercise) {
        this.exercise = exercise;
        return this;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinishedExercise)) {
            return false;
        }
        return id != null && id.equals(((FinishedExercise) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FinishedExercise{" +
            "id=" + getId() +
            ", actualBpm=" + getActualBpm() +
            ", actualMinutes=" + getActualMinutes() +
            ", createDate='" + getCreateDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            "}";
    }
}
