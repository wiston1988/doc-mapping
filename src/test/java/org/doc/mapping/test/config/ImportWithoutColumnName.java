package org.doc.mapping.test.config;

import org.doc.mapping.spring.annotation.DocMappingField;
import org.doc.mapping.spring.annotation.DocMappingParser;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Hui
 * @since 2018/5/7
 */

@Configuration
@DocMappingParser(id = "importWithoutColumnName",defaultDateFormat = "yyyyMMdd",defaultNumberFormat = "5",ignoreError = false,headerSupport = false)
public class ImportWithoutColumnName {
    private Student student;
    @DocMappingField(columnName = "name")
    private String name;
    @DocMappingField(columnName = "age")
    private int age;

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
        @DocMappingField(columnName = "student.studentName")
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