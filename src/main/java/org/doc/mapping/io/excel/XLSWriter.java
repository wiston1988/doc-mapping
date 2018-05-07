package org.doc.mapping.io.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.OutputStream;

/**
 * To write xls( Excel 2003) file
 *
 */
public class XLSWriter extends ExcelWriter {

	public XLSWriter ( OutputStream output){
		this.workBook = new HSSFWorkbook();	
		this.output = output;
	}
	public XLSWriter ( OutputStream output ,String sheetName){
		this.workBook = new HSSFWorkbook();	
		this.output = output;
		createWorkSheet(sheetName);
	}
	
	public void createWorkSheet(String sheetName){
		if(sheetName == null || "".equals(sheetName)){
			//use poi default sheet name
			this.workSheet = this.workBook.createSheet(); 		
		}else{
			this.workSheet = this.workBook.createSheet(sheetName); 			
		}
		this.nextRow=0;
		this.currRow=0;
	}
	@Override
	protected void writeLine(Object[] line) {
		super.defaultWriteLine(line);
	}
	@Override
	protected void writeLine(String[] line, int[] cellTypes) {
		super.defaultWriteLine(line,cellTypes);
	}
}
