package org.doc.mapping.io.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * A very simple Excel reader, which read the first sheet of xlsx file, line by line.
 * 
 */

public class XLSXReader extends ExcelReader {
	
	public XLSXReader(InputStream input) throws IOException {
		this(input, 0);
	}
	
	public XLSXReader(InputStream input, int skipLine) throws IOException {
		initialize(input, skipLine);
	}

	@Override
	void initialize(InputStream input, int skipLine) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(input);
		if(workbook != null)
			sheet = workbook.getSheetAt(0);

		if(sheet != null){
			currentRowNum = sheet.getFirstRowNum()+skipLine;
			lastRowNum = sheet.getLastRowNum();
		}
	}
}
