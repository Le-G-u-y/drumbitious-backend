package com.drumbitious.backend.service.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.drumbitious.backend.domain.Plan} entity.
 */
@ApiModel(description = "Practice Plan that gets")
public class PlanDTO implements Serializable {

    private Long id;

    @NotNull
    private String planName;

    @NotNull
    private String planFocus;

    private String description;

    private Integer minutesPerSession;

    private Integer sessionsPerWeek;

    private Instant targetDate;

    private Boolean active;

    /**
     * the plan the user set to be his current plan
     */
    @NotNull
    @ApiModelProperty(value = "the plan the user set to be his current plan", required = true)
    private Instant createDate;

    @NotNull
    private Instant modifyDate;


    private Long ownerId;

    private String ownerLogin;

    private Long creatorId;

    private String creatorLogin;

    private Long exerciseConfigId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanFocus() {
        return planFocus;
    }

    public void setPlanFocus(String planFocus) {
        this.planFocus = planFocus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinutesPerSession() {
        return minutesPerSession;
    }

    public void setMinutesPerSession(Integer minutesPerSession) {
        this.minutesPerSession = minutesPerSession;
    }

    public Integer getSessionsPerWeek() {
        return sessionsPerWeek;
    }

    public void setSessionsPerWeek(Integer sessionsPerWeek) {
        this.sessionsPerWeek = sessionsPerWeek;
    }

    public Instant getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Instant targetDate) {
        this.targetDate = targetDate;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String userLogin) {
        this.ownerLogin = userLogin;
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

    public Long getExerciseConfigId() {
        return exerciseConfigId;
    }

    public void setExerciseConfigId(Long exerciseConfigId) {
        this.exerciseConfigId = exerciseConfigId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlanDTO planDTO = (PlanDTO) o;
        if (planDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanDTO{" +
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
            ", owner=" + getOwnerId() +
            ", owner='" + getOwnerLogin() + "'" +
            ", creator=" + getCreatorId() +
            ", creator='" + getCreatorLogin() + "'" +
            ", exerciseConfig=" + getExerciseConfigId() +
            "}";
    }
}
