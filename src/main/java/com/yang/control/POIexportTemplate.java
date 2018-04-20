package com.yang.control;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yang.bean.Product;
import com.yang.service.ProductService;
import com.yang.util.POIUtil;
import com.yang.util.UUIDUtils;
@EnableAutoConfiguration
@Controller
@MapperScan("com.yang.dao")
@ComponentScan("com.yang.service")
public class POIexportTemplate {
	@Autowired
	private ProductService productService;
	@RequestMapping("downLoadTemplate")
	public String exportTemplate(HttpServletResponse response) throws Exception{
		String fileName = "上传产品信息表模板.xlsx";
		//文件直接在浏览器上显示或者在访问时弹出文件下载对话框。 
		response.setHeader("Content-Disposition",
		"attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso-8859-1"));
		//设置响应的文本类型
		response.setContentType("application/ynd.ms-excel;charset=UTF-8");
		String path="/com/yang/template/"+fileName;
		InputStream inputStream = this.getClass().getResourceAsStream(path);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
		return null;
	}
	
	@RequestMapping("upLoadTemplate")
	@ResponseBody
	public String upLoadTemplate(MultipartFile file) throws Exception{
		String filename = file.getOriginalFilename();
		InputStream inputStream = file.getInputStream();
		String end = filename.substring(filename.lastIndexOf(".")+1);
		Workbook workbook=null;
		if("xls".equals(end)){
			workbook=new HSSFWorkbook(inputStream);
		}else if("xlsx".equals(end)){
			workbook=new XSSFWorkbook(inputStream);
		}
		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		List<Product> list = new ArrayList<Product>();
		for (int i = 1; i < lastRowNum; i++) {
			Row row = sheet.getRow(i);
			Product product = new Product();
			product.setPid(UUIDUtils.getCode());
			product.setPname((String)POIUtil.fromartCell(row.getCell(1)));
			product.setMarketPrice( Double.parseDouble(POIUtil.fromartCell(row.getCell(2)).toString()));
			product.setShopPrice( Double.parseDouble(POIUtil.fromartCell(row.getCell(3)).toString()));
			product.setPimage((String)POIUtil.fromartCell(row.getCell(4)));
			Long double1 =(long) Double.parseDouble(POIUtil.fromartCell(row.getCell(5)).toString());
			Date date = new Date(double1*1000);
			product.setPdate(date);
			product.setIsHot((int) ( Double.parseDouble(POIUtil.fromartCell(row.getCell(6)).toString())));
			product.setPdesc((String)POIUtil.fromartCell(row.getCell(7)));
			product.setPflag((int) ( Double.parseDouble(POIUtil.fromartCell(row.getCell(8)).toString())));
			product.setCid((String)POIUtil.fromartCell(row.getCell(9)));
			list.add(product);
		}
		productService.insertList(list);
		
		return "ok";
	}
	
	
	

}
