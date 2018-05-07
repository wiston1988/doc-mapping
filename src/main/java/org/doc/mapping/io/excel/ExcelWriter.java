package org.doc.mapping.io.excel;

import org.doc.mapping.io.DocumentExcelWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public abstract class ExcelWriter implements DocumentExcelWriter {
	
	protected OutputStream output;
	protected Workbook workBook;
	protected Sheet workSheet;
	private boolean isFlushed = false;
	protected int nextRow = 0;
	protected int currRow = 0;
	
	/**
	 * It genereates a blank excel file,so the data can write one by one.
	 */
	protected abstract void writeLine( Object[] line );
	protected abstract void writeLine( String[] line, int[] cellTypes);

//	public ExcelWriter ( OutputStream output){
//		this.output = output;
//	}
	
	/* Because the excel file can not be ouput row by row.
	 * when do fluash , write the whole excel. 
	 * And ONE EXCEL FILE CAN ONLY BE OUPUTTED ONE TIME.
	 * User can call flush to output file or call close.
	 */
	@Override
	public void flush() throws IOException {
		/*If the excel file not flushed, do flushed and set the flag to be true.So that 
		 * the excel file can only be outputted once.
			*/
		if ( ! isFlushed){
			workBook.write(output);
			isFlushed = true;
		}
		// else do nothing.
	}

	@Override
	public void writeAll(List<Object []> allLines) throws IOException {
		// Write row by row
		for(Object[] line : (List<Object []>)allLines ){
			writeNext(line);			
		}
	}

	@Override
	public void writeNext(Object[] nextLine) throws IOException {
		writeLine( nextLine);
		currRow = nextRow;
		nextRow ++;
	}
	@Override
	public void writeNext(String[] nextLine,int[] cellTypes)throws IOException {
		writeLine(nextLine,cellTypes);
		currRow = nextRow;
		nextRow ++;
	}
	/**
	 * Default implementation for writing line.
	 * @param line
	 */
	protected void defaultWriteLine(Object[] line){
		Row excelRow = workSheet.createRow(nextRow);
		//Object obj = null;
		/*
		 * Support String,Long,Boolean,Double.
		 * Will support Date
		 * The type not supported will set as blank
		 */
		for (int cellId =0 ; cellId < line.length ; cellId ++  ){
			Cell excelCell = excelRow.createCell(cellId);
			if( line[cellId]!= null && line[cellId] instanceof String){
				excelCell.setCellValue((String)line[cellId]);
			}else if (line[cellId]!= null && line[cellId] instanceof Long){
				excelCell.setCellValue((Long)line[cellId]);
			}else if (line[cellId]!= null && line[cellId] instanceof Boolean){
				excelCell.setCellValue((Boolean)line[cellId]);
			}else if (line[cellId]!= null && line[cellId] instanceof Double){
				excelCell.setCellValue((Double)line[cellId]);
			}else{
				excelCell.setCellValue("");
			}
			
		}
	}
	/**
	 * Default implementation for writing line.
	 * @param line
	 */
	protected void defaultWriteLine(String[] line, int[] cellTypes){
		if(line==null){
			return;
		}
		Row excelRow = workSheet.createRow(nextRow);
		//Object obj = null;
		/*
		 * Support String,Long,Boolean,Double.
		 * Will support Date
		 * The type not supported will set as blank
		 */
		for (int cellId =0 ; cellId < line.length ; cellId ++  ){
			Cell excelCell = excelRow.createCell(cellId);
			excelCell.setCellType(cellTypes[cellId]);
			if(cellTypes[cellId] == Cell.CELL_TYPE_STRING
					|| cellTypes[cellId] == Cell.CELL_TYPE_BLANK
					|| cellTypes[cellId] == Cell.CELL_TYPE_FORMULA
					|| cellTypes[cellId] == Cell.CELL_TYPE_ERROR){
				excelCell.setCellValue(line[cellId]);
			}else if (line[cellId]!= null && cellTypes[cellId] == Cell.CELL_TYPE_NUMERIC){
				excelCell.setCellValue(Double.valueOf(line[cellId]));
			}else if (line[cellId]!= null && cellTypes[cellId] == Cell.CELL_TYPE_BOOLEAN){
				excelCell.setCellValue(Boolean.valueOf(line[cellId]));
			}
		}
	}
	@Override
	public void close() throws IOException {
		//If flushed , do not flush again.
		if ( ! isFlushed ){
			flush();			
		}
		output.close();
	}

}
