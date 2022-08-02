package com.manager.domain;

import com.manager.domain.enumeration.RegisterState;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Property.
 */
@Entity
@Table(name = "property")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 15)
    @Column(name = "manzana", length = 15, nullable = false)
    private String manzana;

    @NotNull
    @Size(max = 15)
    @Column(name = "bloque", length = 15, nullable = false)
    private String bloque;

    @NotNull
    @Size(max = 10)
    @Column(name = "numero", length = 10, nullable = false)
    private String numero;

    @NotNull
    @Size(max = 100)
    @Column(name = "ubicacion", length = 100, nullable = false)
    private String ubicacion;

    @NotNull
    @Size(max = 20)
    @Column(name = "tipo", length = 20, nullable = false)
    private String tipo;

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

    @ManyToOne(optional = false)
    @NotNull
    private Condominium condominium;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Property id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManzana() {
        return this.manzana;
    }

    public Property manzana(String manzana) {
        this.setManzana(manzana);
        return this;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getBloque() {
        return this.bloque;
    }

    public Property bloque(String bloque) {
        this.setBloque(bloque);
        return this;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public String getNumero() {
        return this.numero;
    }

    public Property numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public Property ubicacion(String ubicacion) {
        this.setUbicacion(ubicacion);
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Property tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDiponibilidad() {
        return this.diponibilidad;
    }

    public Property diponibilidad(String diponibilidad) {
        this.setDiponibilidad(diponibilidad);
        return this;
    }

    public void setDiponibilidad(String diponibilidad) {
        this.diponibilidad = diponibilidad;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public Property observacion(String observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public RegisterState getEstado() {
        return this.estado;
    }

    public Property estado(RegisterState estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(RegisterState estado) {
        this.estado = estado;
    }

    public Condominium getCondominium() {
        return this.condominium;
    }

    public void setCondominium(Condominium condominium) {
        this.condominium = condominium;
    }

    public Property condominium(Condominium condominium) {
        this.setCondominium(condominium);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Property)) {
            return false;
        }
        return id != null && id.equals(((Property) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Property{" +
            "id=" + getId() +
            ", manzana='" + getManzana() + "'" +
            ", bloque='" + getBloque() + "'" +
            ", numero='" + getNumero() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", diponibilidad='" + getDiponibilidad() + "'" +
            ", observacion='" + getObservacion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
