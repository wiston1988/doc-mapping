package org.doc.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.doc.mapping.config.RenderFieldConfig;
import org.doc.mapping.converter.render.DateFieldRender;
import org.doc.mapping.converter.render.DefaultFieldRender;
import org.doc.mapping.converter.render.FieldRender;
import org.doc.mapping.converter.render.NumberFieldRender;
import org.doc.mapping.domain.Field;
import org.doc.mapping.exception.ConfigException;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.exception.RecordException;
import org.doc.mapping.io.DocumentWriter;
import org.doc.mapping.strategy.BuildingStrategy;
import org.doc.mapping.strategy.builder.DefaultBuildingStrategy;
import org.doc.mapping.util.introspector.IntrospectUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Hui
 * @since 2018/4/18
 */
public class DefaultDocMappingBuilder extends AbstractDocMapping implements DocMappingBuilder<String[]> {

    private final static Log LOG = LogFactory.getLog(DefaultDocMappingBuilder.class);

    private String buildStrategyClass;
    private BuildingStrategy buildingStrategy;
    private List<FieldRender> fieldRenders;
    private final List<RenderFieldConfig> renderFieldConfigs = new ArrayList<>();

    public DefaultDocMappingBuilder(){
        super();
    }
    @Override
    public void init() throws ConfigException {
        super.init();
        try{
            if(buildStrategyClass==null || buildStrategyClass.trim().length()==0){
                buildingStrategy = new DefaultBuildingStrategy();
            }else{
                buildingStrategy = (BuildingStrategy) Class.forName(buildStrategyClass).newInstance();
            }
            if(fieldRenders == null){
                fieldRenders = new ArrayList<>();
            }
            fieldRenders.add(0,new DateFieldRender());
            fieldRenders.add(0,new NumberFieldRender());
            fieldRenders.add(0,new DefaultFieldRender());

            for(Field field:fields){
                renderFieldConfigs.add(constructRenderFieldConfig(field));
            }
        }catch ( InstantiationException e ){
            throw new ConfigException("InstantiationException for Builder: " + buildStrategyClass,e);
        }catch ( IllegalAccessException e ){
            throw new ConfigException("IllegalAccessException for Builder: " + buildStrategyClass,e);
        }catch ( ClassNotFoundException e ){
            throw new ConfigException("ClassNotFoundException for Builder: " + buildStrategyClass,e);
        }
    }

    private RenderFieldConfig constructRenderFieldConfig(Field field) throws ConfigException {
        RenderFieldConfig renderFieldConfig = new RenderFieldConfig();
        renderFieldConfig.setBuildFormatStr(field.getFormatStr());
        renderFieldConfig.setRendererName(field.getConverter());
        renderFieldConfig.setRequired(field.isRequired());
        renderFieldConfig.setName(field.getName());
        renderFieldConfig.setColumnName(field.getColumnName());

        if(field.getName() != null)//Get Property Descriptor if there is name specified
        {
            renderFieldConfig.setPropertyDescriptors(IntrospectUtil.introspectReadbleProperty(beanClass, field.getName()));
        }

        /***Set default build format***/
        if (renderFieldConfig.getBuildFormatStr() == null || renderFieldConfig.getBuildFormatStr().trim().length() == 0) {
            String defaultBuildFormat = getDefaultFormatStr(renderFieldConfig.getType());
            if (defaultBuildFormat != null && defaultBuildFormat.trim().length() > 0) {
                renderFieldConfig.setBuildFormatStr(defaultBuildFormat);
            }
        }

        /***If Paser or Renderer is nonexistent, get from mapping configuration***/
        if (renderFieldConfig.getFieldRender() == null) {
            int length = fieldRenders.size();
            int idx = length - 1;

            while (idx >= 0) {
                FieldRender fieldRender = fieldRenders.get(idx);
                if (fieldRender.canConvert(renderFieldConfig)) {
                    renderFieldConfig.setFieldRender(fieldRender);
                    break;
                }
                idx--;
            }
        }
        return renderFieldConfig;

    }

    @Override
    public void build(DocumentWriter<String[]> output, List records) throws MappingException, IOException {
        //写头
        if(headerSupport) {
            output.writeNext(buildingStrategy.buildHeader(renderFieldConfigs));
        }

        //写身体
        if(records != null){
            for(Object bean : records) {
                try{
                    if(!bean.getClass().getName().equals(beanClass)){
                        throw new RecordException("bean not match",bean.getClass().getName()+" is not matched "+beanClass);
                    }
                    String[] recordOutput = buildingStrategy.buildBody(bean, renderFieldConfigs);
                    if((recordOutput==null || recordOutput.length==0)
                            && ignoreEmpty){
                        continue;
                    }
                    output.writeNext(recordOutput);
                }catch(MappingException e){
                    if(ignoreError){
                        //Ignore
                        LOG.error("Error will be ignore"+e.getMessage());
                    }else{
                        throw e;
                    }
                }
            }
        }

    }

    public String getBuildStrategyClass() {
        return buildStrategyClass;
    }

    public void setBuildStrategyClass(String buildStrategyClass) {
        this.buildStrategyClass = buildStrategyClass;
    }

    public List<FieldRender> getFieldRenders() {
        return fieldRenders;
    }

    public void setFieldRenders(List<FieldRender> fieldRenders) {
        this.fieldRenders = fieldRenders;
    }
}
