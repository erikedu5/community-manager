package com.meztlisoft.communitymanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "gastos")
public class BillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "concepto")
    private String concept;

    @Column(name = "cantidad")
    private Long cost;

    @Column(name = "evidencia")
    private String evidencePack;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comitiva_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private RetinueEntity retinue;

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

    public String getEvidencePack() {
        return evidencePack;
    }

    public void setEvidencePack(String evidencePack) {
        this.evidencePack = evidencePack;
    }
}
