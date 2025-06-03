package com.uy.enRutaBackend.datatypes;

import com.uy.enRutaBackend.entities.EstadoTransaccion;


public class DtPago {

	private int id_pago;
	
	private int monto;
	
	private EstadoTransaccion estado_trx;
	
	private DtVenta_Compra venta_compra;
    
    private DtMedio_de_Pago medio_de_pago;
    
    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public EstadoTransaccion getEstado_trx() {
        return estado_trx;
    }

    public void setEstado_trx(EstadoTransaccion estado_trx) {
        this.estado_trx = estado_trx;
    }

    public DtVenta_Compra getVenta_compra() {
        return venta_compra;
    }

    public void setVenta_compra(DtVenta_Compra venta_compra) {
        this.venta_compra = venta_compra;
    }
}