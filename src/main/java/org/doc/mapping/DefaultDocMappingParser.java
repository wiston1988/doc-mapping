package org.doc.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.converter.parser.DateFieldParser;
import org.doc.mapping.converter.parser.DefaultFieldParser;
import org.doc.mapping.converter.parser.FieldParser;
import org.doc.mapping.domain.DocumentParseResult;
import org.doc.mapping.domain.Field;
import org.doc.mapping.exception.ConfigException;
import org.doc.mapping.exception.FieldException;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.exception.RecordException;
import org.doc.mapping.io.DocumentReader;
import org.doc.mapping.strategy.ParsingStrategy;
import org.doc.mapping.strategy.parser.DefaultParsingStrategy;
import org.doc.mapping.util.Constants;
import org.doc.mapping.util.introspector.IntrospectUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Chen Hui
 * @since 2018/4/18
 */
public class DefaultDocMappingParser extends AbstractDocMapping implements DocMappingParser {
    private final static Log LOG = LogFactory.getLog(SubStrByLenthDocMappingParser.class);
    private int skipRowsNum = Constants.SKIP_ROWS_NUM;
    private String parseStrategyClass;
    private ParsingStrategy parsingStrategy;
    private List<FieldParser> fieldParsers;
    private final List<ParseFieldConfig> parseFieldConfigs = new ArrayList<>();

    public DefaultDocMappingParser(){
        super();
    }
    @Override
    public void init() throws ConfigException {
        super.init();
        try{
            if(parseStrategyClass==null || parseStrategyClass.trim().length()==0){
                parsingStrategy = new DefaultParsingStrategy();
            }else{
                parsingStrategy = (ParsingStrategy) Class.forName(parseStrategyClass).newInstance();
            }
            if(fieldParsers == null){
                fieldParsers = new ArrayList<>();
            }
            fieldParsers.add(0,new DateFieldParser());
            fieldParsers.add(0,new DefaultFieldParser());

            for(Field field:fields){
                if(field.getMultiColumns()!=null&&field.getMultiColumns().size()>0){
                    for(String columnName:field.getMultiColumns()){
                        parseFieldConfigs.add(constructParseFieldConfig(columnName,field));
                    }
                }else{
                    parseFieldConfigs.add(constructParseFieldConfig(field.getColumnName(),field));
                }
            }

        }catch ( InstantiationException e ){
            throw new ConfigException("InstantiationException for Parser: " + parseStrategyClass,e);
        }catch ( IllegalAccessException e ){
            throw new ConfigException("IllegalAccessException for Parser: " + parseStrategyClass,e);
        }catch ( ClassNotFoundException e ){
            throw new ConfigException("ClassNotFoundException for Parser: " + parseStrategyClass,e);
        }catch (RecordException e) {
            throw new ConfigException("Configuration error for Parser: " + parseStrategyClass,e);
        }
    }


    private ParseFieldConfig constructParseFieldConfig(String columnName,Field field) throws ConfigException, RecordException {
        ParseFieldConfig parseFieldConfig = new ParseFieldConfig();
        parseFieldConfig.setParseFormatStr(field.getFormatStr());
        parseFieldConfig.setParserName(field.getConverter());
        parseFieldConfig.setRequired(field.isRequired());
        parseFieldConfig.setName(field.getName());
        parseFieldConfig.setColumnName(columnName);
        parseFieldConfig.setGroupName(field.getName());

        if(field.getName() != null)//Get Property Descriptor if there is name specified
        {
            parseFieldConfig.setPropertyDescriptors(IntrospectUtil.introspectWriteableProperty(beanClass, field.getName()));
        }

        /***Set default parse format***/
        if(parseFieldConfig.getParseFormatStr() == null || parseFieldConfig.getParseFormatStr().trim().length() ==0){
            String defaultParseFormat = getDefaultFormatStr(parseFieldConfig.getType());
            if(defaultParseFormat != null && defaultParseFormat.trim().length() > 0) {
                parseFieldConfig.setParseFormatStr(defaultParseFormat);
            }
        }

        /***If Paser or Renderer is nonexistent, get from mapping configuration***/
        if(parseFieldConfig.getFieldParser() == null){
            int length = fieldParsers.size();
            int idx = length -1;

            while(idx >=0){
                FieldParser fieldParser = fieldParsers.get(idx);
                if(fieldParser.canConvert(parseFieldConfig)){
                    parseFieldConfig.setFieldParser(fieldParser);
                    break;
                }
                idx --;
            }
        }

        return parseFieldConfig;
    }
    @Override
    public DocumentParseResult parseDocReaderStrArr(DocumentReader<String[]> reader) throws MappingException {
        List resultList = new ArrayList<>();
        int rowNum = 0;
        int discardCount = 0;
        try{
            //解析头
            List<ParseFieldConfig> sortParseFieldConfigs = parseFieldConfigs;
            if(headerSupport) {
                String[] header = null;
                while(reader.hasNext()){
                    ++rowNum;
                    if(skipRowsNum>rowNum){
                        continue;
                    }
                    String[] lineHeader = reader.readNext();
                    if(lineHeader != null){
                        header = lineHeader;
                        break;
                    }
                }
                sortParseFieldConfigs = parseHeader(header);
            }

            //解析身体
            while(reader.hasNext()) {
                String[] line = reader.readNext();
                ++rowNum;
                if(skipRowsNum>rowNum || line == null){
                    continue;
                }
                try{
                    Object bean = parsingStrategy.parseRecord(beanClass,line,sortParseFieldConfigs);
                    if(bean==null && ignoreEmpty){
                        discardCount +=1;
                        continue;
                    }
                    resultList.add(bean);
                }catch(MappingException e){
                    if(ignoreError){
                        discardCount +=1;
                        LOG.error("Error will be ignore"+e.getMessage());
                    }else{
                        throw e;
                    }
                }
            }

            return new DocumentParseResult(resultList,rowNum,discardCount);
        }catch(Exception e){
            LOG.error(e.getMessage(),e);
            if (e instanceof FieldException) {
                throw new MappingException("exception when parse row:"+rowNum+" field:"+((FieldException)e).getFieldValue(),e);
            }else {
                throw new MappingException("exception when parse row:" + rowNum, e);
            }
        }
    }

