package org.doc.mapping.test.parsers;

import org.doc.mapping.config.FieldConfig;
import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.exception.FieldException;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.converter.parser.FieldParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This parser parses string to long.If convertion exception happens,it skip this exceptionand
 *  [NOTE]<bold>return value or throw exception</bold>
 *
 */
public class ToBigDecimalParserThrowE implements FieldParser {
	private static Log logger = LogFactory.getLog(ToBigDecimalParserThrowE.class);
	
	public Object getParsedValue(String fieldValue, Object pojo, ParseFieldConfig config) throws MappingException {
		if(config == null )
			throw new FieldException(config.getName(), "DocMappingField config is null");
		
		String trimedFileValue = fieldValue==null ?  null : fieldValue.trim();
		try{
			if(trimedFileValue == null || trimedFileValue.equals("")){
				return null;
			}else{
				if(config.getParseFormatStr() != null && !config.getParseFormatStr().equals("")){
					
					Pattern pattern = Pattern.compile(config.getParseFormatStr());
					Matcher matcher = pattern.matcher(trimedFileValue);
					boolean boolMatched= matcher.matches();
					//throw exception if not mathced? 
					if (boolMatched){
						return new BigDecimal(trimedFileValue);
					}else{
						throw new FieldException(config.getName(), "Value format is invalid");
					}					
				}
					
				return new BigDecimal(trimedFileValue);
			}
		}catch(NumberFormatException ex){
			if(logger.isInfoEnabled()){
				logger.info("When parse field "+config.getName()+" value to bigdecimal throw this exception:"+ex.getMessage());
			}
			throw new FieldException(config.getName(), ex.getMessage());
		}
	}

	public boolean canConvert(FieldConfig config) {
		return false;
	}

}
