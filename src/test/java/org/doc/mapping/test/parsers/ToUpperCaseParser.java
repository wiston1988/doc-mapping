package org.doc.mapping.test.parsers;

import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.config.FieldConfig;
import org.doc.mapping.exception.FieldException;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.converter.parser.FieldParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ToUpperCaseParser implements FieldParser {
	private static Log logger = LogFactory.getLog(ToUpperCaseParser.class);
	public boolean canConvert(FieldConfig config) {
		return config.getType() == String.class;
	}

	public Object getParsedValue(String fieldValue, Object pojo, ParseFieldConfig config) throws MappingException {
		if(config == null )
			throw new FieldException(config.getName(), "DocMappingField config is null");
		
		String retVal = fieldValue == null ? null : fieldValue.toUpperCase().trim();
		String formatStr = config.getParseFormatStr()==null ? null : config.getParseFormatStr().trim();
		int subLen = 0;
				
		if ( formatStr != null && !formatStr.equals("") ){
			try{
				subLen = Integer.valueOf(formatStr).intValue();
			}catch(Exception ex){
				if(logger.isInfoEnabled()){
					logger.info("Configuration for field "+config.getName() +"->formatStr is not correct.");
				}
				subLen = 0;
			}
		}else{//If no config, just return the fieldvalue
			return retVal;			
		}
		
		try{						
			if(retVal == null || retVal.equals("")){
				return null;
			}else{				
				if (subLen == 0 ){
					return retVal;
				}
				if(retVal.length() > subLen ) {
					throw new FieldException(config.getName(), "DocMappingField value is too long");
				}else {
					return retVal;
				}
			}
		}catch(Exception ex){
			if(logger.isInfoEnabled()){
				logger.info("When parse field "+config.getName()+" value to String skip this exception:"+ex.getMessage());
			}
			throw new FieldException(config.getName(), ex.getMessage(),ex);
		}
	}
}
