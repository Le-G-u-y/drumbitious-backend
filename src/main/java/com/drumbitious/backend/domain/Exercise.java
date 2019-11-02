package com.drumbitious.backend.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.drumbitious.backend.domain.enumeration.SkillType;

import com.drumbitious.backend.domain.enumeration.ExerciseType;

/**
 * A Exercise.
 */
@Entity
@Table(name = "exercise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Exercise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "exercise_name", length = 200, nullable = false)
    private String exerciseName;

    @Size(max = 10000)
    @Column(name = "description", length = 10000)
    private String description;

    @Size(max = 2083)
    @Column(name = "source_url", length = 2083)
    private String sourceUrl;

    @Max(value = 9000)
    @Column(name = "default_minutes")
    private Integer defaultMinutes;

    @Min(value = 1)
    @Max(value = 500)
    @Column(name = "default_bpm_min")
    private Integer defaultBpmMin;

    @Min(value = 1)
    @Max(value = 500)
    @Column(name = "default_bpm_max")
    private Integer defaultBpmMax;

    @Column(name = "deactivted")
    private Boolean deactivted;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @NotNull
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_type", nullable = false)
    private SkillType skillType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "exercise", nullable = false)
    private ExerciseType exercise;

    @ManyToOne
    @JsonIgnoreProperties("exercises")
    private User creator;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public Exercise exerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
        return this;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getDescription() {
        return description;
    }

    public Exercise description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public Exercise sourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        return this;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Integer getDefaultMinutes() {
        return defaultMinutes;
    }

    public Exercise defaultMinutes(Integer defaultMinutes) {
        this.defaultMinutes = defaultMinutes;
        return this;
    }

    public void setDefaultMinutes(Integer defaultMinutes) {
        this.defaultMinutes = defaultMinutes;
    }

    public Integer getDefaultBpmMin() {
        return defaultBpmMin;
    }

    public Exercise defaultBpmMin(Integer defaultBpmMin) {
        this.defaultBpmMin = defaultBpmMin;
        return this;
    }

    public void setDefaultBpmMin(Integer defaultBpmMin) {
        this.defaultBpmMin = defaultBpmMin;
    }

    public Integer getDefaultBpmMax() {
        return defaultBpmMax;
    }

    public Exercise defaultBpmMax(Integer defaultBpmMax) {
        this.defaultBpmMax = defaultBpmMax;
        return this;
    }

    public void setDefaultBpmMax(Integer defaultBpmMax) {
        this.defaultBpmMax = defaultBpmMax;
    }

    public Boolean isDeactivted() {
        return deactivted;
    }

    public Exercise deactivted(Boolean deactivted) {
        this.deactivted = deactivted;
        return this;
    }

    public void setDeactivted(Boolean deactivted) {
        this.deactivted = deactivted;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Exercise createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public Exercise modifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public Exercise skillType(SkillType skillType) {
        this.skillType = skillType;
        return this;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public ExerciseType getExercise() {
        return exercise;
    }

    public Exercise exercise(ExerciseType exercise) {
        this.exercise = exercise;
        return this;
    }

    public void setExercise(ExerciseType exercise) {
        this.exercise = exercise;
    }

    public User getCreator() {
        return creator;
    }

    public Exercise creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exercise)) {
            return false;
        }
        return id != null && id.equals(((Exercise) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Exercise{" +
            "id=" + getId() +
            ", exerciseName='" + getExerciseName() + "'" +
            ", description='" + getDescription() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            ", defaultMinutes=" + getDefaultMinutes() +
            ", defaultBpmMin=" + getDefaultBpmMin() +
            ", defaultBpmMax=" + getDefaultBpmMax() +
            ", deactivted='" + isDeactivted() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            ", skillType='" + getSkillType() + "'" +
            ", exercise='" + getExercise() + "'" +
            "}";
    }
}
