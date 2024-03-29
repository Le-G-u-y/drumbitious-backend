package com.drumbitious.backend.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A FinishedSession.
 */
@Entity
@Table(name = "finished_session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FinishedSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 1)
    @Max(value = 600)
    @Column(name = "minutes_total")
    private Integer minutesTotal;

    @Size(max = 5000)
    @Column(name = "note", length = 5000)
    private String note;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @NotNull
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Plan plan;

    @OneToMany(mappedBy = "finishedSession")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FinishedExercise> exercises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinutesTotal() {
        return minutesTotal;
    }

    public FinishedSession minutesTotal(Integer minutesTotal) {
        this.minutesTotal = minutesTotal;
        return this;
    }

    public void setMinutesTotal(Integer minutesTotal) {
        this.minutesTotal = minutesTotal;
    }

    public String getNote() {
        return note;
    }

    public FinishedSession note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public FinishedSession createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public FinishedSession modifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Plan getPlan() {
        return plan;
    }

    public FinishedSession plan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Set<FinishedExercise> getExercises() {
        return exercises;
    }

    public FinishedSession exercises(Set<FinishedExercise> finishedExercises) {
        this.exercises = finishedExercises;
        return this;
    }

    public FinishedSession addExercise(FinishedExercise finishedExercise) {
        this.exercises.add(finishedExercise);
        finishedExercise.setFinishedSession(this);
        return this;
    }

    public FinishedSession removeExercise(FinishedExercise finishedExercise) {
        this.exercises.remove(finishedExercise);
        finishedExercise.setFinishedSession(null);
        return this;
    }

    public void setExercises(Set<FinishedExercise> finishedExercises) {
        this.exercises = finishedExercises;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinishedSession)) {
            return false;
        }
        return id != null && id.equals(((FinishedSession) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FinishedSession{" +
            "id=" + getId() +
            ", minutesTotal=" + getMinutesTotal() +
            ", note='" + getNote() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            "}";
    }
}
