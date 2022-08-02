package com.payment.domain;

import com.payment.domain.enumeration.CuotaType;
import com.payment.domain.enumeration.RegisterState;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cuota.
 */
@Entity
@Table(name = "cuota")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cuota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private CuotaType tipo;

    @NotNull
    @Size(max = 25)
    @Column(name = "periodicidad", length = 25, nullable = false)
    private String periodicidad;

    @NotNull
    @Size(max = 50)
    @Column(name = "aplica", length = 50, nullable = false)
    private String aplica;

    @NotNull
    @Column(name = "monto", nullable = false)
    private Double monto;

    @NotNull
    @Size(max = 20)
    @Column(name = "diponibilidad", length = 20, nullable = false)
    private String diponibilidad;

    @NotNull
    @Size(max = 200)
    @Column(name = "observacion", length = 200, nullable = false)
    private String observacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
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
