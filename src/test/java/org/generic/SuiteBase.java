package org.generic;
import java.lang.reflect.Method;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.utilities.ExcelBank;

public class SuiteBase extends BaseClass {
	@BeforeTest
	@DataProvider(name="getData")
	public static Object[][] getData(Method m) {
		String[] sheetName = m.getName().split("_", 2);
		String name=sheetName[1];
		System.out.println(name);
		int rows = ExcelBank.getRowCount(name);
		int cols = ExcelBank.getColumnCount(name);
		Object[][] data=new Object[rows-1][cols];
		for (int i = 1; i <= rows-1; i++) {
			for (int j = 0; j < cols; j++) {
				data[i-1][j]=ExcelBank.getCellValue(name, i, j);
			}
		}
		return data;
	}
}
