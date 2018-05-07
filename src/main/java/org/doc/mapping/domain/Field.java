package org.doc.mapping.domain;

import java.io.Serializable;
import java.util.List;

public class Field implements Serializable,Cloneable {

	private String name;
	private String columnName;
	private List<String> multiColumns;
	private boolean required;
	private String converter;
	private String formatStr;

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

	public List<String> getMultiColumns() {
		return multiColumns;
	}

	public void setMultiColumns(List<String> multiColumns) {
		this.multiColumns = multiColumns;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getConverter() {
		return converter;
	}

	public void setConverter(String converter) {
		this.converter = converter;
	}

	public String getFormatStr() {
		return formatStr;
	}

	public void setFormatStr(String formatStr) {
		this.formatStr = formatStr;
	}
}
