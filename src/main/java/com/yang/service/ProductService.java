package com.yang.service;

import java.util.List;

import com.yang.bean.Product;

public interface ProductService {
	List<Product> showProductList();

	void insertList(List<Product> list);
}
