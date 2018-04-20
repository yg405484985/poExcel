package com.yang.control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yang.bean.Product;
import com.yang.service.ProductService;
@EnableAutoConfiguration
@RestController
@MapperScan("com.yang.dao")
@ComponentScan("com.yang.service")
public class POITest {
	@Autowired
	private  ProductService productimp;
	
	/**
	 * java写表格
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		/**
		 * 创建workbook
		 */
	Workbook workbook = new XSSFWorkbook();
	Sheet sheet = workbook.createSheet("test");
	Row row1 = sheet.createRow(0);
	Cell cell = row1.createCell(0);
	CellStyle cellStyle = workbook.createCellStyle();
	DataFormat dataFormat = workbook.createDataFormat();
	cellStyle.setDataFormat(dataFormat.getFormat("m/d/yy h:mm"));
	cell.setCellStyle(cellStyle);
	cell.setCellValue(new Date());
	FileOutputStream outputStream = new FileOutputStream("C:\\text.xlsx");
	workbook.write(outputStream);
	outputStream.flush();
	outputStream.close();
	}
	/**
	 * 测试读取表格
	 * @throws Exception
	 */
	@Test
	public void read() throws Exception{
		FileInputStream inputStream = new FileInputStream("C:\\text.xlsx");
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(0);
		Cell cell = row.getCell(0);
		Object cell2 = fromartCell(cell);
		System.out.println(cell2);
	}
	/**
	 * 测试读取整个表格  只适用于xls
	 * @throws Exception
	 */
	@Test
	public void readAll() throws Exception{
		FileInputStream inputStream = new FileInputStream("C:\\aaa.xls");
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		ExcelExtractor excelExtractor=new ExcelExtractor(workbook);
		excelExtractor.setIncludeSheetNames(false);// 我们不需要Sheet页的名字
		System.out.println(excelExtractor.getText());
	}
	@RequestMapping(value="showAll")
	public void showProductAll() throws Exception, IllegalAccessException{
		List<Product> list = productimp.showProductList();
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("product");
		int i=0;
		for (Product product : list) {
			Row row = sheet.createRow(i);
			int j=0;
		for (Field field : product.getClass().getDeclaredFields()) {
	        field.setAccessible(true);
	    	Cell cell = row.createCell(j);
	    	Object object = field.get(product);
	    	String type = field.getType().toString();
	    	if(object!=null){
	    		cell.setCellValue(object.toString());
	    	}
	    	j++;
			 }
			i++;
		}
		FileOutputStream fileOutputStream = new FileOutputStream("C:\\product.xlsx");
		workbook.write(fileOutputStream);
		fileOutputStream.flush();
		fileOutputStream.close();
		System.out.println("ok");
	}
	
	
	/**
	 * 获取cell的值
	 * 
	 * @param cell
	 * @return
	 */
	public static Object fromartCell(Cell cell) {
		Object values = null;
		if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
			values = cell.getBooleanCellValue();
		} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {

			values = cell.getNumericCellValue();
			// 判断的是 该数值是不是一个日期列
			if (cell.getCellStyle().getDataFormatString().contains("yy")) {
				Date dateCellValue = cell.getDateCellValue();
				// values = new SimpleDateFormat("yyyy-MM-dd
				// HH:mm:ss").format(dateCellValue);
				values = dateCellValue;
			}

		} else if (cell.getCellType() == cell.CELL_TYPE_STRING) {
			values = cell.getStringCellValue();
		}
		return values;
	}
	
	
	
	
	
	
	
	
}
