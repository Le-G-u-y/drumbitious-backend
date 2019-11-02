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
 * Criteria class for the {@link com.drumbitious.backend.domain.FinishedSession} entity. This class is used
 * in {@link com.drumbitious.backend.web.rest.FinishedSessionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /finished-sessions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FinishedSessionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter minutesTotal;

    private StringFilter note;

    private InstantFilter createDate;

    private InstantFilter modifyDate;

    private LongFilter planId;

    private LongFilter exerciseId;

    public FinishedSessionCriteria(){
    }

    public FinishedSessionCriteria(FinishedSessionCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.minutesTotal = other.minutesTotal == null ? null : other.minutesTotal.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.modifyDate = other.modifyDate == null ? null : other.modifyDate.copy();
        this.planId = other.planId == null ? null : other.planId.copy();
        this.exerciseId = other.exerciseId == null ? null : other.exerciseId.copy();
    }

    @Override
    public FinishedSessionCriteria copy() {
        return new FinishedSessionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getMinutesTotal() {
        return minutesTotal;
    }

    public void setMinutesTotal(IntegerFilter minutesTotal) {
        this.minutesTotal = minutesTotal;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
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

    public LongFilter getPlanId() {
        return planId;
    }

    public void setPlanId(LongFilter planId) {
        this.planId = planId;
    }

    public LongFilter getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(LongFilter exerciseId) {
        this.exerciseId = exerciseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FinishedSessionCriteria that = (FinishedSessionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(minutesTotal, that.minutesTotal) &&
            Objects.equals(note, that.note) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(modifyDate, that.modifyDate) &&
            Objects.equals(planId, that.planId) &&
            Objects.equals(exerciseId, that.exerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        minutesTotal,
        note,
        createDate,
        modifyDate,
        planId,
        exerciseId
        );
    }

    @Override
    public String toString() {
        return "FinishedSessionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (minutesTotal != null ? "minutesTotal=" + minutesTotal + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (modifyDate != null ? "modifyDate=" + modifyDate + ", " : "") +
                (planId != null ? "planId=" + planId + ", " : "") +
                (exerciseId != null ? "exerciseId=" + exerciseId + ", " : "") +
            "}";
    }

}
