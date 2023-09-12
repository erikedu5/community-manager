package com.meztlisoft.communitymanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "entradas")
public class EntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "concepto")
    private String concept;

    @Column(name = "cantidad")
    private Long cost;

    @Column(name = "fecha_entrada")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comitiva_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RetinueEntity retinue;

    @OneToOne
    @JoinColumn(name = "cooperation_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CooperationEntity cooperation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "administrador_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AdministratorEntity administrator;

    @Column(name = "fecha_creacion")
    private LocalDateTime creationDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public RetinueEntity getRetinue() {
        return retinue;
    }

    public void setRetinue(RetinueEntity retinue) {
        this.retinue = retinue;
    }

    public CooperationEntity getCooperation() {
        return cooperation;
    }

    public void setCooperation(CooperationEntity cooperation) {
        this.cooperation = cooperation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AdministratorEntity getAdministrator() {
        return administrator;
    }

    public void setAdministrator(AdministratorEntity administrator) {
        this.administrator = administrator;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
