package org.utilities;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.generic.BaseClass;

public class ExcelBank extends BaseClass{
	public static FileInputStream fis;
	static String path = System.getProperty("user.dir")+excel.getProperty("path");
	public static FileOutputStream fos;
	public static Workbook wb;
	public static Sheet sheet;
	public static Cell cell;
	public static Row row;
	public static Workbook getWorkbook() {
		try {
			log.info("Loading Excel File from the path : "+path);
			fis=new FileInputStream(path);
			wb = WorkbookFactory.create(fis);
			log.info("Creating the WorkBook");
		} catch (Exception e) {
			log.error("Failed to create the WorkBook : "+e.toString());

		}
		return wb;
	}
	public static Sheet getSheet(String sheetName) {
		try {
			log.info("Getting the SheetName from excel : "+sheetName);
			sheet = getWorkbook().getSheet(sheetName);
		} catch (Exception e) {
			log.error("Sheet name does not exixt"+e.toString());
		}
		return sheet;
	}
	public static int getRowCount(String sheetName) {
		int rowCount=0;
		try {
			rowCount=getSheet(sheetName).getPhysicalNumberOfRows();
			log.info("The number of Rows : "+rowCount);
		} catch (Exception e) {
			log.error("Error finding rows"+e.toString());
		}
		return rowCount;
	}
	public static int getColumnCount(String sheetName) {
		int columnCount=0;
		try {
			columnCount = getSheet(sheetName).getRow(0).getLastCellNum();
			log.info("The number of Columns : "+columnCount);
		} catch (Exception e) {
			log.error("Error finding columns"+e.toString());
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
			log.error("failed to get the data from excel file"+e.toString());
		}
		return cellValue;
	}
	public static String getCellValue(String sheetName, int rowNo,int ColNo) {
		String cellValue =null;
		try {
			sheet = getSheet(sheetName);
			cell = sheet.getRow(rowNo).getCell(ColNo);
			if(cell.getCellType()==CellType.STRING) {
				cellValue = cell.getStringCellValue();
			}
			else if(cell.getCellType()==CellType.NUMERIC) {
				cellValue=String.valueOf((int)cell.getNumericCellValue());
			}
		} catch (Exception e) {
			log.error("failed to get the data from excel file"+e.toString());
		}

		return cellValue;
	}
	public static boolean setCellValue(String sheetName, String colName,int rowNo,String data) {
		int colNo=0;
		try {
			wb = getWorkbook();
			sheet = wb.getSheet(sheetName);
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
			wb.write(fos);
			fos.close();
		} catch (Exception e) {
			log.error("Error writing to excel file"+e.toString());
			return false;
		}
		return true;
	}
}