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
 * Criteria class for the {@link com.drumbitious.backend.domain.ExcerciseConfig} entity. This class is used
 * in {@link com.drumbitious.backend.web.rest.ExcerciseConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /excercise-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExcerciseConfigCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter practiceBpm;

    private IntegerFilter targetBpm;

    private IntegerFilter minutes;

    private StringFilter note;

    private InstantFilter createDate;

    private InstantFilter modifyDate;

    private LongFilter planId;

    private LongFilter excerciseId;

    public ExcerciseConfigCriteria(){
    }

    public ExcerciseConfigCriteria(ExcerciseConfigCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.practiceBpm = other.practiceBpm == null ? null : other.practiceBpm.copy();
        this.targetBpm = other.targetBpm == null ? null : other.targetBpm.copy();
        this.minutes = other.minutes == null ? null : other.minutes.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.modifyDate = other.modifyDate == null ? null : other.modifyDate.copy();
        this.planId = other.planId == null ? null : other.planId.copy();
        this.excerciseId = other.excerciseId == null ? null : other.excerciseId.copy();
    }

    @Override
    public ExcerciseConfigCriteria copy() {
        return new ExcerciseConfigCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPracticeBpm() {
        return practiceBpm;
    }

    public void setPracticeBpm(IntegerFilter practiceBpm) {
        this.practiceBpm = practiceBpm;
    }

    public IntegerFilter getTargetBpm() {
        return targetBpm;
    }

    public void setTargetBpm(IntegerFilter targetBpm) {
        this.targetBpm = targetBpm;
    }

    public IntegerFilter getMinutes() {
        return minutes;
    }

    public void setMinutes(IntegerFilter minutes) {
        this.minutes = minutes;
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

    public LongFilter getExcerciseId() {
        return excerciseId;
    }

    public void setExcerciseId(LongFilter excerciseId) {
        this.excerciseId = excerciseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExcerciseConfigCriteria that = (ExcerciseConfigCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(practiceBpm, that.practiceBpm) &&
            Objects.equals(targetBpm, that.targetBpm) &&
            Objects.equals(minutes, that.minutes) &&
            Objects.equals(note, that.note) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(modifyDate, that.modifyDate) &&
            Objects.equals(planId, that.planId) &&
            Objects.equals(excerciseId, that.excerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        practiceBpm,
        targetBpm,
        minutes,
        note,
        createDate,
        modifyDate,
        planId,
        excerciseId
        );
    }

    @Override
    public String toString() {
        return "ExcerciseConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (practiceBpm != null ? "practiceBpm=" + practiceBpm + ", " : "") +
                (targetBpm != null ? "targetBpm=" + targetBpm + ", " : "") +
                (minutes != null ? "minutes=" + minutes + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (modifyDate != null ? "modifyDate=" + modifyDate + ", " : "") +
                (planId != null ? "planId=" + planId + ", " : "") +
                (excerciseId != null ? "excerciseId=" + excerciseId + ", " : "") +
            "}";
    }

}
