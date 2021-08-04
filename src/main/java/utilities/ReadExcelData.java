package utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelData {

	XSSFWorkbook workBook;
	XSSFSheet sheet;
	XSSFRow row;
	XSSFCell cell;

	public ReadExcelData(String path) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			workBook = new XSSFWorkbook(fis);
			sheet = workBook.getSheetAt(0);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCellData(int rowNum, int colNum) {

		row = sheet.getRow(rowNum - 1);
		cell = row.getCell(colNum - 1);
		
		DataFormatter df = new DataFormatter();
		return df.formatCellValue(cell);

	}

	public String getCellData(int rowNum, String colName) {
		row = sheet.getRow(1);
		for (int i = 0; i < row.getLastCellNum(); i++) {
			if(row.getCell(i).getStringCellValue().equalsIgnoreCase(colName)){
				DataFormatter df = new DataFormatter();
				return df.formatCellValue(sheet.getRow(rowNum).getCell(i));
			
			}
		}
		return "";
		
	}
	
	public int getRowCount(){
		return sheet.getLastRowNum()-1;
	}
	
	public int getColCount(){
		return sheet.getRow(2).getLastCellNum();
	}
}
