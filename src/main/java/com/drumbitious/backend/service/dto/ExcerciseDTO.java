package com.drumbitious.backend.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.drumbitious.backend.domain.Excercise} entity.
 */
public class ExcerciseDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String excerciseName;

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


    private Long creatorId;

    private String creatorLogin;

    private Set<SkillTypeDTO> skillNames = new HashSet<>();

    private Set<ExcerciseTypeDTO> excerciseTypes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExcerciseName() {
        return excerciseName;
    }

    public void setExcerciseName(String excerciseName) {
        this.excerciseName = excerciseName;
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

    public Set<SkillTypeDTO> getSkillNames() {
        return skillNames;
    }

    public void setSkillNames(Set<SkillTypeDTO> skillTypes) {
        this.skillNames = skillTypes;
    }

    public Set<ExcerciseTypeDTO> getExcerciseTypes() {
        return excerciseTypes;
    }

    public void setExcerciseTypes(Set<ExcerciseTypeDTO> excerciseTypes) {
        this.excerciseTypes = excerciseTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExcerciseDTO excerciseDTO = (ExcerciseDTO) o;
        if (excerciseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), excerciseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExcerciseDTO{" +
            "id=" + getId() +
            ", excerciseName='" + getExcerciseName() + "'" +
            ", description='" + getDescription() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            ", defaultMinutes=" + getDefaultMinutes() +
            ", defaultBpmMin=" + getDefaultBpmMin() +
            ", defaultBpmMax=" + getDefaultBpmMax() +
            ", deactivted='" + isDeactivted() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            ", creator=" + getCreatorId() +
            ", creator='" + getCreatorLogin() + "'" +
            "}";
    }
}
