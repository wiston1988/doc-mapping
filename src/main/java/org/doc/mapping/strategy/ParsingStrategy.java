package org.doc.mapping.strategy;

import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.exception.MappingException;

import java.util.List;

public interface ParsingStrategy {
	Object parseRecord(String beanClass,String[] record,List<ParseFieldConfig> fieldConfigurations) throws MappingException;
}