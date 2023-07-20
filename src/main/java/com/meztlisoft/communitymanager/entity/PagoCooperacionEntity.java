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
@Table(name = "pago_cooperaciones")
public class PagoCooperacionEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asociado_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AsociadoEntity asociado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cooperacion_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CooperacionEntity cooperacion;

    @Column(name = "abono")
    private Long abono;

    @Column(name = "fecha_abono")
    private LocalDateTime fecha_abono;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "administrador_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AdministradorEntity administrador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AsociadoEntity getAsociado() {
        return asociado;
    }

    public void setAsociado(AsociadoEntity asociado) {
        this.asociado = asociado;
    }

    public CooperacionEntity getCooperacion() {
        return cooperacion;
    }

    public void setCooperacion(CooperacionEntity cooperacion) {
        this.cooperacion = cooperacion;
    }

    public Long getAbono() {
        return abono;
    }

    public void setAbono(Long abono) {
        this.abono = abono;
    }

    public LocalDateTime getFecha_abono() {
        return fecha_abono;
    }

    public void setFecha_abono(LocalDateTime fecha_abono) {
        this.fecha_abono = fecha_abono;
    }

    public AdministradorEntity getAdministrador() {
        return administrador;
    }

    public void setAdministrador(AdministradorEntity administrador) {
        this.administrador = administrador;
    }
}
