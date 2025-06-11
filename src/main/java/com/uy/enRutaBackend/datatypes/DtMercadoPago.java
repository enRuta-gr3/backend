package com.uy.enRutaBackend.datatypes;

public class DtMercadoPago {
	private String access_token;
	private String title;
	private String description;
	private String category_id;
	private int quantity;
	private String currency_id;
	private double unit_price;
	private int id_venta;
	
	public DtMercadoPago() {}
	
	public DtMercadoPago(String access_token, String title, String description, String category_id, int quantity,
			String currency_id, double unit_price, int id_venta) {
		super();
		this.access_token = access_token;
		this.title = title;
		this.description = description;
		this.category_id = category_id;
		this.quantity = quantity;
		this.currency_id = currency_id;
		this.unit_price = unit_price;
		this.id_venta = id_venta;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(String currency_id) {
		this.currency_id = currency_id;
	}

	public double getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}

	public int getId_venta() {
		return id_venta;
	}

	public void setId_venta(int id_venta) {
		this.id_venta = id_venta;
	}
}
