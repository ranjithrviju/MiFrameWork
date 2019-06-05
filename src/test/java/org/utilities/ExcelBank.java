package org.utilities;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.generic.BaseClass;

public class ExcelBank extends BaseClass{
	public static FileInputStream fis;
	public static FileOutputStream fos;
	public static Workbook wb;
	public static Sheet sheet;
	public static Cell cell;
	public static Row row;
	public static Workbook getWorkbook() {
		try {
			fis=new FileInputStream(System.getProperty("user.dir")+excel.getProperty("path"));
			wb = WorkbookFactory.create(fis);
		} catch (Exception e) {
			log.error("Failed to load the file from the path"+e.getLocalizedMessage());
		}
		return wb;
	}
	public static Sheet getSheet(String sheetName) {
		try {
			sheet = getWorkbook().getSheet(sheetName);
		} catch (Exception e) {
			log.error("Sheet name does not exixt"+e.getMessage());
		}
		return sheet;
	}
	public static int getRowCount(String sheetName) {
		int rowCount=0;
		try {
			rowCount=getSheet(sheetName).getPhysicalNumberOfRows();
		} catch (Exception e) {
			log.error("Error finding rows"+e.getMessage());
		}
		return rowCount;
	}
	public static int getColumnCount(String sheetName) {
		int columnCount=0;
		try {
			columnCount = getSheet(sheetName).getRow(0).getLastCellNum();
		} catch (Exception e) {
			log.error("Error finding columns"+e.getMessage());
		}
		return columnCount;
	}

	public static String getCellValue(String sheetName, String colName,int rowNo) {
		int colNo=0;
		String cellValue =null;
		try {
			Sheet sh = getSheet(sheetName);
			Row firstRow = sh.getRow(0);
			for (int i = 0; i < firstRow.getLastCellNum(); i++) {
				if(firstRow.getCell(i).getStringCellValue().equalsIgnoreCase(colName)) {
					colNo=i;
				}
			}
			cell=sh.getRow(rowNo).getCell(colNo);
			if(cell.getCellType()==CellType.STRING) {
				cellValue = cell.getStringCellValue();
			}
			else if(cell.getCellType()==CellType.NUMERIC) {
				cellValue=String.valueOf((int)cell.getNumericCellValue());
			}
		} catch (Exception e) {
			log.error("failed to get the data from excel file"+e.getMessage());
		}
		return cellValue;
	}
	public static boolean setCellValue(String sheetName, String colName,int rowNo,String data) {
		int colNo=0;
		try {
		sheet = getWorkbook().getSheet(sheetName);
			Row firstRow = sheet.getRow(0);
			for (int i = 0; i < firstRow.getLastCellNum(); i++) {
				if(firstRow.getCell(i).getStringCellValue().equals(colName)) {
					colNo=i;
				}
			}
			row = sheet.getRow(rowNo);
			if(row==null) {
				row=sheet.createRow(rowNo);
			}
			cell = row.getCell(colNo);
			if(cell==null) {
				cell = row.getCell(colNo);
			}
			cell.setCellValue(data);
			fos=new FileOutputStream(System.getProperty("user.dir")+excel.getProperty("path"));
			getWorkbook().write(fos);
			fos.close();
		} catch (Exception e) {
			log.error("Error writing to excel file"+e.getMessage());
			return false;
		}
		return true;
	}
}
