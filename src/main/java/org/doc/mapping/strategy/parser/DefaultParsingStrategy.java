package org.doc.mapping.strategy.parser;

import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.strategy.ParsingStrategy;

import java.util.List;

public class DefaultParsingStrategy extends AbstractParsingStrategy implements ParsingStrategy {
	
	@Override
	public Object parseRecord(String beanClass,String[] record,List<ParseFieldConfig> fieldConfigurations) throws MappingException{

		if(record == null) {
			return null;
		}
		Object bean = initBean(beanClass);
		if(fieldConfigurations == null) {
			return bean;
		}

		int size = fieldConfigurations.size();
		int recordLength = record.length;

		for(int i=0;i<size;i++){
			ParseFieldConfig fieldConfiguration =  fieldConfigurations.get(i);
			String fieldValue = null;
			if(i < recordLength) {
				fieldValue = record[i];
			}
			setFieldValue(fieldValue, fieldConfiguration, bean);
		}
		return bean;
	}


}
