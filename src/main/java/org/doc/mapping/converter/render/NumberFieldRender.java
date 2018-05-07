package org.doc.mapping.converter.render;

import org.doc.mapping.config.RenderFieldConfig;
import org.doc.mapping.config.FieldConfig;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.util.NumberFormatUtil;
import org.doc.mapping.util.introspector.IntrospectUtil;

import java.text.NumberFormat;

public class NumberFieldRender implements FieldRender {

	@Override
	public String getRenderedValue(Object pojoValue, Object pojo, RenderFieldConfig config) throws MappingException {
		if(pojoValue == null) {
			return "";
		}
		
		NumberFormat numberFormat = NumberFormatUtil.getNumberFormat(config.getBuildFormatStr());
		
		return numberFormat.format(pojoValue);
	}

	@Override
	public boolean canConvert(FieldConfig config) {
		return IntrospectUtil.isTypeMatch(Number.class, config.getType());
	}
}
