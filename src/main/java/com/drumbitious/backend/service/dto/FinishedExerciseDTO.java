package com.drumbitious.backend.service.dto;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.drumbitious.backend.domain.FinishedExercise} entity.
 */
public class FinishedExerciseDTO implements Serializable {

    private Long id;

    @Min(value = 1)
    @Max(value = 500)
    private Integer actualBpm;

    @Min(value = 1)
    @Max(value = 600)
    private Integer actualMinutes;

    @NotNull
    private Instant createDate;

    @NotNull
    private Instant modifyDate;


    private Long finishedSessionId;
    /**
     * Optional reference to exercise
     */
    @ApiModelProperty(value = "Optional reference to exercise")

    private Long exerciseId;

    private String exerciseExerciseName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActualBpm() {
        return actualBpm;
    }

    public void setActualBpm(Integer actualBpm) {
        this.actualBpm = actualBpm;
    }

    public Integer getActualMinutes() {
        return actualMinutes;
    }

    public void setActualMinutes(Integer actualMinutes) {
        this.actualMinutes = actualMinutes;
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

    public Long getFinishedSessionId() {
        return finishedSessionId;
    }

    public void setFinishedSessionId(Long finishedSessionId) {
        this.finishedSessionId = finishedSessionId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseExerciseName() {
        return exerciseExerciseName;
    }

    public void setExerciseExerciseName(String exerciseExerciseName) {
        this.exerciseExerciseName = exerciseExerciseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FinishedExerciseDTO finishedExerciseDTO = (FinishedExerciseDTO) o;
        if (finishedExerciseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), finishedExerciseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FinishedExerciseDTO{" +
            "id=" + getId() +
            ", actualBpm=" + getActualBpm() +
            ", actualMinutes=" + getActualMinutes() +
            ", createDate='" + getCreateDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            ", finishedSession=" + getFinishedSessionId() +
            ", exercise=" + getExerciseId() +
            ", exercise='" + getExerciseExerciseName() + "'" +
            "}";
    }
}
