package com.example.demo;

import java.util.List;

public class Order {
	private int orderId;
	private List<Product> products;
	
	public Order(int orderId, List<Product> products) {
		super();
		this.orderId = orderId;
		this.products = products;
	}
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	
}
