package org.doc.mapping.test.model;

import java.util.Date;

public class People {
	private String name;
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
