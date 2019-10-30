package com.drumbitious.backend.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.drumbitious.backend.domain.SkillType} entity.
 */
public class SkillTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String skillTextKey;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillTextKey() {
        return skillTextKey;
    }

    public void setSkillTextKey(String skillTextKey) {
        this.skillTextKey = skillTextKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SkillTypeDTO skillTypeDTO = (SkillTypeDTO) o;
        if (skillTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), skillTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SkillTypeDTO{" +
            "id=" + getId() +
            ", skillTextKey='" + getSkillTextKey() + "'" +
            "}";
    }
}
