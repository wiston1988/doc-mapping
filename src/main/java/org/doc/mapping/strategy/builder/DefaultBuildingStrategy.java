package org.doc.mapping.strategy.builder;

import org.doc.mapping.config.RenderFieldConfig;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.strategy.BuildingStrategy;

import java.util.List;

public class DefaultBuildingStrategy extends AbstractBuildingStrategy implements BuildingStrategy {

	@Override
	public String[] buildHeader(List<RenderFieldConfig> fieldConfigurations){
		if(fieldConfigurations == null) {
			return new String[0];
		}
		int size = fieldConfigurations.size();
		String[] header = new String[size];
		for(int i=0;i<size;i++){
			RenderFieldConfig fieldConfiguration = fieldConfigurations.get(i);
			header[i] = buildHeaderColumn(fieldConfiguration);
		}
		return header;
	}

	@Override
	public String[] buildBody(Object bean, List<RenderFieldConfig> fieldConfigurations)throws MappingException{
		if(fieldConfigurations == null) {
			return new String[0];
		}
		String[] record = new String[fieldConfigurations.size()];
		int size = fieldConfigurations.size();
		for(int i=0;i<size;i++){
			record[i] = buildBodyColumn(bean,fieldConfigurations.get(i));
		}
		return record;
	}

}
