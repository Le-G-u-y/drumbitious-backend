package com.drumbitious.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.drumbitious.backend.domain.enumeration.SkillType;
import com.drumbitious.backend.domain.enumeration.ExerciseType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.drumbitious.backend.domain.Exercise} entity. This class is used
 * in {@link com.drumbitious.backend.web.rest.ExerciseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /exercises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExerciseCriteria implements Serializable, Criteria {
    /**
     * Class for filtering SkillType
     */
    public static class SkillTypeFilter extends Filter<SkillType> {

        public SkillTypeFilter() {
        }

        public SkillTypeFilter(SkillTypeFilter filter) {
            super(filter);
        }

        @Override
        public SkillTypeFilter copy() {
            return new SkillTypeFilter(this);
        }

    }
    /**
     * Class for filtering ExerciseType
     */
    public static class ExerciseTypeFilter extends Filter<ExerciseType> {

        public ExerciseTypeFilter() {
        }

        public ExerciseTypeFilter(ExerciseTypeFilter filter) {
            super(filter);
        }

        @Override
        public ExerciseTypeFilter copy() {
            return new ExerciseTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter exerciseName;

    private StringFilter description;

    private StringFilter sourceUrl;

    private IntegerFilter defaultMinutes;

    private IntegerFilter defaultBpmMin;

    private IntegerFilter defaultBpmMax;

    private BooleanFilter deactivted;

    private InstantFilter createDate;

    private InstantFilter modifyDate;

    private SkillTypeFilter skillType;

    private ExerciseTypeFilter exercise;

    private LongFilter creatorId;

    public ExerciseCriteria(){
    }

    public ExerciseCriteria(ExerciseCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.exerciseName = other.exerciseName == null ? null : other.exerciseName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.sourceUrl = other.sourceUrl == null ? null : other.sourceUrl.copy();
        this.defaultMinutes = other.defaultMinutes == null ? null : other.defaultMinutes.copy();
        this.defaultBpmMin = other.defaultBpmMin == null ? null : other.defaultBpmMin.copy();
        this.defaultBpmMax = other.defaultBpmMax == null ? null : other.defaultBpmMax.copy();
        this.deactivted = other.deactivted == null ? null : other.deactivted.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.modifyDate = other.modifyDate == null ? null : other.modifyDate.copy();
        this.skillType = other.skillType == null ? null : other.skillType.copy();
        this.exercise = other.exercise == null ? null : other.exercise.copy();
        this.creatorId = other.creatorId == null ? null : other.creatorId.copy();
    }

    @Override
    public ExerciseCriteria copy() {
        return new ExerciseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(StringFilter exerciseName) {
        this.exerciseName = exerciseName;
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

    public SkillTypeFilter getSkillType() {
        return skillType;
    }

    public void setSkillType(SkillTypeFilter skillType) {
        this.skillType = skillType;
    }

    public ExerciseTypeFilter getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseTypeFilter exercise) {
        this.exercise = exercise;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExerciseCriteria that = (ExerciseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(exerciseName, that.exerciseName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(sourceUrl, that.sourceUrl) &&
            Objects.equals(defaultMinutes, that.defaultMinutes) &&
            Objects.equals(defaultBpmMin, that.defaultBpmMin) &&
            Objects.equals(defaultBpmMax, that.defaultBpmMax) &&
            Objects.equals(deactivted, that.deactivted) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(modifyDate, that.modifyDate) &&
            Objects.equals(skillType, that.skillType) &&
            Objects.equals(exercise, that.exercise) &&
            Objects.equals(creatorId, that.creatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        exerciseName,
        description,
        sourceUrl,
        defaultMinutes,
        defaultBpmMin,
        defaultBpmMax,
        deactivted,
        createDate,
        modifyDate,
        skillType,
        exercise,
        creatorId
        );
    }

    @Override
    public String toString() {
        return "ExerciseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (exerciseName != null ? "exerciseName=" + exerciseName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (sourceUrl != null ? "sourceUrl=" + sourceUrl + ", " : "") +
                (defaultMinutes != null ? "defaultMinutes=" + defaultMinutes + ", " : "") +
                (defaultBpmMin != null ? "defaultBpmMin=" + defaultBpmMin + ", " : "") +
                (defaultBpmMax != null ? "defaultBpmMax=" + defaultBpmMax + ", " : "") +
                (deactivted != null ? "deactivted=" + deactivted + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (modifyDate != null ? "modifyDate=" + modifyDate + ", " : "") +
                (skillType != null ? "skillType=" + skillType + ", " : "") +
                (exercise != null ? "exercise=" + exercise + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
            "}";
    }

}
