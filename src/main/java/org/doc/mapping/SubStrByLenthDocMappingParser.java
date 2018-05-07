package org.doc.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.doc.mapping.config.ParseFieldConfig;
import org.doc.mapping.exception.RecordException;
import org.doc.mapping.strategy.parser.DefaultParsingStrategy;

import java.util.List;

/**
 * @author Chen Hui
 * @since 2018/4/18
 */
public class SubStrByLenthDocMappingParser extends DefaultDocMappingParser {
    private final static Log LOG = LogFactory.getLog(SubStrByLenthDocMappingParser.class);

    @Override
    protected String[] readBodyLine(String line,List<ParseFieldConfig> parseFieldConfigs) throws RecordException {
        String[] cellArr = new String[parseFieldConfigs.size()];
        int start = 0;
        for(int i=0;i<parseFieldConfigs.size();i++){
            ParseFieldConfig parseFieldConfig = parseFieldConfigs.get(i);
            String columnName = parseFieldConfig.getColumnName();
            if(columnName == null) {
                throw new RecordException("SubstrExpress columnName should be configured!");
            }
            int strLength =0;
            try{
                strLength = Integer.valueOf(columnName);
                if(strLength<=0){
                    throw new RecordException("SubstrExpress columnName should be positive number："+columnName);
                }
            }catch(NumberFormatException e){
                throw new RecordException("SubstrExpress columnName should be number："+columnName);
            }
            int end = start+strLength;

            if(end > line.length()) {
                throw new RecordException("This Line is too short to parse correctly.");
            }
            cellArr[i] = line.substring(start, end);
        }
        return cellArr;
    }

}
