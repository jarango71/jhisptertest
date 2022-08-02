package com.condominium.domain;

import com.condominium.domain.enumeration.CuotaType;
import com.condominium.domain.enumeration.RegisterState;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Cuota.
 */
@Table("cuota")
public class Cuota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("tipo")
    private CuotaType tipo;

    @NotNull(message = "must not be null")
    @Size(max = 25)
    @Column("periodicidad")
    private String periodicidad;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column("aplica")
    private String aplica;

    @NotNull(message = "must not be null")
    @Column("monto")
    private Double monto;

    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Column("diponibilidad")
    private String diponibilidad;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("observacion")
    private String observacion;

    @NotNull(message = "must not be null")
    @Column("estado")
    private RegisterState estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cuota id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cuota nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CuotaType getTipo() {
        return this.tipo;
    }

    public Cuota tipo(CuotaType tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(CuotaType tipo) {
        this.tipo = tipo;
    }

    public String getPeriodicidad() {
        return this.periodicidad;
    }

    public Cuota periodicidad(String periodicidad) {
        this.setPeriodicidad(periodicidad);
        return this;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getAplica() {
        return this.aplica;
    }

    public Cuota aplica(String aplica) {
        this.setAplica(aplica);
        return this;
    }

    public void setAplica(String aplica) {
        this.aplica = aplica;
    }

    public Double getMonto() {
        return this.monto;
    }

    public Cuota monto(Double monto) {
        this.setMonto(monto);
        return this;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getDiponibilidad() {
        return this.diponibilidad;
    }

    public Cuota diponibilidad(String diponibilidad) {
        this.setDiponibilidad(diponibilidad);
        return this;
    }

    public void setDiponibilidad(String diponibilidad) {
        this.diponibilidad = diponibilidad;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public Cuota observacion(String observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public RegisterState getEstado() {
        return this.estado;
    }

    public Cuota estado(RegisterState estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(RegisterState estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cuota)) {
            return false;
        }
        return id != null && id.equals(((Cuota) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cuota{" +
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
