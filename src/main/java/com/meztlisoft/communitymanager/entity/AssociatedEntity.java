package com.meztlisoft.communitymanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "asociados", uniqueConstraints =  @UniqueConstraint(columnNames = { "ciudadano_id", "comitiva_id"}))
public class AssociatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ciudadano_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CitizenEntity citizen;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "comitiva_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RetinueEntity retinue;

    @Column(name="beneficio")
    private Long benefit;

    @Column(name = "activo")
    private Boolean active;

    @Column(name = "fecha_creacion")
    private LocalDateTime creationDate;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime updateDate;

    @Column(name = "usuario_editor")
    private Long userEditor;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CitizenEntity getCitizen() {
        return citizen;
    }

    public void setCitizen(CitizenEntity citizen) {
        this.citizen = citizen;
    }

    public RetinueEntity getRetinue() {
        return retinue;
    }

    public void setRetinue(RetinueEntity retinue) {
        this.retinue = retinue;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUserEditor() {
        return userEditor;
    }

    public void setUserEditor(Long userEditor) {
        this.userEditor = userEditor;
    }

    public Long getBenefit() {
        return benefit;
    }

    public void setBenefit(Long benefit) {
        this.benefit = benefit;
    }
}
