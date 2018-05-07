package org.doc.mapping.spring.annotation;

import org.doc.mapping.DefaultDocMappingBuilder;
import org.doc.mapping.DefaultDocMappingParser;
import org.doc.mapping.converter.parser.FieldParser;
import org.doc.mapping.converter.render.FieldRender;
import org.doc.mapping.domain.Field;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Chen Hui
 */
public class DocMappingRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Class docMappingClass;
        try {
            docMappingClass = Class.forName(importingClassMetadata.getClassName());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }
        registerParser(importingClassMetadata,docMappingClass,registry);
        registerBuilder(importingClassMetadata,docMappingClass, registry);
    }

    private void registerParser(AnnotationMetadata importingClassMetadata,Class docMappingClass,BeanDefinitionRegistry registry){
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(DocMappingParser.class.getName()));
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DefaultDocMappingParser.class);
        if(attributes == null){
            return;
        }
        if(!StringUtils.hasText(attributes.getString("id"))){
            throw new RuntimeException("annotation DocMappingParser must have id attribute!");
        }
        String beanId = attributes.getString("id");
        String defaultDateFormat = attributes.getString("defaultDateFormat");
        String defaultNumberFormat = attributes.getString("defaultNumberFormat");
        boolean ignoreError = attributes.getBoolean("ignoreError");
        boolean ignoreEmpty = attributes.getBoolean("ignoreEmpty");
        boolean headerSupport = attributes.getBoolean("headerSupport");
        Integer skipRowsNum = attributes.getNumber("skipRowsNum");
        String parseStrategyClass = attributes.getString("parseStrategyClass");
        String[] fieldParserArr = attributes.getStringArray("fieldParsers");
        if (StringUtils.hasText(defaultDateFormat)) {
            builder.addPropertyValue("defaultDateFormat", defaultDateFormat);
        }
        if (StringUtils.hasText(defaultNumberFormat)) {
            builder.addPropertyValue("defaultNumberFormat", defaultNumberFormat);
        }
        builder.addPropertyValue("beanClass", importingClassMetadata.getClassName());
        builder.addPropertyValue("ignoreError", ignoreError);
        builder.addPropertyValue("ignoreEmpty", ignoreEmpty);
        builder.addPropertyValue("headerSupport", headerSupport);
        builder.addPropertyValue("skipRowsNum", skipRowsNum);
        if (StringUtils.hasText(parseStrategyClass)) {
            builder.addPropertyValue("parseStrategyClass", parseStrategyClass);
        }
        if(fieldParserArr!=null && fieldParserArr.length>0){
            List<FieldParser> fieldParsers = new ArrayList<>();
            for(String fieldParserClass:fieldParserArr){
                try {
                    fieldParsers.add((FieldParser)Class.forName(fieldParserClass).newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(),e);
                }
            }
            builder.addPropertyValue("fieldParsers", fieldParsers);
        }

        builder.addPropertyValue("fields", getFields(docMappingClass.getDeclaredFields()));
        builder.setInitMethodName("init");
        registry.registerBeanDefinition(beanId, builder.getBeanDefinition());
    }
    private void registerBuilder(AnnotationMetadata importingClassMetadata,Class docMappingClass,BeanDefinitionRegistry registry){
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(DocMappingBuilder.class.getName()));
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DefaultDocMappingBuilder.class);
        if(attributes == null){
            return;
        }
        if(!StringUtils.hasText(attributes.getString("id"))){
            throw new RuntimeException("annotation DocMappingBuilder must have id attribute!");
        }
        String beanId = attributes.getString("id");
        String defaultDateFormat = attributes.getString("defaultDateFormat");
        String defaultNumberFormat = attributes.getString("defaultNumberFormat");
        boolean ignoreError = attributes.getBoolean("ignoreError");
        boolean ignoreEmpty = attributes.getBoolean("ignoreEmpty");
        boolean headerSupport = attributes.getBoolean("headerSupport");
        String buildStrategyClass = attributes.getString("buildStrategyClass");
        String[] fieldRenderArr = attributes.getStringArray("fieldRenders");
        if (StringUtils.hasText(defaultDateFormat)) {
            builder.addPropertyValue("defaultDateFormat", defaultDateFormat);
        }
        if (StringUtils.hasText(defaultNumberFormat)) {
            builder.addPropertyValue("defaultNumberFormat", defaultNumberFormat);
        }
        builder.addPropertyValue("beanClass", importingClassMetadata.getClassName());
        builder.addPropertyValue("ignoreError", ignoreError);
        builder.addPropertyValue("ignoreEmpty", ignoreEmpty);
        builder.addPropertyValue("headerSupport", headerSupport);
        if (StringUtils.hasText(buildStrategyClass)) {
            builder.addPropertyValue("buildStrategyClass", buildStrategyClass);
        }
        if(fieldRenderArr!=null && fieldRenderArr.length>0){
            List<FieldRender> fieldRenders = new ArrayList<>();
            for(String fieldRenderClass:fieldRenderArr){
                try {
                    fieldRenders.add((FieldRender)Class.forName(fieldRenderClass).newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(),e);
                }
            }
            builder.addPropertyValue("fieldRenders", fieldRenders);
        }

        builder.addPropertyValue("fields", getFields(docMappingClass.getDeclaredFields()));
        builder.setInitMethodName("init");
        registry.registerBeanDefinition(beanId, builder.getBeanDefinition());
    }
    private List<Field> getFields(java.lang.reflect.Field[] fieldArr){
        if(fieldArr == null || fieldArr.length == 0){
            throw new RuntimeException("annotation DocMappingParser or DocMappingBuilder bean must have at least one field!");
        }
        List<Field> docMappingFields = new ArrayList<>();
        constructField("", fieldArr, docMappingFields);
        if(docMappingFields.size()==0){
            throw new RuntimeException("annotation DocMappingParser or DocMappingBuilder bean must have at least one field annotation!");
        }
        return docMappingFields;
    }

    private void constructField(String preFieldName, java.lang.reflect.Field[] fieldArr, List<Field> docMappingFields){
        for(java.lang.reflect.Field field:fieldArr){
            if(field.getType().isPrimitive()
                    || field.getType().getName().startsWith("java.")){
                DocMappingField docMappingFieldAnnotation = field.getDeclaredAnnotation(DocMappingField.class);
                if (docMappingFieldAnnotation == null) {
                    continue;
                }
                Field docMappingField = new Field();
                docMappingField.setName(preFieldName + field.getName());
                if (StringUtils.hasText(docMappingFieldAnnotation.columnName())) {
                    docMappingField.setColumnName(docMappingFieldAnnotation.columnName());
                }
                if (StringUtils.hasText(docMappingFieldAnnotation.multiColumns())) {
                    String[] columnArr = docMappingFieldAnnotation.multiColumns().split(",");
                    docMappingField.setMultiColumns(Arrays.asList(columnArr));
                }

                docMappingField.setRequired(docMappingFieldAnnotation.required());

                if (StringUtils.hasText(docMappingFieldAnnotation.converter())) {
                    docMappingField.setConverter(docMappingFieldAnnotation.converter());
                }
                if (StringUtils.hasText(docMappingFieldAnnotation.formatStr())) {
                    docMappingField.setFormatStr(docMappingFieldAnnotation.formatStr());
                }
                docMappingFields.add(docMappingField);
            }else{
                if(field.isSynthetic()){//非静态成员类里面，为了访问外部类的成员，编译器会给它生成一个额外的成员,那么这个属性需要忽略
                    continue;
                }
                constructField(preFieldName+field.getName()+".", field.getType().getDeclaredFields(), docMappingFields);
            }
        }

    }


}
