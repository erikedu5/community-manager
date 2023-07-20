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
import java.util.Date;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "cooperaciones")
public class CooperacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comitiva_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private ComitivaEntity comitiva;

    @Column(name = "concepto")
    private String concepto;

    @Column(name = "fecha_limite")
    private Date fechaLimite;

    @Column(name = "fecha_incio")
    private Date fechaInicio;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "costo_base_cooperacion")
    private Long costoBaseCooperacion;


}
