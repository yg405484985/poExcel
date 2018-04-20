package com.yang.control;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yang.bean.Product;
import com.yang.service.ProductService;
import com.yang.util.POIUtil;

@EnableAutoConfiguration
@RestController
@MapperScan("com.yang.dao")
@ComponentScan("com.yang.service")
public class POIExport {
	@Autowired
	private ProductService productService;
	@RequestMapping("exportExcel")
	public String exportExcel(HttpServletResponse response) throws Exception{
		String fileName = "Emp员工信息表.xlsx";
		//文件直接在浏览器上显示或者在访问时弹出文件下载对话框。 
		response.setHeader("Content-Disposition",
		"attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso-8859-1"));
		//设置响应的文本类型
		response.setContentType("application/ynd.ms-excel;charset=UTF-8");
		List<Product> list = productService.showProductList();
		Workbook workBook = POIUtil.getWorkBook(list);
		ServletOutputStream outputStream = response.getOutputStream();
		workBook.write(outputStream);
		outputStream.flush();
		outputStream.close();
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
