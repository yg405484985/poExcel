package com.yang.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yang.bean.Product;
@Repository
public interface ProductDao {
	List<Product> showProductList();

	void insertList(List<Product> list);
}
