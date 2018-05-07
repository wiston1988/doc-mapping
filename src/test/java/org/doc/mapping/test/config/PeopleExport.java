package org.doc.mapping.test.config;

import org.doc.mapping.spring.annotation.DocMappingBuilder;
import org.doc.mapping.spring.annotation.DocMappingField;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@DocMappingBuilder(id="peopleExport",defaultDateFormat = "yyyy-MM-dd",defaultNumberFormat = "12",ignoreError = false)
public class PeopleExport {
	@DocMappingField(columnName = "名字")
	private String name;
	@DocMappingField(columnName = "生日",converter = "org.doc.mapping.converter.render.DateFieldRender")
	private Date birthday;

	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date i) {
		this.birthday = i;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "People [name=" + name + ", birthday=" + birthday + "]";
	}
	
}
