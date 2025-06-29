package com.uy.enRutaBackend.datatypes;


public class DtEstadisticaUsuarios {
 private long total;
 private long totalAdministradores;
 private long totalVendedores;
 private long totalClientes;

 public DtEstadisticaUsuarios(long total, long administradores, long vendedores, long clientes) {
     this.total = total;
     this.totalAdministradores = administradores;
     this.totalVendedores = vendedores;
     this.totalClientes = clientes;
 }

public long getTotal() {
	return total;
}

public void setTotal(long total) {
	this.total = total;
}

public long getTotalAdministradores() {
	return totalAdministradores;
}

public void setTotalAdministradores(long totalAdministradores) {
	this.totalAdministradores = totalAdministradores;
}

public long getTotalVendedores() {
	return totalVendedores;
}

public void setTotalVendedores(long totalVendedores) {
	this.totalVendedores = totalVendedores;
}

public long getTotalClientes() {
	return totalClientes;
}

public void setTotalClientes(long totalClientes) {
	this.totalClientes = totalClientes;
}
 
}
