package org.doc.mapping.test;


import org.doc.mapping.DocMappingBuilder;
import org.doc.mapping.io.DocumentWriter;
import org.doc.mapping.io.csv.CSVWriter;
import org.doc.mapping.io.excel.XLSXWriter;
import org.doc.mapping.test.config.PeopleExport;
import org.doc.mapping.test.model.People;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnotationBuildMain {


	private void testCSV(DocMappingBuilder docMappingBuilder){
		File file=new File(System.getProperty("user.dir"),"src/test/resources/export/testExport.csv");
		try{
			/***1. init CSVWriter***/
			StringWriter contentWriter = new StringWriter();
			DocumentWriter<String[]> docWriter = new CSVWriter(contentWriter);
			/***2. fill Content***/
			List<PeopleExport> peopleList = new ArrayList();
			PeopleExport people = new PeopleExport();
			for(int i=0; i<10; i++){
				people.setName("陈慧");
				people.setBirthday(new Date());
				peopleList.add(people);
			}
			docMappingBuilder.build(docWriter,peopleList);
			docWriter.flush(); 
			FileOutputStream out = new FileOutputStream(file);
			out.write(contentWriter.getBuffer().toString().getBytes("gbk"));
			out.flush();
			out.close();
			System.out.println("write csv success");
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

	private void testXLSX(DocMappingBuilder docMappingBuilder){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		File file=new File(System.getProperty("user.dir"),"src/test/resources/export/testExport.xlsx");
		try{

			/***1. init CSVWriter***/
			DocumentWriter<Object[]> docWriter = new XLSXWriter(output,"testSheet");
			/***2. fill Content***/
			List<PeopleExport> peopleList = new ArrayList();
			PeopleExport people = new PeopleExport();
			for(int i=0; i<10; i++){
				people.setName("陈慧");
				people.setBirthday(new Date());
				peopleList.add(people);
			}
			docMappingBuilder.build(docWriter,peopleList);
			docWriter.flush();
			docWriter.close();
			FileOutputStream out = new FileOutputStream(file);
			output.writeTo(out);
			System.out.println("write xlsx success");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.doc.mapping.test");
		DocMappingBuilder docMappingBuilder= (DocMappingBuilder)context.getBean("peopleExport");
		System.out.println("================测试导出csv文件==============");
		new AnnotationBuildMain().testCSV(docMappingBuilder);
		System.out.println("================测试导出xlsx文件==============");
		new AnnotationBuildMain().testXLSX(docMappingBuilder);
	}

}
