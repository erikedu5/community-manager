package com.meztlisoft.communitymanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "comitivas")
public class RetinueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "activo")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_beneficio_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UnitBenefitEntity unitBenefit;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitBenefitEntity getUnitBenefit() {
        return unitBenefit;
    }

    public void setUnitBenefit(UnitBenefitEntity unitBenefit) {
        this.unitBenefit = unitBenefit;
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
}
