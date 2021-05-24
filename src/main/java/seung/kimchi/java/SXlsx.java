package seung.kimchi.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
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
		if(CellType._NONE == cell.getCellTypeEnum()) {
			sCell.setCellValue(null);
		} else if(CellType.BLANK == cell.getCellTypeEnum()) {
			sCell.setCellValue("" + cell.getBooleanCellValue());
		} else if(CellType.BOOLEAN == cell.getCellTypeEnum()) {
			sCell.setCellValue("" + cell.getBooleanCellValue());
		} else if(CellType.STRING == cell.getCellTypeEnum()) {
			sCell.setCellValue(cell.getStringCellValue());
		} else if(CellType.NUMERIC == cell.getCellTypeEnum()) {
			if(DateUtil.isCellDateFormatted(cell)) {
				sCell.setCellValue("" + cell.getDateCellValue());
			} else {
				sCell.setCellValue(NumberToTextConverter.toText(cell.getNumericCellValue()));
			}
		} else if(CellType.FORMULA == cell.getCellTypeEnum()) {
			sCell.setCellValue(cell.getCellFormula());
		} else {
			sCell.setCellValue("Check cell type.");
		}
		return sCell;
	}
}
