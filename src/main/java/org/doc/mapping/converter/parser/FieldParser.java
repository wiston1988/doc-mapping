package org.doc.mapping.converter.parser;

import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.converter.FieldConverter;
import org.doc.mapping.exception.MappingException;

public interface FieldParser extends FieldConverter {
	Object getParsedValue(String fieldValue, Object pojo, ParseFieldConfig config)throws MappingException;
}