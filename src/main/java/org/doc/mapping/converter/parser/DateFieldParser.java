package org.doc.mapping.converter.parser;

import org.doc.mapping.config.FieldConfig;
import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.exception.FieldException;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.util.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFieldParser implements FieldParser {

	@Override
	public Object getParsedValue(String fieldValue, Object pojo, ParseFieldConfig config) throws MappingException {
		if(fieldValue == null || fieldValue.trim().length() == 0) {
			return null;
		}
		
		SimpleDateFormat dateFormat = DateFormat.getFormatter(config.getParseFormatStr());
		
		try{
			return dateFormat.parse(fieldValue);
		}catch(Exception e){
			throw new FieldException(config.getName(), "Failed to Convert Value ["+fieldValue+"] for class [Date]", e);
		}
	}

	@Override
	public boolean canConvert(FieldConfig config) {
		return config.getType() == Date.class;
	}

}
