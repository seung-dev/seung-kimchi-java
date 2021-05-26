package seung.kimchi.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.utils.excel.SCell;
import seung.kimchi.java.utils.excel.SExcel;
import seung.kimchi.java.utils.excel.SRow;
import seung.kimchi.java.utils.excel.SSheet;

@Slf4j
public class SXlsx {

	public static String[][] read(byte[] file, int sheetNo, int rowNoMax, int cellNoMax) {
		
		String[][] data = null;
		
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(file));
			Sheet sheet = workbook.getSheetAt(sheetNo);
			Row row = null;
			Cell cell = null;
			
			int getPhysicalNumberOfRows = sheet.getPhysicalNumberOfRows();
			if(rowNoMax > getPhysicalNumberOfRows + 1) {
				rowNoMax = getPhysicalNumberOfRows + 1;
			}
			
			data = new String[getPhysicalNumberOfRows][cellNoMax];
			for(int rowNo = 0; rowNo <= rowNoMax; rowNo++) {
				row = sheet.getRow(rowNo);
				for(int cellNo = 0; cellNo <= cellNoMax; cellNo++) {
					cell = row.getCell(cellNo);
					data[rowNo][cellNo] = cellValue(cell);
				}
			}
			
		} catch (Exception e) {
			log.error("exception=", e);
		}
		
		return data;
	}
	
	public static SExcel read(byte[] file) {
		
		SExcel sExcel = SExcel.builder()
				.error_code("E999")
				.build()
				;
		
		XSSFWorkbook workbook = null;
		
		try {
			
			workbook = new XSSFWorkbook(new ByteArrayInputStream(file));
			sExcel.setNumberOfSheets(workbook.getNumberOfSheets());
			
			for(Sheet sheet : workbook) {
				
				sExcel.getSheets().add(readSheet(sheet));
				
			}// end of sheet
			
			sExcel.success();
			
		} catch (IOException e) {
			sExcel.exception(e);
			log.error("exception=", e);
		}
		
		return sExcel;
	}
	
	public static SSheet readSheet(Sheet sheet) {
		SSheet sSheet = SSheet.builder()
				.sheetName(sheet.getSheetName())
				.physicalNumberOfRows(sheet.getPhysicalNumberOfRows())
				.build()
				;
		for(Row row : sheet) {
			sSheet.getRows().add(readRow(row));
		}// end of row
		return sSheet;
	}
	
	public static SRow readRow(Row row) {
		SRow sRow = SRow.builder()
				.rowNum(row.getRowNum())
				.build()
				;
		for(Cell cell : row) {
			sRow.getCells().add(readCell(cell));
		}// end of cell
		return sRow;
	}
	
	public static SCell readCell(Cell cell) {
		SCell sCell = SCell.builder()
				.rowIndex(cell.getRowIndex())
				.columnIndex(cell.getColumnIndex())
				.build();
		sCell.setCellValue(cellValue(cell));
		return sCell;
	}
	
	public static CellStyle[][] cellStyle(Sheet sheet, int rowNoMax, int cellNoMax) {
		CellStyle[][] cellStyle = new CellStyle[rowNoMax + 1][cellNoMax + 1];
		for(int rowNo = 0; rowNo <= rowNoMax; rowNo++) {
			for(int cellNo = 0; cellNo <= cellNoMax; cellNo++) {
				cellStyle[rowNo][cellNo] = sheet.getRow(rowNo).getCell(cellNo).getCellStyle();
			}
		}
		return cellStyle;
	}
	
	public static short[] rowHeight(Sheet sheet, int rowNoMax) {
		short[] rowHeight = new short[rowNoMax + 1];
		for(int rowNo = 0; rowNo <= rowNoMax; rowNo++) {
			rowHeight[rowNo] = sheet.getRow(rowNo).getHeight();
		}
		return rowHeight;
	}
	
	public static int[] columnWidth(Sheet sheet, int cellNoMax) {
		int[] columnWidth = new int[cellNoMax + 1];
		for(int cellNo = 0; cellNo <= cellNoMax; cellNo++) {
			columnWidth[cellNo] = sheet.getColumnWidth(cellNo);
		}
		return columnWidth;
	}
	
	public static String cellValue(Cell cell) {
		
		String cellValue = null;
		if(CellType.STRING == cell.getCellTypeEnum()) {
			cellValue = cell.getStringCellValue();
		} else if(CellType.NUMERIC == cell.getCellTypeEnum()) {
			cellValue = cell.getStringCellValue();
		} else if(CellType.BOOLEAN == cell.getCellTypeEnum()) {
			if(DateUtil.isCellDateFormatted(cell)) {
				cellValue = "" + cell.getDateCellValue();
			} else {
				cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
			}
		} else if(CellType.FORMULA == cell.getCellTypeEnum()) {
			cellValue = cell.getCellFormula();
		} else if(CellType.BLANK == cell.getCellTypeEnum()) {
			cellValue = "";
		} else if(CellType._NONE == cell.getCellTypeEnum()) {
			cellValue = "";
		}
		
		return cellValue;
	}
	
}
