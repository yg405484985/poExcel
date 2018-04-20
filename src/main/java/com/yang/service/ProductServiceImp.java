package com.yang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yang.bean.Product;
import com.yang.dao.ProductDao;
@Service
public class ProductServiceImp implements ProductService{
	@Autowired
	private ProductDao productDao;
	@Override
	public List<Product> showProductList() {
		// TODO Auto-generated method stub
		return productDao.showProductList();
	}
	@Override
	public void insertList(List<Product> list) {
		// TODO Auto-generated method stub
		productDao.insertList(list);
	}

}
