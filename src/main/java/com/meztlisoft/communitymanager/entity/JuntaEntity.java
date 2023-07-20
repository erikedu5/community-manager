package com.meztlisoft.communitymanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "juntas")
public class JuntaEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comitiva_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private ComitivaEntity comitiva;

    @Lob
    private String descripcion;

    @Column(name = "fecha_junta")
    private LocalDateTime fechaJunta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComitivaEntity getComitiva() {
        return comitiva;
    }

    public void setComitiva(ComitivaEntity comitiva) {
        this.comitiva = comitiva;
    }

    public LocalDateTime getFechaJunta() {
        return fechaJunta;
    }

    public void setFechaJunta(LocalDateTime fechaJunta) {
        this.fechaJunta = fechaJunta;
    }
}
