package com.condominium.domain;

import com.condominium.domain.enumeration.RegisterState;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Pago.
 */
@Table("pago")
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 4)
    @Column("anio")
    private String anio;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column("mes")
    private String mes;

    @NotNull(message = "must not be null")
    @Column("valor")
    private Double valor;

    @NotNull(message = "must not be null")
    @Column("estado")
    private RegisterState estado;

    @NotNull(message = "must not be null")
    @Column("fecha_generacion")
    private LocalDate fechaGeneracion;

    @NotNull(message = "must not be null")
    @Column("fecha_pago")
    private LocalDate fechaPago;

    @Transient
    private Cuota cuota;

    @Column("cuota_id")
    private Long cuotaId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pago id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnio() {
        return this.anio;
    }

    public Pago anio(String anio) {
        this.setAnio(anio);
        return this;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return this.mes;
    }

    public Pago mes(String mes) {
        this.setMes(mes);
        return this;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Double getValor() {
        return this.valor;
    }

    public Pago valor(Double valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public RegisterState getEstado() {
        return this.estado;
    }

    public Pago estado(RegisterState estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(RegisterState estado) {
        this.estado = estado;
    }

    public LocalDate getFechaGeneracion() {
        return this.fechaGeneracion;
    }

    public Pago fechaGeneracion(LocalDate fechaGeneracion) {
        this.setFechaGeneracion(fechaGeneracion);
        return this;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDate getFechaPago() {
        return this.fechaPago;
    }

    public Pago fechaPago(LocalDate fechaPago) {
        this.setFechaPago(fechaPago);
        return this;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Cuota getCuota() {
        return this.cuota;
    }

    public void setCuota(Cuota cuota) {
        this.cuota = cuota;
        this.cuotaId = cuota != null ? cuota.getId() : null;
    }

    public Pago cuota(Cuota cuota) {
        this.setCuota(cuota);
        return this;
    }

    public Long getCuotaId() {
        return this.cuotaId;
    }

    public void setCuotaId(Long cuota) {
        this.cuotaId = cuota;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pago)) {
            return false;
        }
        return id != null && id.equals(((Pago) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pago{" +
            "id=" + getId() +
            ", anio='" + getAnio() + "'" +
            ", mes='" + getMes() + "'" +
            ", valor=" + getValor() +
            ", estado='" + getEstado() + "'" +
            ", fechaGeneracion='" + getFechaGeneracion() + "'" +
            ", fechaPago='" + getFechaPago() + "'" +
            "}";
    }
}
