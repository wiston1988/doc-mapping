package org.doc.mapping.io.excel;

import org.doc.mapping.io.DocumentReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

abstract class ExcelReader implements DocumentReader<String[]> {

	protected Sheet sheet;

	protected int lastRowNum;//row Num is start from zero
	protected int currentRowNum;

	private static final String EMPTY_STR = "";

	abstract void initialize(InputStream input, int skipLine)throws IOException;

	@Override
	public List<String[]> readAll() throws IOException {
		List<String[]> allElements = new ArrayList<String[]>();

		while (hasNext()) {
			String[] nextLine = readNext();

			if (nextLine != null)
				allElements.add(nextLine);
		}

		return allElements;
	}

	@Override
	public String[] readNext() throws IOException {
		if(hasNext()){
			Row row = sheet.getRow(currentRowNum);
			currentRowNum += 1;
			return getValueFromRow(row);
		}else{
			return null;
		}
	}

	protected String[] getValueFromRow(Row row){
		if(row == null) {
			return null;
		}

		int cellCount = row.getLastCellNum();
		String[] values = new String[cellCount];

		for(int i=0; i<cellCount; i++){
			Cell cell = row.getCell(i);
			if(cell == null) {
				continue;
			}

			values[i] = getCellContent(cell);
		}

		return values;
	}

	protected String getCellContent(Cell cell){
		if(cell == null) {
			return null;
		}

		int cellType = cell.getCellType();
		String value = null;

		switch(cellType){
			case(Cell.CELL_TYPE_STRING):
				value = cell.getStringCellValue();
				break;
			case(Cell.CELL_TYPE_BLANK):
				value = EMPTY_STR;
				break;
			case(Cell.CELL_TYPE_BOOLEAN):
				value = String.valueOf(cell.getBooleanCellValue());
				break;
			case(Cell.CELL_TYPE_NUMERIC):
				Double d = cell.getNumericCellValue();
				if(d.doubleValue() == d.longValue()) {
					value = String.valueOf(d.longValue());
				} else {
					value = String.valueOf(d.doubleValue());
				}
					
				break;
			default:
				value = cell.toString();
		}

		return value;
	}

	@Override
	public boolean hasNext() {
		return currentRowNum <= lastRowNum && sheet != null;
	}

	@Override
	public void close() throws IOException {}
}
