package com.condominium.domain;

import com.condominium.domain.enumeration.RegisterState;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Property.
 */
@Table("property")
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 15)
    @Column("manzana")
    private String manzana;

    @NotNull(message = "must not be null")
    @Size(max = 15)
    @Column("bloque")
    private String bloque;

    @NotNull(message = "must not be null")
    @Size(max = 10)
    @Column("numero")
    private String numero;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("ubicacion")
    private String ubicacion;

    @NotNull(message = "must not be null")
    @Size(max = 20)
    @Column("tipo")
    private String tipo;

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

    @Transient
    private Condominium condominium;

    @Column("condominium_id")
    private Long condominiumId;

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
        this.condominiumId = condominium != null ? condominium.getId() : null;
    }

    public Property condominium(Condominium condominium) {
        this.setCondominium(condominium);
        return this;
    }

    public Long getCondominiumId() {
        return this.condominiumId;
    }

    public void setCondominiumId(Long condominium) {
        this.condominiumId = condominium;
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
