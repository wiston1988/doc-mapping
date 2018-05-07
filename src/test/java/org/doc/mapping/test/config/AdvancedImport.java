package org.doc.mapping.test.config;

import org.doc.mapping.spring.annotation.DocMappingField;
import org.doc.mapping.spring.annotation.DocMappingParser;
import org.doc.mapping.test.model.People;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Chen Hui
 * @since 2018/5/7
 */
@Configuration
@DocMappingParser(id = "advancedImport",defaultDateFormat = "yyyyMMdd",defaultNumberFormat = "5")
public class AdvancedImport {
    @DocMappingField(columnName = "UpperCase Column",required = true,formatStr = "20",converter = "org.doc.mapping.test.parsers.ToUpperCaseParser")
    private String upperCaseField;
    @DocMappingField(columnName = "StringParserThrowE Column",formatStr = "30",converter = "org.doc.mapping.test.parsers.ToStringParserThrowE")
    private String stringParserThrowEField;
    @DocMappingField(columnName = "Date Column",required = true,formatStr = "MM/dd/yyyy;yyyyMMdd",converter = "org.doc.mapping.test.parsers.AdvancedDateParser")
    private Date advancedDateField;
    @DocMappingField(columnName = "Long Column",required = true,formatStr = "12",converter = "org.doc.mapping.test.parsers.ToLongParserThrowE")
    private Long longField;
    @DocMappingField(columnName = "BigDecimal Column",required = true,formatStr = "^-?\\d{1,3}(\\.\\d{0,5})?$",converter = "org.doc.mapping.test.parsers.ToBigDecimalParserThrowE")
    private BigDecimal bigDecimalField;
    private People people;

    public String getUpperCaseField() {
        return upperCaseField;
    }
    public void setUpperCaseField(String upperCaseField) {
        this.upperCaseField = upperCaseField;
    }
    public String getStringParserThrowEField() {
        return stringParserThrowEField;
    }
    public void setStringParserThrowEField(String stringParserThrowEField) {
        this.stringParserThrowEField = stringParserThrowEField;
    }
    public Date getAdvancedDateField() {
        return advancedDateField;
    }
    public void setAdvancedDateField(Date advancedDateField) {
        this.advancedDateField = advancedDateField;
    }
    public Long getLongField() {
        return longField;
    }
    public void setLongField(Long longField) {
        this.longField = longField;
    }
    public BigDecimal getBigDecimalField() {
        return bigDecimalField;
    }
    public void setBigDecimalField(BigDecimal bigDecimalField) {
        this.bigDecimalField = bigDecimalField;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    @Override
    public String toString() {
        return "AdvancedBean [upperCaseField=" + upperCaseField
                + ", stringParserThrowEField=" + stringParserThrowEField
                + ", advancedDateField=" + advancedDateField + ", longField="
                + longField + ", bigDecimalField=" + bigDecimalField
                + ", people=" + people + "]";
    }
}
