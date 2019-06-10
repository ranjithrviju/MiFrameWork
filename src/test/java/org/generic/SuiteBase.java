package org.generic;
import java.lang.reflect.Method;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.utilities.ExcelBank;

public class SuiteBase {
	@BeforeTest
	@DataProvider(name="getData")
	public static Object[][] getData(Method m) {
		String sheetName = m.getName();
		int rows = ExcelBank.getRowCount(sheetName);
		int cols = ExcelBank.getColumnCount(sheetName);
		Object[][] data=new Object[rows-1][cols];
		for (int i = 1; i <= rows-1; i++) {
			for (int j = 0; j < cols; j++) {
				data[i-1][j]=ExcelBank.getCellValue(sheetName, i, j);
			}
		}
		return data;
	}
}
