package org.doc.mapping.strategy.builder;

import org.doc.mapping.config.RenderFieldConfig;
import org.doc.mapping.exception.FieldException;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.util.introspector.IntrospectUtil;


public abstract class AbstractBuildingStrategy {

	protected static final String EMPTY_STR = "";

	protected String buildHeaderColumn(RenderFieldConfig fieldConfiguration){
		String fieldColumn = fieldConfiguration.getColumnName();
		return fieldColumn == null ? EMPTY_STR : fieldColumn;
	}

	protected String buildBodyColumn(Object bean,RenderFieldConfig renderFieldConfig) throws MappingException {
		Object pojoValue = null;

		try{
			pojoValue = IntrospectUtil.getPropertyValue(renderFieldConfig.getPropertyDescriptors(), bean);
		}catch(Exception e){
			throw new FieldException(renderFieldConfig.getName(), e.getMessage(), e);
		}

		String fieldValue = null;

		try{
			fieldValue = renderFieldConfig.getFieldRender().getRenderedValue(pojoValue, bean, renderFieldConfig);
		}catch(Exception e){
			throw new FieldException(renderFieldConfig.getName(),fieldValue, e.getMessage(), e);
		}

		if(fieldValue == null || fieldValue.trim().length() == 0){
			if(renderFieldConfig.isRequired()){
				String errMsg = "Record Field: [" + renderFieldConfig.getName() + "] is missing or not valid";
				throw new FieldException(renderFieldConfig.getName(), errMsg);
			}else{
				return "";
			}
		}

		return fieldValue;
	}
}
