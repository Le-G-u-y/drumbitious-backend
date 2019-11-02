package com.drumbitious.backend.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.drumbitious.backend.domain.enumeration.SkillType;
import com.drumbitious.backend.domain.enumeration.ExerciseType;

/**
 * A DTO for the {@link com.drumbitious.backend.domain.Exercise} entity.
 */
public class ExerciseDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String exerciseName;

    @Size(max = 10000)
    private String description;

    @Size(max = 2083)
    private String sourceUrl;

    @Max(value = 9000)
    private Integer defaultMinutes;

    @Min(value = 1)
    @Max(value = 500)
    private Integer defaultBpmMin;

    @Min(value = 1)
    @Max(value = 500)
    private Integer defaultBpmMax;

    private Boolean deactivted;

    @NotNull
    private Instant createDate;

    @NotNull
    private Instant modifyDate;

    @NotNull
    private SkillType skillType;

    @NotNull
    private ExerciseType exercise;


    private Long creatorId;

    private String creatorLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Integer getDefaultMinutes() {
        return defaultMinutes;
    }

    public void setDefaultMinutes(Integer defaultMinutes) {
        this.defaultMinutes = defaultMinutes;
    }

    public Integer getDefaultBpmMin() {
        return defaultBpmMin;
    }

    public void setDefaultBpmMin(Integer defaultBpmMin) {
        this.defaultBpmMin = defaultBpmMin;
    }

    public Integer getDefaultBpmMax() {
        return defaultBpmMax;
    }

    public void setDefaultBpmMax(Integer defaultBpmMax) {
        this.defaultBpmMax = defaultBpmMax;
    }

    public Boolean isDeactivted() {
        return deactivted;
    }

    public void setDeactivted(Boolean deactivted) {
        this.deactivted = deactivted;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public ExerciseType getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseType exercise) {
        this.exercise = exercise;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long userId) {
        this.creatorId = userId;
    }

    public String getCreatorLogin() {
        return creatorLogin;
    }

    public void setCreatorLogin(String userLogin) {
        this.creatorLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExerciseDTO exerciseDTO = (ExerciseDTO) o;
        if (exerciseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exerciseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExerciseDTO{" +
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
            ", creator=" + getCreatorId() +
            ", creator='" + getCreatorLogin() + "'" +
            "}";
    }
}
