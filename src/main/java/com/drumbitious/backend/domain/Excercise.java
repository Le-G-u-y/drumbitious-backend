package com.drumbitious.backend.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Excercise.
 */
@Entity
@Table(name = "excercise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Excercise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "excercise_name", length = 200, nullable = false)
    private String excerciseName;

    @Size(max = 10000)
    @Column(name = "description", length = 10000)
    private String description;

    @Size(max = 2083)
    @Column(name = "source_url", length = 2083)
    private String sourceUrl;

    @Max(value = 9000)
    @Column(name = "default_minutes")
    private Integer defaultMinutes;

    @Min(value = 1)
    @Max(value = 500)
    @Column(name = "default_bpm_min")
    private Integer defaultBpmMin;

    @Min(value = 1)
    @Max(value = 500)
    @Column(name = "default_bpm_max")
    private Integer defaultBpmMax;

    @Column(name = "deactivted")
    private Boolean deactivted;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @NotNull
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @ManyToOne
    @JsonIgnoreProperties("excercises")
    private User creator;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "excercise_skill_name",
               joinColumns = @JoinColumn(name = "excercise_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "skill_name_id", referencedColumnName = "id"))
    private Set<SkillType> skillNames = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "excercise_excercise_type",
               joinColumns = @JoinColumn(name = "excercise_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "excercise_type_id", referencedColumnName = "id"))
    private Set<ExcerciseType> excerciseTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExcerciseName() {
        return excerciseName;
    }

    public Excercise excerciseName(String excerciseName) {
        this.excerciseName = excerciseName;
        return this;
    }

    public void setExcerciseName(String excerciseName) {
        this.excerciseName = excerciseName;
    }

    public String getDescription() {
        return description;
    }

    public Excercise description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public Excercise sourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        return this;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Integer getDefaultMinutes() {
        return defaultMinutes;
    }

    public Excercise defaultMinutes(Integer defaultMinutes) {
        this.defaultMinutes = defaultMinutes;
        return this;
    }

    public void setDefaultMinutes(Integer defaultMinutes) {
        this.defaultMinutes = defaultMinutes;
    }

    public Integer getDefaultBpmMin() {
        return defaultBpmMin;
    }

    public Excercise defaultBpmMin(Integer defaultBpmMin) {
        this.defaultBpmMin = defaultBpmMin;
        return this;
    }

    public void setDefaultBpmMin(Integer defaultBpmMin) {
        this.defaultBpmMin = defaultBpmMin;
    }

    public Integer getDefaultBpmMax() {
        return defaultBpmMax;
    }

    public Excercise defaultBpmMax(Integer defaultBpmMax) {
        this.defaultBpmMax = defaultBpmMax;
        return this;
    }

    public void setDefaultBpmMax(Integer defaultBpmMax) {
        this.defaultBpmMax = defaultBpmMax;
    }

    public Boolean isDeactivted() {
        return deactivted;
    }

    public Excercise deactivted(Boolean deactivted) {
        this.deactivted = deactivted;
        return this;
    }

    public void setDeactivted(Boolean deactivted) {
        this.deactivted = deactivted;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Excercise createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public Excercise modifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreator() {
        return creator;
    }

    public Excercise creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public Set<SkillType> getSkillNames() {
        return skillNames;
    }

    public Excercise skillNames(Set<SkillType> skillTypes) {
        this.skillNames = skillTypes;
        return this;
    }

    public Excercise addSkillName(SkillType skillType) {
        this.skillNames.add(skillType);
        skillType.getExcercises().add(this);
        return this;
    }

    public Excercise removeSkillName(SkillType skillType) {
        this.skillNames.remove(skillType);
        skillType.getExcercises().remove(this);
        return this;
    }

    public void setSkillNames(Set<SkillType> skillTypes) {
        this.skillNames = skillTypes;
    }

    public Set<ExcerciseType> getExcerciseTypes() {
        return excerciseTypes;
    }

    public Excercise excerciseTypes(Set<ExcerciseType> excerciseTypes) {
        this.excerciseTypes = excerciseTypes;
        return this;
    }

    public Excercise addExcerciseType(ExcerciseType excerciseType) {
        this.excerciseTypes.add(excerciseType);
        excerciseType.getExcercises().add(this);
        return this;
    }

    public Excercise removeExcerciseType(ExcerciseType excerciseType) {
        this.excerciseTypes.remove(excerciseType);
        excerciseType.getExcercises().remove(this);
        return this;
    }

    public void setExcerciseTypes(Set<ExcerciseType> excerciseTypes) {
        this.excerciseTypes = excerciseTypes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Excercise)) {
            return false;
        }
        return id != null && id.equals(((Excercise) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Excercise{" +
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
            "}";
    }
}
