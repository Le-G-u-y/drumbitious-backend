package com.drumbitious.backend.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.drumbitious.backend.domain.ExcerciseType} entity.
 */
public class ExcerciseTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String excerciseTypeTextKey;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExcerciseTypeTextKey() {
        return excerciseTypeTextKey;
    }

    public void setExcerciseTypeTextKey(String excerciseTypeTextKey) {
        this.excerciseTypeTextKey = excerciseTypeTextKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExcerciseTypeDTO excerciseTypeDTO = (ExcerciseTypeDTO) o;
        if (excerciseTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), excerciseTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExcerciseTypeDTO{" +
            "id=" + getId() +
            ", excerciseTypeTextKey='" + getExcerciseTypeTextKey() + "'" +
            "}";
    }
}
