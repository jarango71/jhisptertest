package com.payment.service.dto;

import com.payment.domain.enumeration.RegisterState;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payment.domain.Pago} entity.
 */
public class PagoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 4)
    private String anio;

    @NotNull
    @Size(max = 50)
    private String mes;

    @NotNull
    private Double valor;

    @NotNull
    private RegisterState estado;

    @NotNull
    private LocalDate fechaGeneracion;

    @NotNull
    private LocalDate fechaPago;

    private CuotaDTO cuota;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public RegisterState getEstado() {
        return estado;
    }

    public void setEstado(RegisterState estado) {
        this.estado = estado;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public CuotaDTO getCuota() {
        return cuota;
    }

    public void setCuota(CuotaDTO cuota) {
        this.cuota = cuota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagoDTO)) {
            return false;
        }

        PagoDTO pagoDTO = (PagoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pagoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagoDTO{" +
            "id=" + getId() +
            ", anio='" + getAnio() + "'" +
            ", mes='" + getMes() + "'" +
            ", valor=" + getValor() +
            ", estado='" + getEstado() + "'" +
            ", fechaGeneracion='" + getFechaGeneracion() + "'" +
            ", fechaPago='" + getFechaPago() + "'" +
            ", cuota=" + getCuota() +
            "}";
    }
}
