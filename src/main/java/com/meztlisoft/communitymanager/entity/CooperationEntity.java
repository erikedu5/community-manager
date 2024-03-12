package com.meztlisoft.communitymanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "cooperaciones", uniqueConstraints =  @UniqueConstraint(columnNames = { "concepto", "comitiva_id"}))
public class CooperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comitiva_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RetinueEntity retinue;

    @Column(name = "concepto")
    private String concept;

    @Column(name = "fecha_limite")
    private LocalDate limitDate;

    @Column(name = "fecha_incio")
    private LocalDate startDate;

    @Column(name = "activo")
    private Boolean active;

    @Column(name = "costo_base_cooperacion")
    private Long baseCooperation;

    @Column(name = "costo_foraneo_cooperación")
    private Long notNativeCooperation;

    @Column(name = "cooperacion_por_beneficio")
    private boolean byUnity;

    @Column(name = "aplica_adulto_mayor")
    private boolean elderly;

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

    public RetinueEntity getRetinue() {
        return retinue;
    }

    public void setRetinue(RetinueEntity retinue) {
        this.retinue = retinue;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getBaseCooperation() {
        return baseCooperation;
    }

    public void setBaseCooperation(Long baseCooperation) {
        this.baseCooperation = baseCooperation;
    }

    public boolean isByUnity() {
        return byUnity;
    }

    public void setByUnity(boolean byUnity) {
        this.byUnity = byUnity;
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

    public Long getNotNativeCooperation() {
        return notNativeCooperation;
    }

    public void setNotNativeCooperation(Long notNativeCooperation) {
        this.notNativeCooperation = notNativeCooperation;
    }

    public boolean isElderly() {
        return elderly;
    }

    public void setElderly(boolean elderly) {
        this.elderly = elderly;
    }
}
