package com.drumbitious.backend.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DrummerStatistics.
 */
@Entity
@Table(name = "drummer_statistics")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DrummerStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "self_assessed_level_speed")
    private Integer selfAssessedLevelSpeed;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "self_assessed_level_groove")
    private Integer selfAssessedLevelGroove;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "self_assessed_level_creativity")
    private Integer selfAssessedLevelCreativity;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "self_assessed_level_adaptability")
    private Integer selfAssessedLevelAdaptability;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "self_assessed_level_dynamics")
    private Integer selfAssessedLevelDynamics;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "self_assessed_level_independence")
    private Integer selfAssessedLevelIndependence;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "self_assessed_level_live_performance")
    private Integer selfAssessedLevelLivePerformance;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "self_assessed_level_reading_music")
    private Integer selfAssessedLevelReadingMusic;

    @Size(max = 10000)
    @Column(name = "note", length = 10000)
    private String note;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @NotNull
    @Column(name = "modify_date", nullable = false)
    private Instant modifyDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSelfAssessedLevelSpeed() {
        return selfAssessedLevelSpeed;
    }

    public DrummerStatistics selfAssessedLevelSpeed(Integer selfAssessedLevelSpeed) {
        this.selfAssessedLevelSpeed = selfAssessedLevelSpeed;
        return this;
    }

    public void setSelfAssessedLevelSpeed(Integer selfAssessedLevelSpeed) {
        this.selfAssessedLevelSpeed = selfAssessedLevelSpeed;
    }

    public Integer getSelfAssessedLevelGroove() {
        return selfAssessedLevelGroove;
    }

    public DrummerStatistics selfAssessedLevelGroove(Integer selfAssessedLevelGroove) {
        this.selfAssessedLevelGroove = selfAssessedLevelGroove;
        return this;
    }

    public void setSelfAssessedLevelGroove(Integer selfAssessedLevelGroove) {
        this.selfAssessedLevelGroove = selfAssessedLevelGroove;
    }

    public Integer getSelfAssessedLevelCreativity() {
        return selfAssessedLevelCreativity;
    }

    public DrummerStatistics selfAssessedLevelCreativity(Integer selfAssessedLevelCreativity) {
        this.selfAssessedLevelCreativity = selfAssessedLevelCreativity;
        return this;
    }

    public void setSelfAssessedLevelCreativity(Integer selfAssessedLevelCreativity) {
        this.selfAssessedLevelCreativity = selfAssessedLevelCreativity;
    }

    public Integer getSelfAssessedLevelAdaptability() {
        return selfAssessedLevelAdaptability;
    }

    public DrummerStatistics selfAssessedLevelAdaptability(Integer selfAssessedLevelAdaptability) {
        this.selfAssessedLevelAdaptability = selfAssessedLevelAdaptability;
        return this;
    }

    public void setSelfAssessedLevelAdaptability(Integer selfAssessedLevelAdaptability) {
        this.selfAssessedLevelAdaptability = selfAssessedLevelAdaptability;
    }

    public Integer getSelfAssessedLevelDynamics() {
        return selfAssessedLevelDynamics;
    }

    public DrummerStatistics selfAssessedLevelDynamics(Integer selfAssessedLevelDynamics) {
        this.selfAssessedLevelDynamics = selfAssessedLevelDynamics;
        return this;
    }

    public void setSelfAssessedLevelDynamics(Integer selfAssessedLevelDynamics) {
        this.selfAssessedLevelDynamics = selfAssessedLevelDynamics;
    }

    public Integer getSelfAssessedLevelIndependence() {
        return selfAssessedLevelIndependence;
    }

    public DrummerStatistics selfAssessedLevelIndependence(Integer selfAssessedLevelIndependence) {
        this.selfAssessedLevelIndependence = selfAssessedLevelIndependence;
        return this;
    }

    public void setSelfAssessedLevelIndependence(Integer selfAssessedLevelIndependence) {
        this.selfAssessedLevelIndependence = selfAssessedLevelIndependence;
    }

    public Integer getSelfAssessedLevelLivePerformance() {
        return selfAssessedLevelLivePerformance;
    }

    public DrummerStatistics selfAssessedLevelLivePerformance(Integer selfAssessedLevelLivePerformance) {
        this.selfAssessedLevelLivePerformance = selfAssessedLevelLivePerformance;
        return this;
    }

    public void setSelfAssessedLevelLivePerformance(Integer selfAssessedLevelLivePerformance) {
        this.selfAssessedLevelLivePerformance = selfAssessedLevelLivePerformance;
    }

    public Integer getSelfAssessedLevelReadingMusic() {
        return selfAssessedLevelReadingMusic;
    }

    public DrummerStatistics selfAssessedLevelReadingMusic(Integer selfAssessedLevelReadingMusic) {
        this.selfAssessedLevelReadingMusic = selfAssessedLevelReadingMusic;
        return this;
    }

    public void setSelfAssessedLevelReadingMusic(Integer selfAssessedLevelReadingMusic) {
        this.selfAssessedLevelReadingMusic = selfAssessedLevelReadingMusic;
    }

    public String getNote() {
        return note;
    }

    public DrummerStatistics note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public DrummerStatistics createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifyDate() {
        return modifyDate;
    }

    public DrummerStatistics modifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(Instant modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getUser() {
        return user;
    }

    public DrummerStatistics user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DrummerStatistics)) {
            return false;
        }
        return id != null && id.equals(((DrummerStatistics) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DrummerStatistics{" +
            "id=" + getId() +
            ", selfAssessedLevelSpeed=" + getSelfAssessedLevelSpeed() +
            ", selfAssessedLevelGroove=" + getSelfAssessedLevelGroove() +
            ", selfAssessedLevelCreativity=" + getSelfAssessedLevelCreativity() +
            ", selfAssessedLevelAdaptability=" + getSelfAssessedLevelAdaptability() +
            ", selfAssessedLevelDynamics=" + getSelfAssessedLevelDynamics() +
            ", selfAssessedLevelIndependence=" + getSelfAssessedLevelIndependence() +
            ", selfAssessedLevelLivePerformance=" + getSelfAssessedLevelLivePerformance() +
            ", selfAssessedLevelReadingMusic=" + getSelfAssessedLevelReadingMusic() +
            ", note='" + getNote() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            "}";
    }
}
