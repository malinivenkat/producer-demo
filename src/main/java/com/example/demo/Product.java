package com.example.demo;

public class Product {
	private String productId;
	private int quantity;
	private int from_location;
	private int to_location;
	private int uom;
	private String uom_type;
	
	public Product(String productId, int quantity, int from_location, int to_location, int uom, String uom_type) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.from_location = from_location;
		this.to_location = to_location;
		this.uom = uom;
		this.uom_type = uom_type;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getFrom_location() {
		return from_location;
	}
	public void setFrom_location(int from_location) {
		this.from_location = from_location;
	}
	public int getTo_location() {
		return to_location;
	}
	public void setTo_location(int to_location) {
		this.to_location = to_location;
	}
	public int getUom() {
		return uom;
	}
	public void setUom(int uom) {
		this.uom = uom;
	}
	public String getUom_type() {
		return uom_type;
	}
	public void setUom_type(String uom_type) {
		this.uom_type = uom_type;
	}
	
	
}
