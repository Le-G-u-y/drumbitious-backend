package com.drumbitious.backend.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ExcerciseConfig.
 */
@Entity
@Table(name = "excercise_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExcerciseConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 1)
    @Max(value = 500)
    @Column(name = "practice_bpm")
    private Integer practiceBpm;

    @Min(value = 1)
    @Max(value = 500)
    @Column(name = "target_bpm")
    private Integer targetBpm;

    @Min(value = 1)
    @Max(value = 500)
    @Column(name = "minutes")
    private Integer minutes;

    @Size(max = 5000)
    @Column(name = "note", length = 5000)
    private String note;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @NotNull
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @OneToMany(mappedBy = "excerciseConfig")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Plan> plans = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("excerciseConfigs")
    private Excercise excercise;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPracticeBpm() {
        return practiceBpm;
    }

    public ExcerciseConfig practiceBpm(Integer practiceBpm) {
        this.practiceBpm = practiceBpm;
        return this;
    }

    public void setPracticeBpm(Integer practiceBpm) {
        this.practiceBpm = practiceBpm;
    }

    public Integer getTargetBpm() {
        return targetBpm;
    }

    public ExcerciseConfig targetBpm(Integer targetBpm) {
        this.targetBpm = targetBpm;
        return this;
    }

    public void setTargetBpm(Integer targetBpm) {
        this.targetBpm = targetBpm;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public ExcerciseConfig minutes(Integer minutes) {
        this.minutes = minutes;
        return this;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getNote() {
        return note;
    }

    public ExcerciseConfig note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public ExcerciseConfig createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public ExcerciseConfig modifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Set<Plan> getPlans() {
        return plans;
    }

    public ExcerciseConfig plans(Set<Plan> plans) {
        this.plans = plans;
        return this;
    }

    public ExcerciseConfig addPlan(Plan plan) {
        this.plans.add(plan);
        plan.setExcerciseConfig(this);
        return this;
    }

    public ExcerciseConfig removePlan(Plan plan) {
        this.plans.remove(plan);
        plan.setExcerciseConfig(null);
        return this;
    }

    public void setPlans(Set<Plan> plans) {
        this.plans = plans;
    }

    public Excercise getExcercise() {
        return excercise;
    }

    public ExcerciseConfig excercise(Excercise excercise) {
        this.excercise = excercise;
        return this;
    }

    public void setExcercise(Excercise excercise) {
        this.excercise = excercise;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExcerciseConfig)) {
            return false;
        }
        return id != null && id.equals(((ExcerciseConfig) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ExcerciseConfig{" +
            "id=" + getId() +
            ", practiceBpm=" + getPracticeBpm() +
            ", targetBpm=" + getTargetBpm() +
            ", minutes=" + getMinutes() +
            ", note='" + getNote() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            "}";
    }
}
