package org.doc.mapping.converter.render;

import org.doc.mapping.config.RenderFieldConfig;
import org.doc.mapping.config.FieldConfig;
import org.doc.mapping.exception.FieldException;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.util.BaseTypeConverter;

public class DefaultFieldRender implements FieldRender {

	@Override
	public String getRenderedValue(Object pojoValue, Object pojo, RenderFieldConfig config) throws MappingException {
		try{
			return (String)BaseTypeConverter.getInstance().convertIfNecessary(pojoValue, String.class);
		}catch(Exception e){
			throw new FieldException(config.getName(), "Failed to Convert Value ["+pojoValue+"] to a String.", e);
		}
	}

	@Override
	public boolean canConvert(FieldConfig config) {
		return true;
	}
}
