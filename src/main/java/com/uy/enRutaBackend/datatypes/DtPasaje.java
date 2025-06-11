package com.uy.enRutaBackend.datatypes;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtPasaje {
	private int id_pasaje;

	private double precio;

	private DtViaje viaje;

	private DtAsiento asiento;

	private DtVenta_Compra venta_compra;

	private UUID uuidAuth;

	private String ciCliente;

	public DtPasaje() {
	}

	public int getId_pasaje() {
		return id_pasaje;
	}

	public void setId_pasaje(int id_pasaje) {
		this.id_pasaje = id_pasaje;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public DtViaje getViaje() {
		return viaje;
	}

	public void setViaje(DtViaje viaje) {
		this.viaje = viaje;
	}

	public DtAsiento getAsiento() {
		return asiento;
	}

	public void setAsiento(DtAsiento asiento) {
		this.asiento = asiento;
	}

	public DtVenta_Compra getVenta_compra() {
		return venta_compra;
	}

	public void setVenta_compra(DtVenta_Compra venta_compra) {
		this.venta_compra = venta_compra;
	}

	public UUID getUuidAuth() {
		return uuidAuth;
	}

	public void setUuidAuth(UUID uuidAuth) {
		this.uuidAuth = uuidAuth;
	}

	public String getCiCliente() {
		return ciCliente;
	}

	public void setCiCliente(String ciCliente) {
		this.ciCliente = ciCliente;
	}
}