    @Override
    public DocumentParseResult parseDocReaderStr(DocumentReader<String> reader) throws MappingException {
        List resultList = new ArrayList<>();
        int rowNum =0;
        int discardCount = 0;

        try{
            //解析身体
            while(reader.hasNext()) {
                String line = reader.readNext();
                ++rowNum;
                if(skipRowsNum>rowNum || line == null){
                    continue;
                }
                try{
                    String[] cellArr = readBodyLine(line,parseFieldConfigs);
                    Object bean = parsingStrategy.parseRecord(beanClass,cellArr,parseFieldConfigs);
                    if(bean==null && ignoreEmpty){
                        discardCount +=1;
                        continue;
                    }
                    resultList.add(bean);
                }catch(MappingException e){
                    if(ignoreError){
                        discardCount +=1;
                        LOG.error("Error will be ignore"+e.getMessage());
                    }else{
                        throw e;
                    }
                }
            }

            return new DocumentParseResult(resultList,rowNum,discardCount);
        }catch(Exception e){
            LOG.error(e.getMessage(),e);
            if (e instanceof FieldException) {
                throw new MappingException("exception when parse row:"+rowNum+" field:"+((FieldException)e).getFieldValue(),e);
            }else {
                throw new MappingException("exception when parse row:" + rowNum, e);
            }
        }
    }
    protected String[] readBodyLine(String line,List<ParseFieldConfig> parseFieldConfigs) throws RecordException {
        String[] cellArr = new String[parseFieldConfigs.size()];
        for(int i=0;i<parseFieldConfigs.size();i++){
            ParseFieldConfig parseFieldConfig = parseFieldConfigs.get(i);
            String columnName = parseFieldConfig.getColumnName();
            if(columnName == null) {
                throw new RecordException("SubstrExpress bind-to-column should be configured!");
            }
            String[] expressArr = columnName.split(",");
            if(expressArr.length != 2) {
                throw new RecordException("SubstrExpress bind-to-column should be like [10,20]...");
            }

            int start = 0;
            int end = 0;
            try{
                start = Integer.valueOf(expressArr[0]);
                end = Integer.valueOf(expressArr[1]);
            }catch(NumberFormatException e){
                throw new RecordException("Invalid Config of SubstrExpress for column："+columnName);
            }
            if(start < 0 || start > end) {
                throw new RecordException("Invalid Config of SubstrExpress for Record: start should be smaller than end and greater than 0!");
            }
            if(end > line.length()) {
                throw new RecordException("This Line is too short to parse correctly.");
            }
            cellArr[i] = line.substring(start, end);
        }
        return cellArr;
    }

    protected List<ParseFieldConfig> parseHeader(String[] header)throws MappingException, IOException {
        if(header == null || header.length == 0) {
            return null;
        }
        Set<String> columns = new HashSet<>();
        List<ParseFieldConfig> fieldConfigs = new ArrayList<>();
        for(int i=0;i<header.length;i++){
            String columnName = "";
            if(header[i]!=null){
                columnName = header[i].trim().toLowerCase();//LowerCase all header, to ignore case
            }
            if(columns.contains(columnName)){
                String errMsg = "Duplicate column name in header:"+columnName;
                throw new RecordException(errMsg);
            }
            columns.add(columnName);
            ParseFieldConfig fieldConfig = getConfigByColumnName(columnName);
            fieldConfigs.add(fieldConfig);
        }
        return fieldConfigs;
    }
    private ParseFieldConfig getConfigByColumnName(String columnName){
        int size = parseFieldConfigs.size();
        for(int i=0;i<size;i++){
            ParseFieldConfig currConfig = parseFieldConfigs.get(i);
            if(columnName.trim().equalsIgnoreCase(currConfig.getColumnName())) {
                return currConfig;
            }
        }
        return null;
    }
    public String getParseStrategyClass() {
        return parseStrategyClass;
    }

    public void setParseStrategyClass(String parseStrategyClass) {
        this.parseStrategyClass = parseStrategyClass;
    }

    public int getSkipRowsNum() {
        return skipRowsNum;
    }

    public void setSkipRowsNum(int skipRowsNum) {
        this.skipRowsNum = skipRowsNum;
    }

    public List<FieldParser> getFieldParsers() {
        return fieldParsers;
    }

    public void setFieldParsers(List<FieldParser> fieldParsers) {
        this.fieldParsers = fieldParsers;
    }
}
