package org.doc.mapping.strategy.parser;

import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.exception.FieldException;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.exception.RecordException;
import org.doc.mapping.util.introspector.IntrospectUtil;

public class AbstractParsingStrategy{


	protected Object initBean(String recordClassName) throws RecordException{
		if(recordClassName == null){
			String msg = "Failed to initialize since ClassName is blank.";
			throw new RecordException(msg);
		}
		Object bean = null;
		try{
			bean = Class.forName(recordClassName).newInstance();
		}catch ( InstantiationException e ){
			throw new RecordException(e.getMessage(),e);
		}catch ( IllegalAccessException e ){
			throw new RecordException(e.getMessage(),e);
		}catch ( ClassNotFoundException e ){
			throw new RecordException(e.getMessage(),e);
		}
		if(bean == null){
			String msg = "Failed to initialize Bean Class: " + recordClassName;
			throw new RecordException(msg);
		}
		return bean;
	}


	protected void setFieldValue(String fieldValue, ParseFieldConfig parseFieldConfig, Object bean) throws MappingException{
		if(parseFieldConfig == null){
			throw new FieldException("fieldConfiguration is null when parse field value:"+fieldValue);
		}
		if((fieldValue == null || fieldValue.trim().length() == 0) && parseFieldConfig.isRequired()){
			String errMsg = "Record Field: [" + parseFieldConfig.getName() + "] is not defined in imported File";
			throw new FieldException(parseFieldConfig.getName(), errMsg);
		}
		if(fieldValue != null)//FIXME, shall we trim value here, or let unTrimed value go to parser
		{
			fieldValue = fieldValue.trim();
		}
		/***Convert Data***/
		Object covertedValue = null;

		try{
			covertedValue = parseFieldConfig.getFieldParser().getParsedValue(fieldValue, bean, parseFieldConfig);
		}catch(Exception e){
			throw new FieldException(parseFieldConfig.getName(),fieldValue, e.getMessage(), e);
		}
		/***Set Value to mapping field***/
		try{
			IntrospectUtil.setPropertyValue(parseFieldConfig.getPropertyDescriptors(), bean, covertedValue);
		}catch(Exception e){
			throw new FieldException(parseFieldConfig.getName(),fieldValue, e.getMessage(), e);
		}
	}



}
