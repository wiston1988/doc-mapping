package org.doc.mapping.converter.render;

import org.doc.mapping.config.RenderFieldConfig;
import org.doc.mapping.converter.FieldConverter;
import org.doc.mapping.exception.MappingException;

public interface FieldRender extends FieldConverter {
	String getRenderedValue(Object pojoValue, Object pojo, RenderFieldConfig config)throws MappingException;

}