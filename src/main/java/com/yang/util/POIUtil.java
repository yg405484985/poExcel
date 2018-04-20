package com.yang.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIUtil {
	
	public static <E> Workbook getWorkBook(List<E> list) throws Exception{
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row = sheet.createRow(0);
		int j=0;
		for (Field field : list.get(0).getClass().getDeclaredFields()) {
	        field.setAccessible(true);
	    	Cell cell = row.createCell(j);
	    	String title = field.getName();
	    	cell.setCellValue(title);
	    	j++;
			 }
		j=1;
		for (E entity : list) {
			Row row2 = sheet.createRow(j);
			int i=0;
			for (Field field : entity.getClass().getDeclaredFields()) {
		        field.setAccessible(true);
		    	Cell cell = row2.createCell(i);
		    	Object title = field.get(entity);
		    	if(title.toString().contains("CST")){
		    		 Date date = parse(title.toString(), "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US); 
		    		 CellStyle cellStyle = workbook.createCellStyle();
		    		 DataFormat dataFormat = workbook.createDataFormat();
		    		 cellStyle.setDataFormat(dataFormat.getFormat("yy-m-d"));
		    		 cell.setCellStyle(cellStyle);
		    		 cell.setCellValue(date);
		    	}else{
		    		cell.setCellValue(title.toString());
		    	}
		    	
		    	i++;
				 }	
			j++;
		}
		return workbook;
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
		}	
			// 判断的是 该数值是不是一个日期列
		else	if (cell.getCellStyle().getDataFormatString().contains("yy")) {
				Date dateCellValue = cell.getDateCellValue();
				// values = new SimpleDateFormat("yyyy-MM-dd
				// HH:mm:ss").format(dateCellValue);
				values = dateCellValue;
			}
		else if (cell.getCellType() == cell.CELL_TYPE_STRING) {
			values = cell.getStringCellValue();
		}
		return values;
	}
	
	
	/**
	 * 将老外时间转成时间
	 * @param str
	 * @param pattern
	 * @param locale
	 * @return
	 */
	   public static Date parse(String str, String pattern, Locale locale) { 
	        if(str == null || pattern == null) { 
	            return null; 
	        } 
	        try { 
	            return new SimpleDateFormat(pattern, locale).parse(str); 
	        } catch (ParseException e) { 
	            e.printStackTrace(); 
	        } 
	        return null; 
	    } 
	// 
	    public static String format(Date date, String pattern, Locale locale) { 
	        if(date == null || pattern == null) { 
	            return null; 
	        } 
	        return new SimpleDateFormat(pattern, locale).format(date); 
	    } 
}
