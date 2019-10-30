package com.drumbitious.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.drumbitious.backend.domain.Plan} entity. This class is used
 * in {@link com.drumbitious.backend.web.rest.PlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlanCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter planName;

    private StringFilter planFocus;

    private StringFilter description;

    private IntegerFilter minutesPerSession;

    private IntegerFilter sessionsPerWeek;

    private InstantFilter targetDate;

    private BooleanFilter active;

    private InstantFilter createDate;

    private InstantFilter modifyDate;

    private LongFilter ownerId;

    private LongFilter creatorId;

    private LongFilter excerciseConfigId;

    public PlanCriteria(){
    }

    public PlanCriteria(PlanCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.planName = other.planName == null ? null : other.planName.copy();
        this.planFocus = other.planFocus == null ? null : other.planFocus.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.minutesPerSession = other.minutesPerSession == null ? null : other.minutesPerSession.copy();
        this.sessionsPerWeek = other.sessionsPerWeek == null ? null : other.sessionsPerWeek.copy();
        this.targetDate = other.targetDate == null ? null : other.targetDate.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.modifyDate = other.modifyDate == null ? null : other.modifyDate.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.creatorId = other.creatorId == null ? null : other.creatorId.copy();
        this.excerciseConfigId = other.excerciseConfigId == null ? null : other.excerciseConfigId.copy();
    }

    @Override
    public PlanCriteria copy() {
        return new PlanCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPlanName() {
        return planName;
    }

    public void setPlanName(StringFilter planName) {
        this.planName = planName;
    }

    public StringFilter getPlanFocus() {
        return planFocus;
    }

    public void setPlanFocus(StringFilter planFocus) {
        this.planFocus = planFocus;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getMinutesPerSession() {
        return minutesPerSession;
    }

    public void setMinutesPerSession(IntegerFilter minutesPerSession) {
        this.minutesPerSession = minutesPerSession;
    }

    public IntegerFilter getSessionsPerWeek() {
        return sessionsPerWeek;
    }

    public void setSessionsPerWeek(IntegerFilter sessionsPerWeek) {
        this.sessionsPerWeek = sessionsPerWeek;
    }

    public InstantFilter getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(InstantFilter targetDate) {
        this.targetDate = targetDate;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public InstantFilter getCreateDate() {
        return createDate;
    }

    public void setCreateDate(InstantFilter createDate) {
        this.createDate = createDate;
    }

    public InstantFilter getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(InstantFilter modifyDate) {
        this.modifyDate = modifyDate;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }

    public LongFilter getExcerciseConfigId() {
        return excerciseConfigId;
    }

    public void setExcerciseConfigId(LongFilter excerciseConfigId) {
        this.excerciseConfigId = excerciseConfigId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlanCriteria that = (PlanCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(planName, that.planName) &&
            Objects.equals(planFocus, that.planFocus) &&
            Objects.equals(description, that.description) &&
            Objects.equals(minutesPerSession, that.minutesPerSession) &&
            Objects.equals(sessionsPerWeek, that.sessionsPerWeek) &&
            Objects.equals(targetDate, that.targetDate) &&
            Objects.equals(active, that.active) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(modifyDate, that.modifyDate) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(excerciseConfigId, that.excerciseConfigId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        planName,
        planFocus,
        description,
        minutesPerSession,
        sessionsPerWeek,
        targetDate,
        active,
        createDate,
        modifyDate,
        ownerId,
        creatorId,
        excerciseConfigId
        );
    }

    @Override
    public String toString() {
        return "PlanCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (planName != null ? "planName=" + planName + ", " : "") +
                (planFocus != null ? "planFocus=" + planFocus + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (minutesPerSession != null ? "minutesPerSession=" + minutesPerSession + ", " : "") +
                (sessionsPerWeek != null ? "sessionsPerWeek=" + sessionsPerWeek + ", " : "") +
                (targetDate != null ? "targetDate=" + targetDate + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (modifyDate != null ? "modifyDate=" + modifyDate + ", " : "") +
                (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (excerciseConfigId != null ? "excerciseConfigId=" + excerciseConfigId + ", " : "") +
            "}";
    }

}
