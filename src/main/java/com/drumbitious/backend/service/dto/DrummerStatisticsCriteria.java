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
 * Criteria class for the {@link com.drumbitious.backend.domain.DrummerStatistics} entity. This class is used
 * in {@link com.drumbitious.backend.web.rest.DrummerStatisticsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /drummer-statistics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DrummerStatisticsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter selfAssessedLevelSpeed;

    private IntegerFilter selfAssessedLevelGroove;

    private IntegerFilter selfAssessedLevelCreativity;

    private IntegerFilter selfAssessedLevelAdaptability;

    private IntegerFilter selfAssessedLevelDynamics;

    private IntegerFilter selfAssessedLevelIndependence;

    private IntegerFilter selfAssessedLevelLivePerformance;

    private IntegerFilter selfAssessedLevelReadingMusic;

    private StringFilter note;

    private InstantFilter createDate;

    private InstantFilter modifyDate;

    private LongFilter userId;

    public DrummerStatisticsCriteria(){
    }

    public DrummerStatisticsCriteria(DrummerStatisticsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.selfAssessedLevelSpeed = other.selfAssessedLevelSpeed == null ? null : other.selfAssessedLevelSpeed.copy();
        this.selfAssessedLevelGroove = other.selfAssessedLevelGroove == null ? null : other.selfAssessedLevelGroove.copy();
        this.selfAssessedLevelCreativity = other.selfAssessedLevelCreativity == null ? null : other.selfAssessedLevelCreativity.copy();
        this.selfAssessedLevelAdaptability = other.selfAssessedLevelAdaptability == null ? null : other.selfAssessedLevelAdaptability.copy();
        this.selfAssessedLevelDynamics = other.selfAssessedLevelDynamics == null ? null : other.selfAssessedLevelDynamics.copy();
        this.selfAssessedLevelIndependence = other.selfAssessedLevelIndependence == null ? null : other.selfAssessedLevelIndependence.copy();
        this.selfAssessedLevelLivePerformance = other.selfAssessedLevelLivePerformance == null ? null : other.selfAssessedLevelLivePerformance.copy();
        this.selfAssessedLevelReadingMusic = other.selfAssessedLevelReadingMusic == null ? null : other.selfAssessedLevelReadingMusic.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.modifyDate = other.modifyDate == null ? null : other.modifyDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public DrummerStatisticsCriteria copy() {
        return new DrummerStatisticsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getSelfAssessedLevelSpeed() {
        return selfAssessedLevelSpeed;
    }

    public void setSelfAssessedLevelSpeed(IntegerFilter selfAssessedLevelSpeed) {
        this.selfAssessedLevelSpeed = selfAssessedLevelSpeed;
    }

    public IntegerFilter getSelfAssessedLevelGroove() {
        return selfAssessedLevelGroove;
    }

    public void setSelfAssessedLevelGroove(IntegerFilter selfAssessedLevelGroove) {
        this.selfAssessedLevelGroove = selfAssessedLevelGroove;
    }

    public IntegerFilter getSelfAssessedLevelCreativity() {
        return selfAssessedLevelCreativity;
    }

    public void setSelfAssessedLevelCreativity(IntegerFilter selfAssessedLevelCreativity) {
        this.selfAssessedLevelCreativity = selfAssessedLevelCreativity;
    }

    public IntegerFilter getSelfAssessedLevelAdaptability() {
        return selfAssessedLevelAdaptability;
    }

    public void setSelfAssessedLevelAdaptability(IntegerFilter selfAssessedLevelAdaptability) {
        this.selfAssessedLevelAdaptability = selfAssessedLevelAdaptability;
    }

    public IntegerFilter getSelfAssessedLevelDynamics() {
        return selfAssessedLevelDynamics;
    }

    public void setSelfAssessedLevelDynamics(IntegerFilter selfAssessedLevelDynamics) {
        this.selfAssessedLevelDynamics = selfAssessedLevelDynamics;
    }

    public IntegerFilter getSelfAssessedLevelIndependence() {
        return selfAssessedLevelIndependence;
    }

    public void setSelfAssessedLevelIndependence(IntegerFilter selfAssessedLevelIndependence) {
        this.selfAssessedLevelIndependence = selfAssessedLevelIndependence;
    }

    public IntegerFilter getSelfAssessedLevelLivePerformance() {
        return selfAssessedLevelLivePerformance;
    }

    public void setSelfAssessedLevelLivePerformance(IntegerFilter selfAssessedLevelLivePerformance) {
        this.selfAssessedLevelLivePerformance = selfAssessedLevelLivePerformance;
    }

    public IntegerFilter getSelfAssessedLevelReadingMusic() {
        return selfAssessedLevelReadingMusic;
    }

    public void setSelfAssessedLevelReadingMusic(IntegerFilter selfAssessedLevelReadingMusic) {
        this.selfAssessedLevelReadingMusic = selfAssessedLevelReadingMusic;
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

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DrummerStatisticsCriteria that = (DrummerStatisticsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(selfAssessedLevelSpeed, that.selfAssessedLevelSpeed) &&
            Objects.equals(selfAssessedLevelGroove, that.selfAssessedLevelGroove) &&
            Objects.equals(selfAssessedLevelCreativity, that.selfAssessedLevelCreativity) &&
            Objects.equals(selfAssessedLevelAdaptability, that.selfAssessedLevelAdaptability) &&
            Objects.equals(selfAssessedLevelDynamics, that.selfAssessedLevelDynamics) &&
            Objects.equals(selfAssessedLevelIndependence, that.selfAssessedLevelIndependence) &&
            Objects.equals(selfAssessedLevelLivePerformance, that.selfAssessedLevelLivePerformance) &&
            Objects.equals(selfAssessedLevelReadingMusic, that.selfAssessedLevelReadingMusic) &&
            Objects.equals(note, that.note) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(modifyDate, that.modifyDate) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        selfAssessedLevelSpeed,
        selfAssessedLevelGroove,
        selfAssessedLevelCreativity,
        selfAssessedLevelAdaptability,
        selfAssessedLevelDynamics,
        selfAssessedLevelIndependence,
        selfAssessedLevelLivePerformance,
        selfAssessedLevelReadingMusic,
        note,
        createDate,
        modifyDate,
        userId
        );
    }

    @Override
    public String toString() {
        return "DrummerStatisticsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (selfAssessedLevelSpeed != null ? "selfAssessedLevelSpeed=" + selfAssessedLevelSpeed + ", " : "") +
                (selfAssessedLevelGroove != null ? "selfAssessedLevelGroove=" + selfAssessedLevelGroove + ", " : "") +
                (selfAssessedLevelCreativity != null ? "selfAssessedLevelCreativity=" + selfAssessedLevelCreativity + ", " : "") +
                (selfAssessedLevelAdaptability != null ? "selfAssessedLevelAdaptability=" + selfAssessedLevelAdaptability + ", " : "") +
                (selfAssessedLevelDynamics != null ? "selfAssessedLevelDynamics=" + selfAssessedLevelDynamics + ", " : "") +
                (selfAssessedLevelIndependence != null ? "selfAssessedLevelIndependence=" + selfAssessedLevelIndependence + ", " : "") +
                (selfAssessedLevelLivePerformance != null ? "selfAssessedLevelLivePerformance=" + selfAssessedLevelLivePerformance + ", " : "") +
                (selfAssessedLevelReadingMusic != null ? "selfAssessedLevelReadingMusic=" + selfAssessedLevelReadingMusic + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (modifyDate != null ? "modifyDate=" + modifyDate + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
