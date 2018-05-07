package org.doc.mapping.converter.parser;

import org.doc.mapping.config.FieldConfig;
import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.exception.FieldException;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.util.BaseTypeConverter;

public class DefaultFieldParser implements FieldParser {

	@Override
	public Object getParsedValue(String fieldValue, Object pojo, ParseFieldConfig config) throws MappingException {
		try{
			return BaseTypeConverter.getInstance().convertIfNecessary(fieldValue, config.getType());
		}catch(Exception e){
			throw new FieldException(config.getName(), "Failed to Convert Value ["+fieldValue+"] for class ["+config.getType()+"]", e);
		}
	}

	@Override
	public boolean canConvert(FieldConfig config) {
		return true;
	}
}
