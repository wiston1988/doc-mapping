package org.doc.mapping.test;


import org.doc.mapping.DocMappingParser;
import org.doc.mapping.domain.DocumentParseResult;
import org.doc.mapping.exception.RecordException;
import org.doc.mapping.io.DocumentReader;
import org.doc.mapping.io.csv.CSVReader;
import org.doc.mapping.io.excel.XLSReader;
import org.doc.mapping.io.excel.XLSXReader;
import org.doc.mapping.io.jdkAdapter.DocumentBufferReader;
import org.doc.mapping.test.config.BaseImport;
import org.doc.mapping.test.config.ImportTextFile;
import org.doc.mapping.test.config.ImportWithoutColumnName;
import org.doc.mapping.test.model.Teacher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class AnnotationParseMain {

	/**
	 * @param args
	 */
	public static final String XLSX_FILE = "import/testImport.xlsx";
	public static final String XLS_FILE = "import/testImport.xls";
	public static final String CSV_FILE = "import/testImport.csv";
	public static final String TXT_FILE = "import/testImport.txt";
	public static final String NO_HEADER_FILE = "import/testImportWithoutHeader.csv";
	public static final String ADVANCED_IMPORT_FILE = "import/advancedImport.xlsx";

	private static final String NO_HEADER_TAG="[ImportWithoutBindingColumn]";
	private static final String COMMON_TAG = "[TestImport]";
	private static final String ADVANCED_IMPORT_TAG = "[AdvancedImport]";
	private static final String IMPORT_TXT_TAG = "[ImportTextFile]";

	private void testParseCSV(DocMappingParser docMappingPaser){
		try{

			/****2. Adapter IO**/
			InputStream testCsvStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CSV_FILE);
			DocumentReader<String[]> csvReader =  new CSVReader(new BufferedReader(new InputStreamReader(testCsvStream)));

			/***3. parse and getBeans***/
			DocumentParseResult result = docMappingPaser.parseDocReaderStrArr(csvReader);

			List resultList = result.getRecords();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				BaseImport tc = (BaseImport)resultList.get(i);
				System.out.println(tc.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void testParseXLS(DocMappingParser docMappingPaser){
		try{

			/****2. Adapter IO**/
			InputStream testExcelStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(XLS_FILE);
			DocumentReader<String[]> excelReader =  new XLSReader(testExcelStream);

			/***4. parse and getBeans***/
			DocumentParseResult result = docMappingPaser.parseDocReaderStrArr(excelReader);
			List resultList = result.getRecords();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				System.out.println(resultList.get(i).toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void testParseXLSX(DocMappingParser docMappingPaser){
		try{

			/****2. Adapter IO**/
			InputStream testExcelStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(XLSX_FILE);
			DocumentReader<String[]> excelReader =  new XLSXReader(testExcelStream);

			/***4. parse and getBeans***/
			DocumentParseResult result = docMappingPaser.parseDocReaderStrArr(excelReader);
			List resultList = result.getRecords();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				System.out.println(resultList.get(i).toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void testAdvancedParser(DocMappingParser docMappingPaser){
		try{

			/****2. Adapter IO**/
			InputStream testExcelStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ADVANCED_IMPORT_FILE);
			DocumentReader<String[]> excelReader =  new XLSXReader(testExcelStream);

			/***4. parse and getBeans***/
			DocumentParseResult result = docMappingPaser.parseDocReaderStrArr(excelReader);
			List resultList = result.getRecords();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				System.out.println(resultList.get(i).toString());
			}

		}catch(RecordException e){
			System.err.println("record:"+e.getIdentifier()+" reason:"+e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void testParseNoHeaderFile(DocMappingParser docMappingPaser){
		try{

			/****2. Adapter IO**/
			InputStream testCsvStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(NO_HEADER_FILE);
			DocumentReader<String[]> csvReader =  new CSVReader(new BufferedReader(new InputStreamReader(testCsvStream)));

			/***4. parse and getBeans***/
			DocumentParseResult result = docMappingPaser.parseDocReaderStrArr(csvReader);

			List resultList = result.getRecords();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				ImportWithoutColumnName tc = (ImportWithoutColumnName)resultList.get(i);
				System.out.println(tc.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void testParseTXT(DocMappingParser docMappingPaser){
		try{

			/****2. Adapter IO**/
			InputStream testCsvStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(TXT_FILE);
			DocumentReader<String> txtReader =  new DocumentBufferReader(new BufferedReader(new InputStreamReader(testCsvStream)));

			/***4. parse and getBeans***/
			DocumentParseResult result = docMappingPaser.parseDocReaderStr(txtReader);

			List resultList = result.getRecords();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				ImportTextFile tc = (ImportTextFile)resultList.get(i);
				System.out.println(tc.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.doc.mapping.test");
		DocMappingParser importWithoutColumnName= (DocMappingParser)context.getBean("importWithoutColumnName");
		DocMappingParser baseImport= (DocMappingParser)context.getBean("baseImport");
		DocMappingParser advancedImport= (DocMappingParser)context.getBean("advancedImport");
		DocMappingParser importTextFile= (DocMappingParser)context.getBean("importTextFile");
		//测试没有header的文件解析
		System.out.println("================测试没有header的文件解析==============");
		new AnnotationParseMain().testParseNoHeaderFile(importWithoutColumnName);
		//测试包含header的文件解析
		System.out.println("================测试包含header的CSV文件解析==============");
		new AnnotationParseMain().testParseCSV(baseImport);
		System.out.println("================测试包含header的XLSX文件解析==============");
		new AnnotationParseMain().testParseXLSX(baseImport);
		System.out.println("================测试包含header的XLS文件解析==============");
		new AnnotationParseMain().testParseXLS(baseImport);
		System.out.println("================测试包含各个类型字段的XLSX文件解析==============");
		new AnnotationParseMain().testAdvancedParser(advancedImport);
		//测试列名表示成字符位置的文件
		System.out.println("================测试列名表示成字符位置的TXT文件解析==============");
		new AnnotationParseMain().testParseTXT(importTextFile);
	}

}
