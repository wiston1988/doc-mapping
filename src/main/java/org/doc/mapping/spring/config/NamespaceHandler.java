package org.doc.mapping.spring.config;

import org.doc.mapping.DefaultDocMappingBuilder;
import org.doc.mapping.DefaultDocMappingParser;
import org.doc.mapping.converter.parser.FieldParser;
import org.doc.mapping.domain.Field;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class NamespaceHandler extends NamespaceHandlerSupport {

  @Override
  public void init() {
    registerBeanDefinitionParser("parser", new BeanParser());
    registerBeanDefinitionParser("builder", new BeanBuilder());
  }

  static class BeanParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
      return DefaultDocMappingParser.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
      Object source = parserContext.extractSource(element);
      String defaultDateFormat = element.getAttribute("defaultDateFormat");
      String defaultNumberFormat = element.getAttribute("defaultNumberFormat");
      String beanClass = element.getAttribute("beanClass");
      String ignoreError = element.getAttribute("ignoreError");
      String ignoreEmpty = element.getAttribute("ignoreEmpty");
      String headerSupport = element.getAttribute("headerSupport");
      String skipRowsNum = element.getAttribute("skipRowsNum");
      String parseStrategyClass = element.getAttribute("parseStrategyClass");
      if (StringUtils.hasText(defaultDateFormat)) {
        builder.addPropertyValue("defaultDateFormat", defaultDateFormat);
      }
      if (StringUtils.hasText(defaultNumberFormat)) {
        builder.addPropertyValue("defaultNumberFormat", defaultNumberFormat);
      }
      if (StringUtils.hasText(beanClass)) {
        builder.addPropertyValue("beanClass", beanClass);
      }
      if (StringUtils.hasText(ignoreError)) {
        builder.addPropertyValue("ignoreError", Boolean.valueOf(ignoreError));
      }
      if (StringUtils.hasText(ignoreEmpty)) {
        builder.addPropertyValue("ignoreEmpty", Boolean.valueOf(ignoreEmpty));
      }
      if (StringUtils.hasText(headerSupport)) {
        builder.addPropertyValue("headerSupport", Boolean.valueOf(headerSupport));
      }
      if (StringUtils.hasText(skipRowsNum)) {
        builder.addPropertyValue("skipRowsNum", Integer.valueOf(skipRowsNum));
      }
      if (StringUtils.hasText(parseStrategyClass)) {
        builder.addPropertyValue("parseStrategyClass", parseStrategyClass);
      }

      builder.addPropertyValue("fieldParsers", getFieldParsers(element,source,parserContext));
      builder.addPropertyValue("fields",getFields(element));
      builder.setInitMethodName("init");
    }
    private ManagedList<?> getFieldParsers(Element element, Object source, ParserContext parserContext){
      Element parsersElement = DomUtils.getChildElementByTagName(element, "field-parsers");
      ManagedList<? super Object> fieldParsers = new ManagedList<Object>();
      if (parsersElement != null) {
        fieldParsers.setSource(source);
        for (Element beanElement : DomUtils.getChildElementsByTagName(parsersElement, "bean", "ref")) {
          Object object = parserContext.getDelegate().parsePropertySubElement(beanElement, null);
          fieldParsers.add(object);
        }
      }
      return fieldParsers;
    }

    private List<Field> getFields(Element element){
      Element fieldsElement = DomUtils.getChildElementByTagName(element, "fields");
      List<Element> fieldElements = DomUtils.getChildElementsByTagName(fieldsElement, "field");
      List<Field> fields = new ArrayList<>();
      for (Element fieldElement: fieldElements) {
          Field field = new Field();
          String name = fieldElement.getAttribute("name");
          String columName = fieldElement.getAttribute("columnName");
          String multiColumns = fieldElement.getAttribute("multiColumns");
          String required = fieldElement.getAttribute("required");
          String converter = fieldElement.getAttribute("converter");
          String formatStr = fieldElement.getAttribute("formatStr");
          if (StringUtils.hasText(name)) {
            field.setName(name);
          }
          if (StringUtils.hasText(columName)) {
            field.setColumnName(columName);
          }
          if (StringUtils.hasText(multiColumns)) {
            String[] columnArr = multiColumns.split(",");
            field.setMultiColumns(Arrays.asList(columnArr));
          }
          if (StringUtils.hasText(required)) {
            field.setRequired(Boolean.valueOf(required));
          }
          if (StringUtils.hasText(converter)) {
            field.setConverter(converter);
          }
          if (StringUtils.hasText(formatStr)) {
            field.setFormatStr(formatStr);
          }
          fields.add(field);
      }
      return fields;

    }
  }
  static class BeanBuilder extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
      return DefaultDocMappingBuilder.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
      Object source = parserContext.extractSource(element);
      String defaultDateFormat = element.getAttribute("defaultDateFormat");
      String defaultNumberFormat = element.getAttribute("defaultNumberFormat");
      String beanClass = element.getAttribute("beanClass");
      String ignoreError = element.getAttribute("ignoreError");
      String ignoreEmpty = element.getAttribute("ignoreEmpty");
      String headerSupport = element.getAttribute("headerSupport");
      String buildStrategyClass = element.getAttribute("buildStrategyClass");
      if (StringUtils.hasText(defaultDateFormat)) {
        builder.addPropertyValue("defaultDateFormat", defaultDateFormat);
      }
      if (StringUtils.hasText(defaultNumberFormat)) {
        builder.addPropertyValue("defaultNumberFormat", defaultNumberFormat);
      }
      if (StringUtils.hasText(beanClass)) {
        builder.addPropertyValue("beanClass", beanClass);
      }
      if (StringUtils.hasText(ignoreError)) {
        builder.addPropertyValue("ignoreError", Boolean.valueOf(ignoreError));
      }
      if (StringUtils.hasText(ignoreEmpty)) {
        builder.addPropertyValue("ignoreEmpty", Boolean.valueOf(ignoreEmpty));
      }
      if (StringUtils.hasText(headerSupport)) {
        builder.addPropertyValue("headerSupport", Boolean.valueOf(headerSupport));
      }
      if (StringUtils.hasText(buildStrategyClass)) {
        builder.addPropertyValue("buildStrategyClass", buildStrategyClass);
      }
      builder.addPropertyValue("fieldRenders", getFieldRenders(element,source,parserContext));
      builder.addPropertyValue("fields",getFields(element));
      builder.setInitMethodName("init");
    }
    private ManagedList<?> getFieldRenders(Element element, Object source, ParserContext parserContext){
      Element parsersElement = DomUtils.getChildElementByTagName(element, "field-renders");
      ManagedList<? super Object> fieldRenders = new ManagedList<Object>();
      if (parsersElement != null) {
        fieldRenders.setSource(source);
        for (Element beanElement : DomUtils.getChildElementsByTagName(parsersElement, "bean", "ref")) {
          Object object = parserContext.getDelegate().parsePropertySubElement(beanElement, null);
          fieldRenders.add(object);
        }
      }
      return fieldRenders;
    }

    private List<Field> getFields(Element element){
      Element fieldsElement = DomUtils.getChildElementByTagName(element, "fields");
      List<Element> fieldElements = DomUtils.getChildElementsByTagName(fieldsElement, "field");
      List<Field> fields = new ArrayList<>();
      for (Element fieldElement: fieldElements) {
        Field field = new Field();
        String name = fieldElement.getAttribute("name");
        String columName = fieldElement.getAttribute("columnName");
        String multiColumns = fieldElement.getAttribute("multiColumns");
        String required = fieldElement.getAttribute("required");
        String converter = fieldElement.getAttribute("converter");
        String formatStr = fieldElement.getAttribute("formatStr");
        if (StringUtils.hasText(name)) {
          field.setName(name);
        }
        if (StringUtils.hasText(columName)) {
          field.setColumnName(columName);
        }
        if (StringUtils.hasText(multiColumns)) {
          String[] columnArr = multiColumns.split(",");
          field.setMultiColumns(Arrays.asList(columnArr));
        }
        if (StringUtils.hasText(required)) {
          field.setRequired(Boolean.valueOf(required));
        }
        if (StringUtils.hasText(converter)) {
          field.setConverter(converter);
        }
        if (StringUtils.hasText(formatStr)) {
          field.setFormatStr(formatStr);
        }
        fields.add(field);
      }
      return fields;

    }
  }
}
