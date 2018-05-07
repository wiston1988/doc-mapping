package org.doc.mapping.test.config;

import org.doc.mapping.spring.annotation.DocMappingField;
import org.doc.mapping.spring.annotation.DocMappingParser;
import org.springframework.context.annotation.Configuration;

@Configuration
@DocMappingParser(id = "baseImport",defaultDateFormat = "MM/dd/yyyy",defaultNumberFormat = "12",ignoreError = false,fieldParsers = {"org.doc.mapping.test.parsers.IntegerParser"})
public class BaseImport {

	@DocMappingField(columnName = "name",converter = "org.doc.mapping.test.parsers.ToUpperCaseParser")
	private String name;
	@DocMappingField(columnName = "age")
	private int age;

	private Student student;
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	@Override
	public String toString() {
		return "Teacher [name=" + name + ", age=" + age + ", student="
				+ student + "]";
	}
	public class Student {
		@DocMappingField(multiColumns = "student name,student name1,student name2")
		private String studentName;

		public String getStudentName() {
			return studentName;
		}

		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}

		@Override
		public String toString() {
			return "Student [studentName=" + studentName + "]";
		}

	}
}
