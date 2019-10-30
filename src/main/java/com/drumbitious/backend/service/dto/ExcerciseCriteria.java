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
 * Criteria class for the {@link com.drumbitious.backend.domain.Excercise} entity. This class is used
 * in {@link com.drumbitious.backend.web.rest.ExcerciseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /excercises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExcerciseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter excerciseName;

    private StringFilter description;

    private StringFilter sourceUrl;

    private IntegerFilter defaultMinutes;

    private IntegerFilter defaultBpmMin;

    private IntegerFilter defaultBpmMax;

    private BooleanFilter deactivted;

    private InstantFilter createDate;

    private InstantFilter modifyDate;

    private LongFilter creatorId;

    private LongFilter skillNameId;

    private LongFilter excerciseTypeId;

    public ExcerciseCriteria(){
    }

    public ExcerciseCriteria(ExcerciseCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.excerciseName = other.excerciseName == null ? null : other.excerciseName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.sourceUrl = other.sourceUrl == null ? null : other.sourceUrl.copy();
        this.defaultMinutes = other.defaultMinutes == null ? null : other.defaultMinutes.copy();
        this.defaultBpmMin = other.defaultBpmMin == null ? null : other.defaultBpmMin.copy();
        this.defaultBpmMax = other.defaultBpmMax == null ? null : other.defaultBpmMax.copy();
        this.deactivted = other.deactivted == null ? null : other.deactivted.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.modifyDate = other.modifyDate == null ? null : other.modifyDate.copy();
        this.creatorId = other.creatorId == null ? null : other.creatorId.copy();
        this.skillNameId = other.skillNameId == null ? null : other.skillNameId.copy();
        this.excerciseTypeId = other.excerciseTypeId == null ? null : other.excerciseTypeId.copy();
    }

    @Override
    public ExcerciseCriteria copy() {
        return new ExcerciseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getExcerciseName() {
        return excerciseName;
    }

    public void setExcerciseName(StringFilter excerciseName) {
        this.excerciseName = excerciseName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(StringFilter sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public IntegerFilter getDefaultMinutes() {
        return defaultMinutes;
    }

    public void setDefaultMinutes(IntegerFilter defaultMinutes) {
        this.defaultMinutes = defaultMinutes;
    }

    public IntegerFilter getDefaultBpmMin() {
        return defaultBpmMin;
    }

    public void setDefaultBpmMin(IntegerFilter defaultBpmMin) {
        this.defaultBpmMin = defaultBpmMin;
    }

    public IntegerFilter getDefaultBpmMax() {
        return defaultBpmMax;
    }

    public void setDefaultBpmMax(IntegerFilter defaultBpmMax) {
        this.defaultBpmMax = defaultBpmMax;
    }

    public BooleanFilter getDeactivted() {
        return deactivted;
    }

    public void setDeactivted(BooleanFilter deactivted) {
        this.deactivted = deactivted;
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

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }

    public LongFilter getSkillNameId() {
        return skillNameId;
    }

    public void setSkillNameId(LongFilter skillNameId) {
        this.skillNameId = skillNameId;
    }

    public LongFilter getExcerciseTypeId() {
        return excerciseTypeId;
    }

    public void setExcerciseTypeId(LongFilter excerciseTypeId) {
        this.excerciseTypeId = excerciseTypeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExcerciseCriteria that = (ExcerciseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(excerciseName, that.excerciseName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(sourceUrl, that.sourceUrl) &&
            Objects.equals(defaultMinutes, that.defaultMinutes) &&
            Objects.equals(defaultBpmMin, that.defaultBpmMin) &&
            Objects.equals(defaultBpmMax, that.defaultBpmMax) &&
            Objects.equals(deactivted, that.deactivted) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(modifyDate, that.modifyDate) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(skillNameId, that.skillNameId) &&
            Objects.equals(excerciseTypeId, that.excerciseTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        excerciseName,
        description,
        sourceUrl,
        defaultMinutes,
        defaultBpmMin,
        defaultBpmMax,
        deactivted,
        createDate,
        modifyDate,
        creatorId,
        skillNameId,
        excerciseTypeId
        );
    }

    @Override
    public String toString() {
        return "ExcerciseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (excerciseName != null ? "excerciseName=" + excerciseName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (sourceUrl != null ? "sourceUrl=" + sourceUrl + ", " : "") +
                (defaultMinutes != null ? "defaultMinutes=" + defaultMinutes + ", " : "") +
                (defaultBpmMin != null ? "defaultBpmMin=" + defaultBpmMin + ", " : "") +
                (defaultBpmMax != null ? "defaultBpmMax=" + defaultBpmMax + ", " : "") +
                (deactivted != null ? "deactivted=" + deactivted + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (modifyDate != null ? "modifyDate=" + modifyDate + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (skillNameId != null ? "skillNameId=" + skillNameId + ", " : "") +
                (excerciseTypeId != null ? "excerciseTypeId=" + excerciseTypeId + ", " : "") +
            "}";
    }

}
