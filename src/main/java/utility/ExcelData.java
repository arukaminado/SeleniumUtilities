package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelData {

	public String[][] readData(String srcFile, String sheetName) {

		try {
			FileInputStream file = new FileInputStream(new File(srcFile));

			// Get the workbook instance for XLSX file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first sheet from the workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);

			int lastRow = sheet.getPhysicalNumberOfRows();
			int lastColumn = sheet.getRow(1).getPhysicalNumberOfCells();

			String data[][] = new String[lastRow][lastColumn];
			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			int rowNum = 0;
			// while (rowIterator.hasNext()) {
			while (rowNum < lastRow) {
				Row row = rowIterator.next();

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				int colNum = 0;
				// while (cellIterator.hasNext()) {
				while (colNum < lastColumn) {
					Cell cell = cellIterator.next();

					String value = "";

					switch (cell.getCellTypeEnum()) {
					case NUMERIC:
						value = String.valueOf((int) cell.getNumericCellValue());
						break;
					default:
						value = cell.getStringCellValue();
						break;
					}

					data[rowNum][colNum++] = value.equals("") ? "No_Data" : value;
				}
				rowNum++;
			}
			file.close();
			workbook.close();
			return data;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void writeData(String destFile, ArrayList<String[]> dataSet) {
		Workbook workbook = new XSSFWorkbook();
		Sheet opSheet = workbook.createSheet("export");
		int rowIndex = -1;

		for (String[] dataRow : dataSet) {
			Row row = opSheet.createRow(++rowIndex);

			int cellIndex = -1;
			for (String cellValue : dataRow) {
				row.createCell(++cellIndex).setCellValue(cellValue);
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(destFile);
			workbook.write(fos);
			workbook.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
