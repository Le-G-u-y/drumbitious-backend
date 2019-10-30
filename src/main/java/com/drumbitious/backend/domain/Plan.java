package com.drumbitious.backend.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Practice Plan that gets
 */
@Entity
@Table(name = "plan")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "plan_name", nullable = false)
    private String planName;

    @NotNull
    @Column(name = "plan_focus", nullable = false)
    private String planFocus;

    @Column(name = "description")
    private String description;

    @Column(name = "minutes_per_session")
    private Integer minutesPerSession;

    @Column(name = "sessions_per_week")
    private Integer sessionsPerWeek;

    @Column(name = "target_date")
    private Instant targetDate;

    @Column(name = "active")
    private Boolean active;

    /**
     * the plan the user set to be his current plan
     */
    @NotNull
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @NotNull
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @ManyToOne
    @JsonIgnoreProperties("plans")
    private User owner;

    @ManyToOne
    @JsonIgnoreProperties("plans")
    private User creator;

    @ManyToOne
    @JsonIgnoreProperties("plans")
    private ExcerciseConfig excerciseConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public Plan planName(String planName) {
        this.planName = planName;
        return this;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanFocus() {
        return planFocus;
    }

    public Plan planFocus(String planFocus) {
        this.planFocus = planFocus;
        return this;
    }

    public void setPlanFocus(String planFocus) {
        this.planFocus = planFocus;
    }

    public String getDescription() {
        return description;
    }

    public Plan description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinutesPerSession() {
        return minutesPerSession;
    }

    public Plan minutesPerSession(Integer minutesPerSession) {
        this.minutesPerSession = minutesPerSession;
        return this;
    }

    public void setMinutesPerSession(Integer minutesPerSession) {
        this.minutesPerSession = minutesPerSession;
    }

    public Integer getSessionsPerWeek() {
        return sessionsPerWeek;
    }

    public Plan sessionsPerWeek(Integer sessionsPerWeek) {
        this.sessionsPerWeek = sessionsPerWeek;
        return this;
    }

    public void setSessionsPerWeek(Integer sessionsPerWeek) {
        this.sessionsPerWeek = sessionsPerWeek;
    }

    public Instant getTargetDate() {
        return targetDate;
    }

    public Plan targetDate(Instant targetDate) {
        this.targetDate = targetDate;
        return this;
    }

    public void setTargetDate(Instant targetDate) {
        this.targetDate = targetDate;
    }

    public Boolean isActive() {
        return active;
    }

    public Plan active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Plan createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public Plan modifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getOwner() {
        return owner;
    }

    public Plan owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public User getCreator() {
        return creator;
    }

    public Plan creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public ExcerciseConfig getExcerciseConfig() {
        return excerciseConfig;
    }

    public Plan excerciseConfig(ExcerciseConfig excerciseConfig) {
        this.excerciseConfig = excerciseConfig;
        return this;
    }

    public void setExcerciseConfig(ExcerciseConfig excerciseConfig) {
        this.excerciseConfig = excerciseConfig;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plan)) {
            return false;
        }
        return id != null && id.equals(((Plan) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Plan{" +
            "id=" + getId() +
            ", planName='" + getPlanName() + "'" +
            ", planFocus='" + getPlanFocus() + "'" +
            ", description='" + getDescription() + "'" +
            ", minutesPerSession=" + getMinutesPerSession() +
            ", sessionsPerWeek=" + getSessionsPerWeek() +
            ", targetDate='" + getTargetDate() + "'" +
            ", active='" + isActive() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            "}";
    }
}
