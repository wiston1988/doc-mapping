package org.doc.mapping;


import org.doc.mapping.domain.Field;
import org.doc.mapping.exception.ConfigException;
import org.doc.mapping.util.Constants;
import org.doc.mapping.util.introspector.IntrospectUtil;

import java.util.Date;
import java.util.List;

/**
 * @author Chen Hui
 * @since 2018/4/18
 */
abstract class AbstractDocMapping {

    protected String defaultDateFormat = Constants.DEFAULT_DATE_FORMAT;
    protected String defaultNumberFormat = Constants.DEFAULT_NUMBER_FORMAT;
    protected boolean ignoreError = Constants.IGNORE_ERROR;
    protected boolean ignoreEmpty = Constants.IGNORE_EMPTY;
    protected boolean headerSupport = Constants.HEADER_SUPPORT;
    protected String beanClass;
    protected List<Field> fields;

    public void init()throws ConfigException{
        if(beanClass==null){
            throw new ConfigException("beanClass should be configured!");
        }
        if(fields==null || fields.size()==0){
            throw new ConfigException("fields should be configured!");
        }
    }
    protected String getDefaultFormatStr(Class type){
        if(IntrospectUtil.isTypeMatch(Date.class, type)){
            return defaultDateFormat;
        }else if(IntrospectUtil.isTypeMatch(Number.class, type)){
            return defaultNumberFormat;
        }
        return null;
    }
    public String getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public void setDefaultDateFormat(String defaultDateFormat) {
        this.defaultDateFormat = defaultDateFormat;
    }

    public String getDefaultNumberFormat() {
        return defaultNumberFormat;
    }

    public void setDefaultNumberFormat(String defaultNumberFormat) {
        this.defaultNumberFormat = defaultNumberFormat;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public boolean isIgnoreError() {
        return ignoreError;
    }

    public void setIgnoreError(boolean ignoreError) {
        this.ignoreError = ignoreError;
    }

    public boolean isIgnoreEmpty() {
        return ignoreEmpty;
    }

    public void setIgnoreEmpty(boolean ignoreEmpty) {
        this.ignoreEmpty = ignoreEmpty;
    }

    public boolean isHeaderSupport() {
        return headerSupport;
    }

    public void setHeaderSupport(boolean headerSupport) {
        this.headerSupport = headerSupport;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
