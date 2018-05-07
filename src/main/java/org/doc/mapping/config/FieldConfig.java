package org.doc.mapping.config;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class FieldConfig implements Serializable,Cloneable {

	protected String name;
	protected String columnName;
	protected boolean required;
	protected List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();
	protected Class type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public List<PropertyDescriptor> getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public void setPropertyDescriptors(List<PropertyDescriptor> propertyDescriptors) {
		this.propertyDescriptors = propertyDescriptors;
		this.type = propertyDescriptors.get(propertyDescriptors.size()-1).getPropertyType();
	}

}
