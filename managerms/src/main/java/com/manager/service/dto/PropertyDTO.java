package com.manager.service.dto;

import com.manager.domain.enumeration.RegisterState;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.manager.domain.Property} entity.
 */
public class PropertyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 15)
    private String manzana;

    @NotNull
    @Size(max = 15)
    private String bloque;

    @NotNull
    @Size(max = 10)
    private String numero;

    @NotNull
    @Size(max = 100)
    private String ubicacion;

    @NotNull
    @Size(max = 20)
    private String tipo;

    @NotNull
    @Size(max = 20)
    private String diponibilidad;

    @NotNull
    @Size(max = 200)
    private String observacion;

    @NotNull
    private RegisterState estado;

    private CondominiumDTO condominium;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public CondominiumDTO getCondominium() {
        return condominium;
    }

    public void setCondominium(CondominiumDTO condominium) {
        this.condominium = condominium;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyDTO)) {
            return false;
        }

        PropertyDTO propertyDTO = (PropertyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, propertyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PropertyDTO{" +
            "id=" + getId() +
            ", manzana='" + getManzana() + "'" +
            ", bloque='" + getBloque() + "'" +
            ", numero='" + getNumero() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", diponibilidad='" + getDiponibilidad() + "'" +
            ", observacion='" + getObservacion() + "'" +
            ", estado='" + getEstado() + "'" +
            ", condominium=" + getCondominium() +
            "}";
    }
}
