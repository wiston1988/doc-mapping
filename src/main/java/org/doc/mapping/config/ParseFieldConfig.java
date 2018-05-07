package org.doc.mapping.config;

import org.doc.mapping.converter.parser.FieldParser;
import org.doc.mapping.exception.ConfigException;

public class ParseFieldConfig extends FieldConfig {

	private String groupName;
	private String parseFormatStr;
	private FieldParser fieldParser;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getParseFormatStr() {
		return parseFormatStr;
	}

	public void setParseFormatStr(String parseFormatStr) {
		this.parseFormatStr = parseFormatStr;
	}

	public FieldParser getFieldParser() {
		return fieldParser;
	}

	public void setFieldParser(FieldParser fieldParser) {
		this.fieldParser = fieldParser;
	}

	public void setParserName(String parserName) throws ConfigException {
		buildParser(parserName);
	}

	private void buildParser(String parserName) throws ConfigException {
		final String info = "\nbuildRenderer - ";

		if(parserName == null || parserName.trim().length() == 0) {
			return;
		}

		try{
			fieldParser = (FieldParser) Class.forName(parserName).newInstance();
		}catch ( ClassNotFoundException e ){
			throw new ConfigException(info + "ClassNotFoundException for csv parser: " + parserName,e);
		}catch ( InstantiationException e ){
			throw new ConfigException(info + "InstantiationException for csv parser: " + parserName,e);
		}catch ( IllegalAccessException e ){
			throw new ConfigException(info + "IllegalAccessException for csv parser: " + parserName,e);
		}catch ( Exception e ){
			throw new ConfigException(info + "Unknown Exception for csv parser: " + parserName,e);
		}
	}

}
