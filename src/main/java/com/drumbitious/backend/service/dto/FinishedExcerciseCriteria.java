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
 * Criteria class for the {@link com.drumbitious.backend.domain.FinishedExcercise} entity. This class is used
 * in {@link com.drumbitious.backend.web.rest.FinishedExcerciseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /finished-excercises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FinishedExcerciseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter actualBpm;

    private IntegerFilter actualMinutes;

    private InstantFilter createDate;

    private InstantFilter modifyDate;

    private LongFilter finishedSessionId;

    private LongFilter excerciseId;

    public FinishedExcerciseCriteria(){
    }

    public FinishedExcerciseCriteria(FinishedExcerciseCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.actualBpm = other.actualBpm == null ? null : other.actualBpm.copy();
        this.actualMinutes = other.actualMinutes == null ? null : other.actualMinutes.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.modifyDate = other.modifyDate == null ? null : other.modifyDate.copy();
        this.finishedSessionId = other.finishedSessionId == null ? null : other.finishedSessionId.copy();
        this.excerciseId = other.excerciseId == null ? null : other.excerciseId.copy();
    }

    @Override
    public FinishedExcerciseCriteria copy() {
        return new FinishedExcerciseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getActualBpm() {
        return actualBpm;
    }

    public void setActualBpm(IntegerFilter actualBpm) {
        this.actualBpm = actualBpm;
    }

    public IntegerFilter getActualMinutes() {
        return actualMinutes;
    }

    public void setActualMinutes(IntegerFilter actualMinutes) {
        this.actualMinutes = actualMinutes;
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

    public LongFilter getFinishedSessionId() {
        return finishedSessionId;
    }

    public void setFinishedSessionId(LongFilter finishedSessionId) {
        this.finishedSessionId = finishedSessionId;
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
        final FinishedExcerciseCriteria that = (FinishedExcerciseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(actualBpm, that.actualBpm) &&
            Objects.equals(actualMinutes, that.actualMinutes) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(modifyDate, that.modifyDate) &&
            Objects.equals(finishedSessionId, that.finishedSessionId) &&
            Objects.equals(excerciseId, that.excerciseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        actualBpm,
        actualMinutes,
        createDate,
        modifyDate,
        finishedSessionId,
        excerciseId
        );
    }

    @Override
    public String toString() {
        return "FinishedExcerciseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (actualBpm != null ? "actualBpm=" + actualBpm + ", " : "") +
                (actualMinutes != null ? "actualMinutes=" + actualMinutes + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (modifyDate != null ? "modifyDate=" + modifyDate + ", " : "") +
                (finishedSessionId != null ? "finishedSessionId=" + finishedSessionId + ", " : "") +
                (excerciseId != null ? "excerciseId=" + excerciseId + ", " : "") +
            "}";
    }

}
