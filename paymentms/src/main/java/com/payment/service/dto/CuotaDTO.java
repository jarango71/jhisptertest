package com.payment.service.dto;

import com.payment.domain.enumeration.CuotaType;
import com.payment.domain.enumeration.RegisterState;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payment.domain.Cuota} entity.
 */
public class CuotaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nombre;

    @NotNull
    private CuotaType tipo;

    @NotNull
    @Size(max = 25)
    private String periodicidad;

    @NotNull
    @Size(max = 50)
    private String aplica;

    @NotNull
    private Double monto;

    @NotNull
    @Size(max = 20)
    private String diponibilidad;

    @NotNull
    @Size(max = 200)
    private String observacion;

    @NotNull
    private RegisterState estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CuotaType getTipo() {
        return tipo;
    }

    public void setTipo(CuotaType tipo) {
        this.tipo = tipo;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getAplica() {
        return aplica;
    }

    public void setAplica(String aplica) {
        this.aplica = aplica;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getDiponibilidad() {
        return diponibilidad;
    }

    public void setDiponibilidad(String diponibilidad) {
        this.diponibilidad = diponibilidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public RegisterState getEstado() {
        return estado;
    }

    public void setEstado(RegisterState estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CuotaDTO)) {
            return false;
        }

        CuotaDTO cuotaDTO = (CuotaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cuotaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CuotaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", periodicidad='" + getPeriodicidad() + "'" +
            ", aplica='" + getAplica() + "'" +
            ", monto=" + getMonto() +
            ", diponibilidad='" + getDiponibilidad() + "'" +
            ", observacion='" + getObservacion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
