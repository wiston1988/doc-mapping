package org.doc.mapping.test.config;

import org.doc.mapping.spring.annotation.DocMappingField;
import org.doc.mapping.spring.annotation.DocMappingParser;
import org.springframework.context.annotation.Configuration;

@Configuration
@DocMappingParser(id = "importTextFile",defaultDateFormat = "yyyyMMdd",defaultNumberFormat = "5",ignoreError = false)
public class ImportTextFile {

	@DocMappingField(columnName = "20,40")
	private String name;
	@DocMappingField(columnName = "40,43")
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
	private class Student {
		@DocMappingField(columnName = "0,20")
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
