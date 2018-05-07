package org.doc.mapping.config;

import org.doc.mapping.converter.render.FieldRender;
import org.doc.mapping.exception.ConfigException;

public class RenderFieldConfig extends FieldConfig {


	private String buildFormatStr;
	private FieldRender fieldRender;

	public String getBuildFormatStr() {
		return buildFormatStr;
	}

	public void setBuildFormatStr(String buildFormatStr) {
		this.buildFormatStr = buildFormatStr;
	}

	public FieldRender getFieldRender() {
		return fieldRender;
	}

	public void setFieldRender(FieldRender fieldRender) {
		this.fieldRender = fieldRender;
	}


	public void setRendererName(String rendererName) throws ConfigException {
		buildRenderer(rendererName);
	}

	private void buildRenderer(String rendererName) throws ConfigException {
		final String info = "\nbuildRenderer - ";

		if(rendererName == null || rendererName.trim().length() == 0) {
			return;
		}

		try{
			fieldRender = (FieldRender) Class.forName(rendererName).newInstance();
		}catch ( ClassNotFoundException e ){
			throw new ConfigException(info + "ClassNotFoundException for csv renderer: " + rendererName,e);
		}catch ( InstantiationException e ){
			throw new ConfigException(info + "InstantiationException for csv renderer: " + rendererName,e);
		}catch ( IllegalAccessException e ){
			throw new ConfigException(info + "IllegalAccessException for csv renderer: " + rendererName,e);
		}catch ( Exception e ){
			throw new ConfigException(info + "Unknown Exception for csv renderer: " + rendererName,e);
		}
	}

}
