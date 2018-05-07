package org.doc.mapping.strategy;

import org.doc.mapping.config.RenderFieldConfig;
import org.doc.mapping.exception.MappingException;

import java.util.List;

public interface BuildingStrategy {
    String[] buildHeader(List<RenderFieldConfig> fieldConfigurations);
    String[] buildBody(Object bean, List<RenderFieldConfig> fieldConfigurations)throws MappingException;
}
